import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import ProfileLink from "../../components/ProfileLink";

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
    const map = {
        id: 1,
        nameOrEmail: 'user@example.com'
    }

    return { ...map, ...options };
};

describe('Profile link test', ()=>{

    test('Show name or email', ()=>{
        propsMap = customPropsMap()
        render(<ProfileLink {...propsMap}/>)
        expect(screen.getByText(propsMap.nameOrEmail)).toBeInTheDocument()
    })

})