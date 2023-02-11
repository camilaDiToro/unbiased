import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import CancelSearchLink from "../../components/CancelSearchLink";
import * as nextRouter from "next/router";

const {render, screen} = testingLibrary;
// nextRouter.useRouter = jest.fn();

const customPropsMap = (options = {}) => {
    const map = {
        text: "Cancel search filter: \"This is the search\""
    }

    return { ...map, ...options };
};

let propsMap

describe('Cancel search link test', ()=>{

    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('Shows link text and redirects to the correct path', ()=>{
        const query = {
            order: '',
            type: '',
        }
        render(<CancelSearchLink {...propsMap}/>, {query})
        screen.getByText(propsMap.text)

        expect(screen.getByText(propsMap.text)).toBeInTheDocument()
        expect(screen.getByText(propsMap.text).closest('a')).toHaveAttribute('href', '/?order=&type=')
    })
})

