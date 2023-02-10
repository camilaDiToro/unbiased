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

    test('Bookmark show up if it is a loggedUser', ()=>{

        propsMap = customPropsMap()
        const loggedUser = getDefaultLoggedUser()
        render(<Bookmark {...propsMap}/>, {loggedUser})
        expect(screen.getByRole('tooltip', {name: 'Mocked tooltip'})).toBeInTheDocument()
    })

    test('Bookmark does not show up if it is not a loggedUser', ()=>{

        propsMap = customPropsMap()
        render(<Bookmark {...propsMap}/>)
        expect(screen.queryByRole('tooltip', {name: 'Mocked tooltip'})).toBeNull()
    })

})