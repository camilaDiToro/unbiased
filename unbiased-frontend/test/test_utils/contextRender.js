import React from 'react'
import {render} from '@testing-library/react'
import {RouterContext} from "next/dist/shared/lib/router-context";
import {createMockRouter} from "./createMockRouter";
import {AppContext} from "../../context";
import { useI18n} from "../../customI18n";
import '@testing-library/jest-dom'
import {withI18n} from "../../i18n";

const AllTheProviders = ({children, options= {loggedUser: null, query: {}, pathname: '/'}})=>{
    const I18n = useI18n()
    const {query={}, pathname='/', ...restOptions} = options

    return(
            <RouterContext.Provider value={createMockRouter({query, pathname})}>
                <AppContext.Provider value={{I18n, ...restOptions}}>
                    {children}
                </AppContext.Provider>
            </RouterContext.Provider>
    )
}

const FinalWrapper = withI18n(AllTheProviders)

const contextRender = (ui, options) =>
    render(ui, {wrapper: (props) => <FinalWrapper {...props} options={options} />, ...options})

export * from '@testing-library/react'
export {contextRender as render}
