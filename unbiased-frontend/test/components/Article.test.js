import React from 'react'
import Article from "../../components/Article";
import * as testingLibrary from "../test_utils/contextRender";

const {render, screen} = testingLibrary;

const customPropsMap = (options = {}) => {
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

//Mocks of the internal components used in Article
jest.mock('../../components/FormattedDate', ()=>{
    return jest.fn(() => {
        return "3 months ago";
    });
})

jest.mock('../../components/DeleteButton', ()=>{
    return jest.fn(()=> <div role="button">Mocked delete button</div>)
})

jest.mock('../../components/PinButton', ()=>{
    return jest.fn(()=> <div role="button">Mocked pin button</div>)
})

jest.mock('../../components/CommentButton', ()=>{
    return jest.fn(()=> <div role="button">Mocked comment button</div>)
})

let propsMap

describe('Article test', ()=>{

    beforeEach(()=>{
        propsMap = customPropsMap() //get the default props amp
    })


    test('Check if all the readable props appear on the card', () => {

        render(<Article {...propsMap}/>) //This wraps the Article component into our rendered context (it's a custom render of RTL)

        Object.entries(propsMap).forEach(([key,value])=>{
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

    test('Show card image when there is one ', ()=> {
        propsMap = customPropsMap({hasImage: true, image: "http://localhost:8080/webapp_war_exploded/api/news/4/image"})
        render(<Article {...propsMap}/>)
        expect(screen.getByRole('img', {name: 'cardImage'})).toBeInTheDocument()
    })

    test('Does not show card image when there is not one', ()=> {
        render(<Article {...propsMap}/>) //The default propsMap has the hasImage prop in false
        expect(screen.queryByLabelText('cardImage')).toBeNull()
    })

    test('Show delete button, pin button and it doesn\'t show comment button if is a profile article', ()=>{
        propsMap = customPropsMap({profileArticle: true})
        render(<Article {...propsMap}/>)
        expect(screen.getByRole('button', {name: 'Mocked delete button'})).toBeInTheDocument()
        expect(screen.getByRole('button', {name: 'Mocked pin button'})).toBeInTheDocument()
        expect(screen.queryByRole('button', {name: 'Mocked comment button'})).toBeNull()
    })

    test('Show comment button and it doesn\'t show delete button and pin button if is not a profile article', ()=>{
        propsMap = customPropsMap()
        render(<Article {...propsMap}/>)
        expect(screen.queryByRole('button', {name: 'Mocked delete button'})).toBeNull()
        expect(screen.queryByRole('button', {name: 'Mocked pin button'})).toBeNull()
        expect(screen.getByRole('button', {name: 'Mocked comment button'})).toBeInTheDocument()
    })
})
