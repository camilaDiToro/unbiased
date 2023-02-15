import { createContext, useContext, useEffect, useState } from "react";
import { useI18n } from "../customI18n";
import axios from "axios";
import { baseURL } from "../constants";
import { useRouter } from "next/router";
import { useSnackbar } from "notistack";
import { useApi } from "../api";
import {
  ACCESS_DENIED,
  CONN_TIMEOUT,
  FORBIDDEN, INVALID_JWT_CLAIM,
  SERVER_ERROR,
  UNAUTHORIZED,
  UNKNOWN,
} from "../errors";
export const AppContext = createContext(null);

export default function AppWrapper({ children }) {
  const I18n = useI18n();
  const { enqueueSnackbar, closeSnackbar } = useSnackbar();
  let newJwt = {};
  if (typeof window !== "undefined") {
    newJwt = {
      accessToken: localStorage.getItem("accessToken"),
      refreshToken: localStorage.getItem("refreshToken"),
    };
  }
  const jwtState = useState(newJwt);
  const [loggedUser, setLoggedUser] = useState(undefined);

  const router = useRouter();
  const axiosInstance = axios.create({
    baseURL,
  });

  const isAdminPath = (path) => {
    return path.includes("/admin");
  };

  const isOwnerPath = (path) => {
    return path.includes("/manage-admins");
  };

  const showUnauthorized = () => {
    enqueueSnackbar(I18n(`${UNAUTHORIZED}`));
    router.replace("/");
  };

  const showForbidden = () => {
    enqueueSnackbar(I18n(`${FORBIDDEN}`));
    router.replace("/");
  };

  useEffect(() => {
    if(!router.isReady || loggedUser === undefined) {
      return
    }
    if (isAdminPath(router.pathname)) {
      if (!loggedUser) {
        showUnauthorized();
      } else if (!loggedUser.isAdmin) {
        showForbidden();
      }
    }
    else if (isOwnerPath(router.pathname)) {
      if (!loggedUser) {
        showUnauthorized();
      } else if (!loggedUser.authorities.includes("ROLE_OWNER")) {
        showForbidden();
      }
    }
    else if (router.pathname.includes("/create-article")) {
      if (!loggedUser) {
        showUnauthorized();
      }
    }
  }, [loggedUser,router.isReady]);

  const setHeadersIfExist = (response) => {
    if (response) {
      const accessHeader = response.headers.get("access-token");
      const refreshHeader = response.headers.get("refresh-token");
      if (accessHeader && refreshHeader) {
        const accessToken = response.headers.get("access-token").split(" ")[1];
        const refreshToken = response.headers
          .get("refresh-token")
          .split(" ")[1];
        jwtState[1]({ accessToken, refreshToken });
      }
    }
  };

  axiosInstance.interceptors.request.use(function (config) {
    const { accessToken, refreshToken } = jwt;
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    } else if (refreshToken) {
      config.headers.Authorization = `Bearer ${refreshToken}`;
    }

    return config;
  });

  const getErrorCodes = (error) => {
    if (error.code === "ERR_NETWORK") return CONN_TIMEOUT;
    if (error.status === 500) {
      return [SERVER_ERROR];
    }
    if (
      !error.response ||
      !error.response.data
    ) {
      return [UNKNOWN];
    }
    if (Array.isArray(error.response.data)) {
      return error.response.data.map(a => a.apiCode)
    }
    return [error.response.data.apiCode ? error.response.data.apiCode : UNKNOWN]
  };

  const showError = (code) => {
    if (code === 604) return;
    enqueueSnackbar(I18n(`${code}`));
  };

  axiosInstance.interceptors.response.use(
    async (r) => {
      setHeadersIfExist(r);
      return r;
    },
    async (error) => {
      setHeadersIfExist(error.response);

      const codes = getErrorCodes(error);

      if (
        (error.config.login || error.config.register) &&
        error.response.status === 404
      ) {
        enqueueSnackbar(
          error.config.login
            ? I18n("login.succesful")
            : I18n("regoster.succesful")
        );
      } else {
        if (!error.config.hideError) {
          for (const code of codes) {
            showError(code);
          }
        }
      }

      const loginURL = "/login/";
      if (router.pathname !== loginURL) {
        if (codes.includes(UNAUTHORIZED) || codes.includes(INVALID_JWT_CLAIM) || codes.includes(FORBIDDEN) || codes.includes(ACCESS_DENIED)) {
          jwtState[1]({});
        } else if (codes.includes(604)) {
          if (jwt.accessToken) {
            jwtState[1]({ refreshToken: jwt.refreshToken });
            const config = error.config;
            config.headers.Authorization = `Bearer ${jwt.refreshToken}`;
            return axios.request(config);
          } else {
            jwtState[1]({});
            if (!error.config.authOptional) {
              await router.push(loginURL);
            }
          }
        }
      }

      return Promise.reject(error);
    }
  );

  const jwt = jwtState[0];

  useEffect(() => {
    if (jwt.accessToken) {
      localStorage.setItem("accessToken", jwt.accessToken);
      const loggedUser = JSON.parse(atob(jwt.accessToken.split(".")[1]));
      setLoggedUser({
        ...loggedUser,
        nameOrEmail: loggedUser.username,
        hasImage: !!loggedUser.imageLink,
        image: loggedUser.imageLink,
        id: loggedUser.userId,
      });
    } else {
      localStorage.removeItem("accessToken");
    }
    if (jwt.refreshToken) {
      localStorage.setItem("refreshToken", jwt.refreshToken);
    } else {
      localStorage.removeItem("refreshToken");
    }
    if (!jwt.accessToken && !jwt.refreshToken) {
      setLoggedUser(null);
    }
  }, [jwt]);

  let sharedState = {
    I18n,
    loggedUser,
    axios: axiosInstance,
    jwtState,
    setLoggedUser,
    api: useApi(loggedUser, axiosInstance, I18n),
  };

  return (
    <AppContext.Provider value={sharedState}>{children}</AppContext.Provider>
  );
}

export function useAppContext() {
  return useContext(AppContext);
}
