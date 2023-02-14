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
        //Note that in all renders we're going to need mock the pathname to prevent error with a '//'expression
        render(<ReportedCard {...propsMap}/>, {pathname: '/mocked/pathname'})
        expect(screen.getByText(propsMap.reportCount)).toBeInTheDocument()
        expect(screen.getByText(propsMap.title)).toBeInTheDocument()
        expect(screen.getByText('3 months ago')).toBeInTheDocument()
        expect(screen.getByText('View details')).toBeInTheDocument()
        expect(screen.getByRole('button', {name: 'Mocked delete button'})).toBeInTheDocument()
        expect(screen.getByRole('button', {name: 'Mocked profile pic'})).toBeInTheDocument()
    })

    test('Redirects to the correct article with comment card', ()=>{
        propsMap = customPropsMap({comment: true, newsId: 5})
        render(<ReportedCard {...propsMap}/>, {pathname: '/mocked/pathname'})
        expect(screen.getByText(`"${propsMap.title}"`).closest('a')).toHaveAttribute('href', `/article?id=${propsMap.newsId}&comment=${propsMap.id}`)
    })

    test('Redirects to the correct article with article card', ()=>{
        propsMap = customPropsMap()
        render(<ReportedCard {...propsMap}/>, {pathname: '/mocked/pathname'})
        expect(screen.getByText(propsMap.title).closest('a')).toHaveAttribute('href', `/article?id=${propsMap.id}`)
    })

    test('Show subtitle if it is an article card', ()=>{
        propsMap = customPropsMap()
        render(<ReportedCard {...propsMap}/>, {pathname: '/mocked/pathname'})
        expect(screen.getByText(propsMap.subtitle)).toBeInTheDocument()
    })

    test('Redirects to the correct path with view details link', ()=>{
        propsMap = customPropsMap()
        render(<ReportedCard {...propsMap}/>, {pathname: '/mocked/pathname'})
        expect(screen.getByText('View details').closest('a')).toHaveAttribute('href', `/mocked/pathname/${propsMap.id}`)
    })
})