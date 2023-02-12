import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import Modal from "../../components/Modal"

const {render, screen, fireEvent} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
    const map = {
        body: "Modal body",
        id: "binModal2",
        onClickHandler: jest.fn(),
        title: "Modal title"

    }

    return { ...map, ...options };
};

describe('Modal test', ()=>{

    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('Shows all visible props correctly', ()=>{
        propsMap = customPropsMap({acceptText: "Delete comment"})
        render(<Modal {...propsMap}/>)
        expect(screen.getByText(propsMap.title)).toBeInTheDocument()
        expect(screen.getByText(propsMap.body)).toBeInTheDocument()
        expect(screen.getByText(propsMap.acceptText)).toBeInTheDocument()
        expect(screen.queryByText(/confirm/i)).toBeNull()
    })

    test('Shows confirm message with no acceptText passed by prop', ()=>{
        render(<Modal {...propsMap}/>)
        expect(screen.getByText(/confirm/i)).toBeInTheDocument()
    })

    test('Handler should run after clicking confirm button', ()=>{
        render(<Modal {...propsMap}/>)
        const button = screen.getByRole('button', {name: /confirm/i})
        fireEvent.click(button)
        expect(propsMap.onClickHandler).toHaveBeenCalled()
    })
})