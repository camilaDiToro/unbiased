import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import Bookmark from "../../components/Bookmark";
import {getDefaultLoggedUser} from "../test_utils/defaultLoggedUser";

jest.mock('../../components/Tooltip', ()=>{
    return jest.fn(()=> <div role="tooltip">Mocked tooltip</div>)
})

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
    const map = {
        id: 1,
        saved: true,
        triggerEffect: jest.fn()
    }

    return { ...map, ...options };
};

describe('Bookmark test', ()=>{

    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('Bookmark show up if it is a logged user', ()=>{

        const loggedUser = getDefaultLoggedUser()
        render(<Bookmark {...propsMap}/>, {loggedUser})
        expect(screen.getByRole('tooltip', {name: 'Mocked tooltip'})).toBeInTheDocument()
    })

    test('Bookmark redirects to log in if it is not a logged user', ()=> {

        render(<Bookmark {...propsMap}/>)
        expect(screen.getByRole('tooltip', {name: 'Mocked tooltip'}).closest('a')).toHaveAttribute('href', '/login')
    })

})