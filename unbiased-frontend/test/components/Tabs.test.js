import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import Tabs from "../../components/Tabs"
import {getResourcePath} from "../../constants";

const {render, screen, fireEvent} = testingLibrary;


let propsMap;

const customPropsMap = (options = {}) => {
    const map = {
        items: [{
            text: 'a',
            params: {'a':'a'}
        }, {
            text: 'b',
            params: {'b':'b'}
        }],
        selected: 'b'
    }

    return { ...map, ...options };
};

describe('Tabs test', ()=>{


    propsMap = customPropsMap()

    test('Test for correct links construction', ()=>{


        render(<Tabs {...propsMap}> </Tabs>)
        const a = screen.getByText('a')
        const b = screen.getByText('b')

        expect(a).toBeVisible()
        expect(screen.getByText('a').closest('a')).toHaveAttribute('href', '/?a=a')

        expect(b).toBeVisible()
        expect(screen.getByText('b').closest('a')).toHaveAttribute('href', '/?b=b')

    })

    test('Test for correct tab selection', ()=>{

        propsMap.selected = 'a'
        render(<Tabs {...propsMap}> </Tabs>)
        const a = screen.getByText('a')

        expect(a).toBeVisible()
        expect(a.classList).toContain('active')
    })
})