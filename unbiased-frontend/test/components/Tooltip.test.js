import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import Tooltip from "../../components/Tooltip";

const {render, screen, fireEvent} = testingLibrary;

let propsMap;

const customPropsMap = (options = {}) => {
    const map = {
        onClickHandler: jest.fn(),
        position: 'bottom',
        text: 'My tooltip',
    }

    return { ...map, ...options };
};

describe('Back button test', ()=>{

    propsMap = customPropsMap()
    test('Check if on click back button run handle', ()=>{
        render(<Tooltip {...propsMap}/>)
        const button = screen.getByTestId('tooltip')

        fireEvent.click(button)

        expect(propsMap.onClickHandler).toHaveBeenCalled()
    })
})