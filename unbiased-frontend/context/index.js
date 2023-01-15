import {createContext, useContext, useEffect, useState} from 'react';
import I18n from "../i18n"
import axios from "axios";
import baseURL from "../pages/back";
import {useRouter} from "next/router";
const AppContext = createContext(null);

export default function AppWrapper({ children }) {
    const [errorDetails, setErrorDetails] = useState({})
    const router = useRouter()
    const axiosInstance = axios.create({
        baseURL: baseURL.href
    })
    useEffect(() => {
        const loginURL = '/login'
        console.log(errorDetails)

        if (errorDetails.apiCode && router.pathname !== loginURL) {
            router.push(loginURL)
        }
    }, [errorDetails])
    let sharedState = {I18n, loggedUser: {
        nameOrEmail: 'kevin',
            id: 13,
            hasImage: false,
            isAdmin: true
        }, axios: axiosInstance, setErrorDetails}


    return (
        <AppContext.Provider value={sharedState}>
            {children}
        </AppContext.Provider>
    );
}

export function useAppContext() {
    return useContext(AppContext);
}