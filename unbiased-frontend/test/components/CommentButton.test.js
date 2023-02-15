import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import CommentButton from "../../components/CommentButton";

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
    const map = {
        id: 0,
    }

    return { ...map, ...options };
};

describe('Comment button test', ()=>{

    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('Redirects to the correct path', ()=>{
        const query = {
            order: '',
            type: '',
        }
        render(<CommentButton {...propsMap}/>, {query})
        expect(screen.getByTitle('comment-button-link').closest('a')).toHaveAttribute('href', `/article?id=${propsMap.id}&comment=true`)
    })

})