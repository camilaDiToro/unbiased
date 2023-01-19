import {render, screen } from '@testing-library/react';
import Login from '../pages/login/index';
import '@testing-library/jest-dom'

describe('Login test', ()=>{
    test('aparece el boton de confirmar', ()=>{
        render(<Login/>);
        const button = screen.getByRole('button', {
            name: /Log in/i,
        })
        expect(button).toBeInTheDocument()
    })
});