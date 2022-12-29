import { createContext, useContext } from 'react';
import I18n from "../i18n"
const AppContext = createContext(null);

export default function AppWrapper({ children }) {
    let sharedState = {I18n}

    return (
        <AppContext.Provider value={sharedState}>
            {children}
        </AppContext.Provider>
    );
}

export function useAppContext() {
    return useContext(AppContext);
}