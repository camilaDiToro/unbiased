import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import EditProfileForm from "../../components/EditProfileForm";

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
    const map = {
        handlerArray: jest.fn(),
        triggerEffect: jest.fn()
    }

    return { ...map, ...options };
};

describe('EditProfile test', ()=>{

    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('Shows description if the user is journalist', ()=>{
        propsMap = customPropsMap({isJournalist: true})
        render(<EditProfileForm {...propsMap}/>)
        expect(screen.getByTitle('description')).toBeInTheDocument()
    })

    test('Do not show description if the user is not a journalist', ()=>{
        propsMap = customPropsMap({isJournalist: false})
        render(<EditProfileForm {...propsMap}/>)
        expect(screen.queryByTitle('description')).toBeNull()
    })

})