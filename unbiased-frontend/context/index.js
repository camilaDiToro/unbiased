import {createContext, useContext, useEffect, useState} from 'react';
import I18n from "../i18n"
import axios from "axios";
import baseURL from "../pages/back";
import {useRouter} from "next/router";
const AppContext = createContext(null);

export default function AppWrapper({ children }) {
    const [errorDetails, setErrorDetails] = useState({})
    let newJwt = {}
    if (typeof window !== 'undefined') {
        newJwt = {        accessToken: localStorage.getItem('accessToken'), refreshToken: localStorage.getItem('refreshToken')
        }
    }
    const jwtState = useState(newJwt)
    const [loggedUser, setLoggedUser] = useState(null)
    // useEffect(()=> {
    //
    //     alert('About to set to ' + JSON.stringify(newJwt))
    //     jwtState[1](newJwt)
    // }, [])
    const router = useRouter()
    const axiosInstance = axios.create({
        baseURL: baseURL.href,
        transformRequest: [function (data, headers) {
            // Do whatever you want to transform the data
            const {accessToken, refreshToken} = jwt
            if (accessToken) {
                headers.Authorization = `Bearer ${accessToken}`
            } else if(refreshToken) {
                // alert('setting refreshToken')
                headers.Authorization = `Bearer ${refreshToken}`
            }
            return data;
        }]
    })

    const setHeadersIfExist = (response) => {
        if (response) {
            const accessHeader = response.headers.get('access-token')
            const refreshHeader = response.headers.get('refresh-token')
            if (accessHeader && refreshHeader) {
                const accessToken = response.headers.get('access-token').split(' ')[1]
                const refreshToken = response.headers.get('refresh-token').split(' ')[1]
                // localStorage.setItem('access-token', accessToken)
                // localStorage.setItem('refresh-token', refreshToken)
                jwtState[1]({accessToken, refreshToken})
            }
        }
    }

    axiosInstance.interceptors.response.use((r) => {
        setHeadersIfExist(r)
        return r;
    }, (error) => {
        setHeadersIfExist(error.response)
        // Any status codes that falls outside the range of 2xx cause this function to trigger
        // Do something with response error
        const loginURL = '/login'
        const errorDetails = error.response
        if (errorDetails.apiCode && router.pathname !== loginURL) {
            if (errorDetails.apiCode === 606 || errorDetails.apiCode === 603) {
                router.push(loginURL)
            } else if (errorDetails.apiCode === 604) {
                if(jwt.accessToken) {
                    jwtState[1]({refreshToken: jwt.refreshToken})
                } else {
                    jwtState[1]({})
                    localStorage.setItem('fromPage', 'true')
                    router.push(loginURL)
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
            setLoggedUser({...loggedUser, nameOrEmail: loggedUser.username, hasImage: !!loggedUser.imageLink,
            image: loggedUser.imageLink, id: loggedUser.userId})

        } else {
            localStorage.removeItem('refreshToken')
        }
        if (!jwt.accessToken && !jwt.refreshToken) {
            setLoggedUser(undefined)
        }
        // alert(JSON.stringify(jwt))
    }, [jwt])


    let sharedState = {I18n, loggedUser, axios: axiosInstance, setErrorDetails, jwtState}


    return (
        <AppContext.Provider value={sharedState}>
            {children}
        </AppContext.Provider>
    );
}

export function useAppContext() {
    return useContext(AppContext);
}