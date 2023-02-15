import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import FollowButton from "../../components/FollowButton";

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
    const map = {
        userId: 1,
        following: true,
        triggerEffect: jest.fn()
    }

    return { ...map, ...options };
};

describe('Follow button test', ()=>{

    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('Shows follow button if the user do not follow the profile user', ()=>{
        propsMap = customPropsMap({following: false})
        render(<FollowButton {...propsMap}/>)
        expect(screen.getByText('Follow')).toBeInTheDocument()
        expect(screen.queryByAltText('following')).toBeNull()
    })

    test('Shows unfollow button if the user is following the profile user', ()=>{
        render(<FollowButton {...propsMap}/>)
        expect(screen.queryByText('Follow')).toBeNull()
        expect(screen.getByAltText('following')).toBeInTheDocument()
    })

})