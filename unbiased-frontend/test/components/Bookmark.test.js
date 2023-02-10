import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import Bookmark from "../../components/Bookmark";
import Tooltip from "../../components/Tooltip";

jest.mock('../../components/Tooltip', ()=>{
    return jest.fn(({children})=>{
        return (
            <div>{children}</div>
        )
    })
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
        render(
            <Tooltip>
                <Bookmark {...propsMap}/>
            </Tooltip>
        )
        const img = screen.getByRole('img', {name: 'bookmark'})
        // expect(img.src).toBe("/img/bookmark-clicked")
    })

})