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

    test('Correct src is loaded giving the prop saved with true', ()=>{

        propsMap = customPropsMap()
        const loggedUser = getDefaultLoggedUser()
        render(<Bookmark {...propsMap}/>, {loggedUser})
        expect(screen.getByRole('tooltip', {name: 'Mocked tooltip'})).toBeInTheDocument()
    })

})