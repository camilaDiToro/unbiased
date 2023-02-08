import React from 'react'
import {render} from '@testing-library/react'
import {RouterContext} from "next/dist/shared/lib/router-context";
import {createMockRouter} from "./createMockRouter";
import {AppContext} from "../../context";
import {I18nTesting} from "../../i18n";

const AllTheProviders = ({children})=>{
    render(
        <RouterContext.Provider value={createMockRouter({})}>
            <AppContext.Provider value={{I18n: I18nTesting}}>
                {children}
            </AppContext.Provider>
        </RouterContext.Provider>
    )
}

export const ContextRender = (ui, options) =>
    render(ui, {wrapper: AllTheProviders, ...options})

export * from '@testing-library/react'
