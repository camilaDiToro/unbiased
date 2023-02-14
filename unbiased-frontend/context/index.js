import {createContext, useContext, useEffect, useState} from 'react';
import {useI18n} from "../customI18n"
import axios from "axios";
import {baseURL} from "../constants";
import {useRouter} from "next/router";
import {useSnackbar} from "notistack";
import {useApi} from "../api";
import {CONN_TIMEOUT, UNKNOWN} from "../errors";
export const AppContext = createContext(null);

export default function AppWrapper({ children }) {
    const I18n = useI18n()
    const { enqueueSnackbar, closeSnackbar } = useSnackbar();
    let newJwt = {}
    if (typeof window !== 'undefined') {
        newJwt = {        accessToken: localStorage.getItem('accessToken'), refreshToken: localStorage.getItem('refreshToken')
        }
    }
    const jwtState = useState(newJwt)
    const [loggedUser, setLoggedUser] = useState(null)

    const router = useRouter()
    const axiosInstance = axios.create({
        baseURL
    })

    const isAdminPath = (path) => {
        return path.includes('/admin')
    }

    const isOwnerPath = (path) => {
        return path.includes('/manage-admins')
    }

    useEffect(() => {
        if (isAdminPath(router.pathname)) {
            if (!loggedUser || !loggedUser.isAdmin) {
                router.replace("/")
            }
        }
        if (isOwnerPath(router.pathname)) {
            if (!loggedUser || !loggedUser.authorities.includes('ROLE_OWNER')) {
                router.replace("/")
            }
        }

    }, [loggedUser])



    const setHeadersIfExist = (response) => {
        if (response) {
            const accessHeader = response.headers.get('access-token')
            const refreshHeader = response.headers.get('refresh-token')
            if (accessHeader && refreshHeader) {
                const accessToken = response.headers.get('access-token').split(' ')[1]
                const refreshToken = response.headers.get('refresh-token').split(' ')[1]
                jwtState[1]({accessToken, refreshToken})
            }
        }
    }

    axiosInstance.interceptors.request.use(function (config) {
        const {accessToken, refreshToken} = jwt
        if (accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`
        } else if(refreshToken) {
            config.headers.Authorization = `Bearer ${refreshToken}`
        }
        return config;
    })

    const getErrorCode = (error) => {

        if (error.code === 'ERR_NETWORK')
            return CONN_TIMEOUT;
        if (!error.response || !error.response.data || !error.response.data.apiCode) {
            return UNKNOWN
        }

        return error.response.data.apiCode
    }

    const showError = (code) => {
        if (code === 604)
            return
        enqueueSnackbar(I18n(`${code}`))
    }

    axiosInstance.interceptors.response.use((r) => {
        setHeadersIfExist(r)
        return r;
    }, async (error) => {
        setHeadersIfExist(error.response)
        const code = getErrorCode(error)

        if ((error.config.login || error.config.register) && error.response.status === 404) {
            enqueueSnackbar(error.config.login ? I18n("login.succesful") : I18n("regoster.succesful"))
        } else {
            if (!error.config.hideError) {
                showError(code)
            }
        }



        const loginURL = '/login/'
        if (router.pathname !== loginURL) {
            if (code === 606 || code  === 603 || code  === 605|| code  === 600) {
                jwtState[1]({})
            } else if (code === 604) {
                if(jwt.accessToken) {
                    jwtState[1]({refreshToken: jwt.refreshToken})
                    const config = error.config
                    config.headers.Authorization = `Bearer ${jwt.refreshToken}`
                    return axios.request(config)
                } else {
                    jwtState[1]({})
                    if (!error.config.authOptional) {
                        await router.push(loginURL)
                    }
                }
            }
        }

        return Promise.reject(error);
    });

    const jwt = jwtState[0]


    useEffect(() => {
        // alert('setting jwt to ' + JSON.stringify(jwt))
        if (jwt.accessToken) {
            localStorage.setItem('accessToken', jwt.accessToken)
        } else {
            localStorage.removeItem('accessToken')
        }
        if (jwt.refreshToken) {
            localStorage.setItem('refreshToken', jwt.refreshToken)
            const loggedUser = JSON.parse(atob(jwt.refreshToken.split('.')[1]))
            console.log(loggedUser)
            setLoggedUser({...loggedUser, nameOrEmail: loggedUser.username, hasImage: !!loggedUser.imageLink,
            image: loggedUser.imageLink, id: loggedUser.userId})

        } else {
            localStorage.removeItem('refreshToken')
        }
        if (!jwt.accessToken && !jwt.refreshToken) {
            setLoggedUser(null)
        }
        // alert(JSON.stringify(jwt))
    }, [jwt])


    let sharedState = {I18n, loggedUser, axios: axiosInstance,jwtState,api :useApi(loggedUser, axiosInstance)}




    return (
        <AppContext.Provider value={sharedState}>
            {children}
        </AppContext.Provider>
    );
}

export function useAppContext() {
    return useContext(AppContext);
}