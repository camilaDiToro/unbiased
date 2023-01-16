import {createContext, useContext, useEffect, useState} from 'react';
import I18n from "../i18n"
import axios from "axios";
import baseURL from "../pages/back";
import {useRouter} from "next/router";
const AppContext = createContext(null);

export default function AppWrapper({ children }) {
    const [errorDetails, setErrorDetails] = useState({})
    const jwtState = useState({})
    useEffect(()=> {
        jwtState[1]({        accessToken: localStorage.getItem('accessToken'), refreshToken: localStorage.getItem('refreshToken')
        })
    }, [])
    const router = useRouter()
    const axiosInstance = axios.create({
        baseURL: baseURL.href,
        transformRequest: [function (data, headers) {
            // Do whatever you want to transform the data
            const {accessToken, refreshToken} = jwt
            if (accessToken) {
                headers.Authorization = `Bearer ${accessToken}`
            } else if(refreshToken) {
                headers.Authorization = `Bearer ${refreshToken}`
            }
            return data;
        }]
    })

    const jwt = jwtState[0]

    useEffect(() => {
        if (jwt.accessToken) {
            localStorage.setItem('accessToken', jwt.accessToken)
        } else {
            localStorage.removeItem('accessToken')
        }
        if (jwt.refreshToken) {
            localStorage.setItem('refreshToken', jwt.refreshToken)
        } else {
            localStorage.removeItem('refreshToken')
        }
    }, [jwt])

    useEffect(() => {
        const loginURL = '/login'
        console.log(errorDetails)

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
    }, [errorDetails])
    let sharedState = {I18n, loggedUser: {
        nameOrEmail: 'kevin',
            id: 13,
            hasImage: false,
            isAdmin: true
        }, axios: axiosInstance, setErrorDetails, jwtState}


    return (
        <AppContext.Provider value={sharedState}>
            {children}
        </AppContext.Provider>
    );
}

export function useAppContext() {
    return useContext(AppContext);
}