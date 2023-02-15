import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import * as nextRouter from 'next/router';

nextRouter.useRouter = jest.fn();

import TimeSelector from "../../components/TimeSelector";



const {render, screen, fireEvent} = testingLibrary;


let propsMap;

const customPropsMap = (options = {}) => {
    const map = {
    }

    return { ...map, ...options };
};

describe('TimeSelector test', ()=>{


    propsMap = customPropsMap()

    test('Test for correct links construction', ()=>{
        nextRouter.useRouter.mockImplementation(() => ({ route: '/' , query: {}}));


        render(<TimeSelector {...propsMap}> </TimeSelector>)


        expect(screen.getByTestId('selected-time')).toHaveTextContent('Last week')

    })

    test('Test for correct time selection', ()=>{

        nextRouter.useRouter.mockImplementation(() => ({ route: '/' , query: {time: 'HOUR'}}));

        render(<TimeSelector {...propsMap}> </TimeSelector>)


        expect(screen.getByTestId('selected-time')).toHaveTextContent('Last hour')

    })
})