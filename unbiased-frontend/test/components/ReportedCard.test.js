import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import ReportedCard from "../../components/ReportedCard";

const {render, screen} = testingLibrary;

jest.mock('../../components/FormattedDate', ()=>{
    return jest.fn(()=> "3 months ago")
})

jest.mock('../../components/DeleteButton', ()=>{
    return jest.fn(()=> <div role="button">Mocked delete button</div>)
})

jest.mock('../../components/ProfilePic', ()=>{
    return jest.fn(()=> <div role="button">Mocked profile pic</div>)
})

let propsMap

const customPropsMap = (options = {}) => {
    const map = {
        title: "My title",
        subtitle: "My subtitle",
        body: "My body",
        creator: {
            id: 1,
            nameOrEmail: "My name",
            tier: "default",
        },
        datetime: "2022-10-19T11:24:59.723",
        hasImage: false,
        id: 1,
        pinned: false,
        rating: 1,
        readTime: 0,
        reportCount: 1,
        reported: true,
        saved: false,
        upvotes: 1,
        triggerEffect: jest.fn(),

    }

    return { ...map, ...options };
};

describe('Report form test', ()=>{

    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('All the readable props appear on the card', ()=>{
        render(<ReportedCard {...propsMap}/>, {pathname: '/mocked/pathname'})
        expect(screen.getByText(propsMap.reportCount)).toBeInTheDocument()
        screen.debug()
    })

})