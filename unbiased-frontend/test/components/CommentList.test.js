import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import {getDefaultLoggedUser} from "../test_utils/defaultLoggedUser";
import CommentList from "../../components/CommentList";

const {render, screen} = testingLibrary;

jest.mock('../../components/Comment', ()=>{
    return jest.fn((props)=> <div data-testid={`comment-${props.id}`}>Mocked comment</div>)
})

jest.mock('../../components/Pagination', ()=>{
    return jest.fn(()=> <div title="pagination">Mocked Pagination</div>)
})

jest.mock('../../components/TopNewTabs', ()=>{
    return jest.fn(()=> <div title="topNewsTabs">Mocked topNew Tabs</div>)
})


let propsMap

const customPropsMap = (options = {}) => {
    const map = {
        comments: [
            { id: 1, text: 'Comment 1' },
            { id: 2, text: 'Comment 2' },
            { id: 3, text: 'Comment 3' },
        ]
    }

    return { ...map, ...options };
};

describe('Comment list test', ()=>{

    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('Shows the comment block when the user is logged', ()=>{
        const loggedUser = getDefaultLoggedUser()
        render(<CommentList {...propsMap}/>, {loggedUser})
        expect(screen.getByTitle('comment-block')).toBeInTheDocument()
    })

    test('Shows the list of comments and pagination', ()=>{
        render(<CommentList {...propsMap}/>)
        expect(screen.getAllByTestId(/comment-\d+/)).toHaveLength(3)
        expect(screen.getByTitle('pagination')).toBeInTheDocument()
        expect(screen.queryByText("Nobody has commened yet.")).toBeNull()
    })

    test('If there is no comments shows the appropriate message and dont show pagination', ()=>{
        propsMap = customPropsMap({comments: []})
        render(<CommentList {...propsMap}/>)
        expect(screen.queryAllByTestId(/comment-\d+/)).toHaveLength(0)
        expect(screen.getByText("Nobody has commened yet.")).toBeInTheDocument()
        expect(screen.queryByTitle('pagination')).toBeNull()
    })

})