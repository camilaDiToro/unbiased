import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import Comment from "../../components/Comment";
import i18n from "../../i18n/i18n";
import {getDefaultLoggedUser} from "../test_utils/defaultLoggedUser";

const {render, screen} = testingLibrary;

const customPropsMap = (options = {}) => {
    const map = {
        creator: {
            hasImage: true,
            Image: "http://localhost:8080/webapp_war_exploded/api/users/1/image",
            id: 1,
            nameOrEmail: "My name",
            tier: "default",
        },
        datetime: "2022-10-19T11:24:59.723",
        deleted: true,
        newsId: 1,
        rating: 0,
        reportCount: 0,
        reported: false,
        triggerEffect: jest.fn(),
        id: 1
    }

    return { ...map, ...options };
};

let propsMap

//Mocks of the internal components used in Article
jest.mock('../../components/FormattedDate', ()=>{
    return jest.fn(()=> <div data-testid="formattedDate">Wednesday, October 19, 2022</div>)
})

jest.mock('../../components/DeleteButton', ()=>{
    return jest.fn(()=> <div role="button">Mocked delete button</div>)
})

jest.mock('../../components/UpvoteButtons', ()=>{
    return jest.fn(()=> <div role="button">Mocked upvote button</div>)
})

jest.mock('../../components/ReportFlag', ()=>{
    return jest.fn(()=> <div role="button">Mocked report flag button</div>)
})

describe('Comment test', () => {

    beforeEach(()=>{
        propsMap = customPropsMap()
        i18n.changeLanguage('en')
    })

    test('Check if all the default visible props appear on the comment', ()=>{
        render(<Comment {...propsMap}/>)
        expect(screen.getByText(propsMap.creator.nameOrEmail)).toBeInTheDocument()
        expect(screen.getByRole('img', {name: 'profileImage'})).toBeInTheDocument()
        expect(screen.getByTestId('formattedDate').textContent).toContain("Wednesday, October 19, 2022")

    })

    test('Show al visible components when comment is not deleted', ()=>{
        propsMap = customPropsMap({body: "My body", deleted: false})
        render(<Comment {...propsMap}/>)
        expect(screen.getByText(propsMap.body)).toBeInTheDocument()
        expect(screen.getByRole('button', {name: "Mocked delete button"})).toBeInTheDocument()
        expect(screen.getByRole('button', {name: "Mocked upvote button"})).toBeInTheDocument()
        expect(screen.getByRole('button', {name: "Mocked report flag button"})).toBeInTheDocument()
        expect(screen.queryByText("The comment has been deleted.")).toBeNull()
    })

    test('Show delete message when comment is deleted', ()=>{
        propsMap = customPropsMap()//By default this is a deleted comment
        render(<Comment {...propsMap}/>)

        expect(screen.getByText("The comment has been deleted.")).toBeInTheDocument()
        expect(screen.queryByRole('button', {name: "Mocked delete button"})).toBeNull()
        expect(screen.queryByRole('button', {name: "Mocked upvote button"})).toBeNull()
        expect(screen.queryByRole('button', {name: "Mocked report flag button"})).toBeNull()
    })
})



