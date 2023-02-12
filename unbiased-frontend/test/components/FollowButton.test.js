import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import FollowButton from "../../components/FollowButton";

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
    const map = {
        userId: 1,
        following: true
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
        expect(screen.getByRole('link', {name: /follow/i})).toBeInTheDocument()
        // todo: expect(screen.queryByRole('link', {name: /unfollow/i})).toBeNull() cuando se agregue el i18n de unfollow
    })

    test('Shows unfollow button if the user is following the profile user', ()=>{
        render(<FollowButton {...propsMap}/>)
        expect(screen.queryByRole('link', {name: /follow/i})).toBeNull()
        // todo: expect(screen.getByRole('link', {name: /unfollow/i})).toBeInTheDocument() cuando se agregue el i18n de unfollow
    })

})