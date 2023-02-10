import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import Tooltip from "../../components/Tooltip";
import PropTypes from "prop-types";
import ReportFlag from "../../components/ReportFlag";
import {getResourcePath} from "../../constants";

const {render, screen, fireEvent} = testingLibrary;


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
    test('Test for correct image shown when reported', ()=>{
        propsMap.reported = true
        render(<ReportFlag {...propsMap}> </ReportFlag>)
        const img = screen.getByTestId('flag-img')
        expect(img.getAttribute('src')).toEqual(getResourcePath(`/img/flag-clicked.svg`))
    })

    test('Test for correct image shown when reported', ()=>{
        propsMap.reported = false
        render(<ReportFlag {...propsMap}> </ReportFlag>)
        const img = screen.getByTestId('flag-img')
        expect(img.getAttribute('src')).toEqual(getResourcePath(`/img/flag.svg`))
    })
})