import {render, screen } from '@testing-library/react';
import Login from '../pages/login/index';
import '@testing-library/jest-dom'
import AppWrapper from "../context";
import {createMockRouter} from "./test_utils/createMockRouter";
import {RouterContext} from "next/dist/shared/lib/router-context";

describe('Login test', ()=>{

    beforeAll(()=>{
        render(
            <RouterContext.Provider value={createMockRouter({})}>
                <Login/>
            </RouterContext.Provider>
        );
    })

    test('aparece el boton de confirmar', ()=>{

        const button = screen.getByRole('button', {
            name: /Log in/i,
        })
        expect(button).toBeInTheDocument()
    })
});