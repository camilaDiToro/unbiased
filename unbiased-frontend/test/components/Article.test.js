import React from 'react'
import Article from "../../components/Article";
import * as testingLibrary from "../test_utils/contextRender";

const {render, screen} = testingLibrary;

const defaultMap = (options = {}) => {
    const map = {
        title: "My title",
        subtitle: "My subtitle",
        body: "my body",
        datetime: "3 months ago",
        readTime: 0,
        creator: {
            hasImage: true,
            Image: "http://localhost:8080/webapp_war_exploded/api/users/1/image",
            id: 1,
            nameOrEmail: "My name",
            tier: "default",
        },
        hasImage: false,
        stats: {
            upvoted: 1,
            interactions: 1,
            positivity: 'positive'
        },
        upvotes: 1,
        triggerEffect: jest.fn(),
        id: 1

    }

    return { ...map, ...options };
};

jest.mock('../../components/FormattedDate', ()=>{
    return jest.fn(() => {
        return "3 months ago";
    });
})


describe('Article test', ()=>{

    beforeEach(()=>{
        render(null) //This render our context, it's a modified render of react-testing-library
    })


    test('Renders the component with the default props', () => {
        const map = defaultMap()

        render(
            <Article {...map}/>) //This wraps the Article component into our rendered context without rendering the context again

        //Here we check if all the readable props appear on the card
        Object.entries(map).forEach(([key,value])=>{
            if(typeof value === 'string' && (key === 'title' || key === 'subtitle' || key === 'datetime')){
                expect(screen.getByText(value)).toBeInTheDocument()

            }else if (typeof  value === 'number' && (key === 'readTime' || key === 'upvotes')){
                if(key === 'readTime'){
                    expect(screen.getByText(`${value} min read`)).toBeInTheDocument()
                }else{
                    expect(screen.getByText(value)).toBeInTheDocument()
                }

            }else if (typeof  value === 'object' && (key === value.nameOrEmail)) {
                expect(screen.getByText(value.nameOrEmail)).toBeInTheDocument()
            }
        })

    })
})
