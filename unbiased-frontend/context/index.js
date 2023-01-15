import { createContext, useContext } from 'react';
import I18n from "../i18n"
import axios from "axios";
import baseURL from "../pages/back";
const AppContext = createContext(null);

export default function AppWrapper({ children }) {
    let sharedState = {I18n, loggedUser: {
        nameOrEmail: 'kevin',
            id: 13,
            hasImage: false,
            isAdmin: true
        }, axios: axios.create({
            baseURL: baseURL.href
        })}


    return (
        <AppContext.Provider value={sharedState}>
            {children}
        </AppContext.Provider>
    );
}

export function useAppContext() {
    return useContext(AppContext);
}