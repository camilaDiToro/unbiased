import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import DeleteButton from "../../components/DeleteButton";

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
    const map = {
        creatorId: 1,
        id: 1
    }

    return { ...map, ...options };
};

describe('Delete button test', ()=>{

    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('Shows the corrects messages when is a delete article button', ()=>{
        render(<DeleteButton {...propsMap}/>)
        expect(screen.getByText('Delete article')).toBeInTheDocument()
        expect(screen.queryByText('Delete comment')).toBeNull()
        expect(screen.getByText('Delete news')).toBeInTheDocument()
        expect(screen.queryByText('Delete comment')).toBeNull()
    })

    test('Shows the corrects messages when is a delete comment button', ()=>{
        propsMap = customPropsMap({comment: true})
        render(<DeleteButton {...propsMap}/>)
        expect(screen.queryByText('Delete article')).toBeNull()
        expect(screen.getByText('Delete comment')).toBeInTheDocument()
        expect(screen.queryByText('Delete news')).toBeNull()
        expect(screen.getByText('Delete comment')).toBeInTheDocument()
    })

})