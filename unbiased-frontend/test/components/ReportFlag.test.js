import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import ReportFlag from "../../components/ReportFlag";
import {getResourcePath} from "../../constants";

const {render, screen} = testingLibrary;


let propsMap;

const customPropsMap = (options = {}) => {
    const map = {
        triggerEffect: jest.fn(),
        comment: false,
        id: 5,
        reported: false
    }

    return { ...map, ...options };
};

describe('ReportFlag test', ()=>{

    propsMap = customPropsMap()
    test('Test for correct image shown when reported and it is a logged user', ()=>{
        const loggedUser = customPropsMap()
        propsMap.reported = true
        render(<ReportFlag {...propsMap}/>, {loggedUser})
        const img = screen.getByTestId('flag-img')
        expect(img.getAttribute('src')).toEqual(getResourcePath(`/img/flag-clicked.svg`))
    })

    test('Test for correct image shown when reported and it is a logged user', ()=>{
        const loggedUser = customPropsMap()
        propsMap.reported = false
        render(<ReportFlag {...propsMap}/>, {loggedUser})
        const img = screen.getByTestId('flag-img')
        expect(img.getAttribute('src')).toEqual(getResourcePath(`/img/flag.svg`))
    })

    test('Report flag redirects to log in if it is not a logged user', ()=>{
        render(<ReportFlag {...propsMap}/>)
        expect(screen.getByTestId('flag-img').closest('a')).toHaveAttribute('href', '/login')
    })
})