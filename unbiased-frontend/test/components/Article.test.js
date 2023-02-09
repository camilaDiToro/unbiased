import React from 'react'
import Article from "../../components/Article";
import {render} from "../test_utils/contextRender";
import {screen} from '@testing-library/react'

jest.mock('../../components/FormattedDate', ()=>{
    return jest.fn(() => {
        return "3 months ago";
    });
})

describe('Article test', ()=>{

    beforeEach(()=>{
        render(null)
    })

    test('Render the readable props correctly', async () => {

        const map = new Map([
            ["title", "my Title"],
            ["subtitle", "My description"],
            // ["body", "My body"],
            ["dateTime", "3 months ago"],
            ["readTime", 0],
            ["creator", {
                nameOrEmail: "My name",
            }],
            ["hasImage", false],
            ["stats", {
                upvoted: 1,
                interactions: 1,
                positivity: 'positive'
            }],
            ['upvotes', 1],
            // ['triggerEffect', jest.fn()],

        ])

        render(
            <Article
                title={map.get('title')}
                subtitle={map.get('subtitle')}
                dateTime={map.get('dateTime')}
                readTime={map.get('readTime')}
                creator={map.get('creator')}
                body={map.get('body')}
                hasImage={map.get('hasImage')}
                stats={map.get('stats')}
                upvotes={map.get('upvotes')}
            />) //This wraps the Article component into the ContextRender without rendering the context again

        map.forEach((value, key) => {
            if(typeof value === 'string' && (key === 'title' || key === 'subtitle' || key === 'dateTime')){
                expect(screen.getByText(value)).toBeInTheDocument()
            }else if (typeof  value === 'number' && (key === 'dateTime' || key === 'readTime' || key === 'upvotes')){
                if(key === 'readTime'){
                    expect(screen.getByText(`${value} min read`)).toBeInTheDocument()
                }
            }else if (typeof  value === 'object' && (key === value.nameOrEmail)) {
                expect(screen.getByText(value.nameOrEmail)).toBeInTheDocument()
            }
        })

    })
})
