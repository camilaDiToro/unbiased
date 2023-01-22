import {render, screen, waitFor} from '@testing-library/react';
import Login from '../pages/login/index';
import '@testing-library/jest-dom'
import AppWrapper from "../context";
import {createMockRouter} from "./test_utils/createMockRouter";
import {RouterContext} from "next/dist/shared/lib/router-context";
import userEvent from "@testing-library/user-event";

describe('Login test', ()=>{

    beforeEach(()=>{
        render(
            <RouterContext.Provider value={createMockRouter({})}>
                <Login/>
            </RouterContext.Provider>
        );
    })

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

    test('Confirm button working correctly', ()=>{

        const button = screen.getByRole('button', {
            name: /Log in/i,
        })
        expect(button).toBeInTheDocument()
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
});