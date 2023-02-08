import React, {useState} from 'react';
import {render, screen, waitFor} from '@testing-library/react';
import {AppContext} from "../../context";
import Login from "../../pages/login";
import {I18nTesting} from "../../i18n";
import {RouterContext} from "next/dist/shared/lib/router-context";
import {createMockRouter} from "../test_utils/createMockRouter";
import '@testing-library/jest-dom'
import userEvent from "@testing-library/user-event";

// jest.mock('../i18n', () => ({
//     I18n: jest.fn(()=>{return 'Hello world'}),
// }));

describe('Login test', ()=>{

    beforeEach(()=>{


        const axios = (() => {
            return new Promise((resolve, reject) => {
                reject({response: {status: 404}})
            });
        })

        render(
            <RouterContext.Provider value={createMockRouter({})}>
                <AppContext.Provider value={{I18n: I18nTesting, axios}}>
                    <Login/>
                </AppContext.Provider>
            </RouterContext.Provider>
        );
    })

    test('MyComponent should display the correct value from i18n context', () => {

        const button = screen.getByRole('button', {
            name: /Log in/i,
        })
        expect(button).toBeInTheDocument()
    });

    test('inputs working correctly', async () => {
        const usernameInput = screen.getByRole('textbox', {
            name: "username",
        })
        const passwordInput = screen.getByTestId("password")

        expect(usernameInput).toBeInTheDocument()
        expect(passwordInput).toBeInTheDocument()


        await waitFor(() => {
            userEvent.clear(usernameInput)
            userEvent.clear(passwordInput)
        })

        await userEvent.type(usernameInput, "my username")
        await userEvent.type(passwordInput, "my password")

        await waitFor(() => {
            expect(usernameInput).toHaveValue("my username")
            expect(passwordInput).toHaveValue("my password")
        })

    })

    test('Eye button working correctly', async () => {
        const eyeIcon = screen.getByAltText("eye")
        expect(eyeIcon).toBeInTheDocument()

        const eyeButton = screen.getByRole('button', {
            name: "eye",
        })

        await userEvent.click(eyeButton)

        const eyeSlashIcon = screen.getByAltText("eyeSlash")
        expect(eyeSlashIcon).toBeInTheDocument()
    })
})
