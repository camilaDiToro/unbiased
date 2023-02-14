import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import ReportReason from "../../components/ReportReason";

jest.mock('../../components/FormattedDate', ()=>{
    return jest.fn(()=> "Wednesday, October 19, 2022")
})

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
    const map = {
        articleId: 1,
        datetime: "2022-11-01T21:52:00.133",
        id: 1,
        user: "Aleca",
        reason: "report.inappropiate",

    }

    return { ...map, ...options };
};

describe('Report reason test', ()=>{

    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('All the readable props appear on the report reason', ()=>{
        render(
            <table>
                <tbody>
                    <ReportReason {...propsMap}/>
                </tbody>
            </table>
        )
        expect(screen.getByText(propsMap.user)).toBeInTheDocument()
        expect(screen.getByText('The content is inappropriate.')).toBeInTheDocument()
    })
})