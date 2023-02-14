import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import ModerationPanel from "../../components/ModerationPanel";

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
    const map = {
    }

    return { ...map, ...options };
};

describe('Moderation Panel test', ()=>{

    test('Show selected manage message and not selected the other options', ()=>{
        propsMap = customPropsMap({selected: 'reported-news'})
        render(<ModerationPanel {...propsMap}/>)
        expect(screen.getByTestId('reported-news-select')).toHaveClass('select')
        expect(screen.getByTestId('reported-comments-default')).toHaveClass('selected')
        expect(screen.queryByTestId('manage-admins-default')).toBeNull()
    })

    test('Show selected manage message and not selected the other options', ()=>{
        propsMap = customPropsMap({selected: 'reported-news'})
        render(<ModerationPanel {...propsMap}/>, {loggedUser: {authorities:["ROLE_OWNER"]}})
        expect(screen.getByTestId('reported-news-select')).toHaveClass('select')
        expect(screen.getByTestId('reported-comments-default')).toHaveClass('selected')
        expect(screen.queryByTestId('manage-admins-default')).toHaveClass('selected')
    })

    test('No one of the options selected', ()=>{
        propsMap = customPropsMap()
        render(<ModerationPanel {...propsMap}/>)
        expect(screen.getByTestId('reported-news-default')).toHaveClass('selected')
        expect(screen.getByTestId('reported-comments-default')).toHaveClass('selected')
        expect(screen.queryByTestId('manage-admins-default')).toBeNull()
    })

})