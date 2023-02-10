import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import * as nextRouter from 'next/router';


import TimeSelector from "../../components/TimeSelector";
import PropTypes from "prop-types";
import TopCreator from "../../components/TopCreator";
import TopCreatorsPanel from "../../components/TopCreatorsPanel";
import {useTranslation} from "react-i18next";
import UpvoteButtons from "../../components/UpvoteButtons";
import {waitFor} from "@testing-library/react";

nextRouter.useRouter = jest.fn();

const {render, screen, fireEvent} = testingLibrary;


let propsMap;

const customPropsMap = (options = {}) => {
    const map = {
        rating: 1,
        id: 1,
        triggerEffect: jest.fn(),
        comment: false,
        upvotes: 10
    }

    return { ...map, ...options };
};

describe('UpvoteButtons test', ()=>{

    beforeEach(() => {
        propsMap = customPropsMap()
    })



    test('Test for absence of user parameters when not logged in', ()=>{


        render(<UpvoteButtons {...propsMap}> </UpvoteButtons>)


        expect(screen.getByTestId("upvote").getAttribute('src')).not.toContain('clicked')
        expect(screen.getByTestId("downvote").getAttribute('src')).not.toContain('clicked')
        expect(screen.getByTestId("rating").classList).not.toContain('upvoted')
        expect(screen.getByTestId("rating").classList).not.toContain('downvoted')


    })

    test('Test for user parameters when logged in', ()=>{


        render(<UpvoteButtons {...propsMap}> </UpvoteButtons>, {loggedUser: true})


        expect(screen.getByTestId("upvote").getAttribute('src')).toContain('clicked')
        expect(screen.getByTestId("downvote").getAttribute('src')).not.toContain('clicked')
        expect(screen.getByTestId("rating").classList).toContain('upvoted')
        expect(screen.getByTestId("rating").classList).not.toContain('downvoted')


    })

    test('Test for user upvote remove', async () => {

        propsMap.rating = 1
        propsMap.comment = false
        propsMap.triggerEffect = jest.fn()

        const api = {
            upvoteArticleRemove: jest.fn()
        }

        api.upvoteArticleRemove.mockImplementation(() => ({success: true}))

        render(<UpvoteButtons {...propsMap}> </UpvoteButtons>, {loggedUser: true, api})

        const upvoteBtn = screen.getByTestId("upvote")

        fireEvent.click(upvoteBtn)

        expect(api.upvoteArticleRemove).toBeCalled()
        await waitFor(() => expect(propsMap.triggerEffect).toBeCalled())

    })

    test('Test for user downvote remove', async () => {

        propsMap.rating = -1
        propsMap.comment = false
        propsMap.triggerEffect = jest.fn()

        const api = {
            downvoteArticleRemove: jest.fn()
        }

        api.downvoteArticleRemove.mockImplementation(() => ({success: true}))

        render(<UpvoteButtons {...propsMap}> </UpvoteButtons>, {loggedUser: true, api})

        const upvoteBtn = screen.getByTestId("downvote")

        fireEvent.click(upvoteBtn)

        expect(api.downvoteArticleRemove).toBeCalled()
        await waitFor(() => expect(propsMap.triggerEffect).toBeCalled())

    })

    test('Test for user upvote', async () => {

        propsMap.rating = 0
        propsMap.comment = false
        propsMap.triggerEffect = jest.fn()

        const api = {
            upvoteArticle: jest.fn()
        }

        api.upvoteArticle.mockImplementation(() => ({success: true}))

        render(<UpvoteButtons {...propsMap}> </UpvoteButtons>, {loggedUser: true, api})

        const upvoteBtn = screen.getByTestId("upvote")

        fireEvent.click(upvoteBtn)

        expect(api.upvoteArticle).toBeCalled()
        await waitFor(() => expect(propsMap.triggerEffect).toBeCalled())

    })

    test('Test for user downvote', async () => {

        propsMap.rating = 0
        propsMap.comment = false
        propsMap.triggerEffect = jest.fn()

        const api = {
            downvoteArticle: jest.fn()
        }

        api.downvoteArticle.mockImplementation(() => ({success: true}))

        render(<UpvoteButtons {...propsMap}> </UpvoteButtons>, {loggedUser: true, api})

        const upvoteBtn = screen.getByTestId("downvote")

        fireEvent.click(upvoteBtn)

        expect(api.downvoteArticle).toBeCalled()
        await waitFor(() => expect(propsMap.triggerEffect).toBeCalled())

    })

    test('Test for user upvote fail', async () => {

        propsMap.rating = 0
        propsMap.comment = false
        const api = {
            upvoteArticle: jest.fn()
        }

        api.upvoteArticle.mockImplementation(() => ({success: false}))

        render(<UpvoteButtons {...propsMap}> </UpvoteButtons>, {loggedUser: true, api})

        const upvoteBtn = screen.getByTestId("upvote")

        fireEvent.click(upvoteBtn)

        expect(api.upvoteArticle).toBeCalled()
        await waitFor(() => expect(propsMap.triggerEffect).not.toBeCalled())

    })

    test('Test for user upvote on comment', async () => {

        propsMap.rating = 0
        propsMap.comment = true
        propsMap.triggerEffect = jest.fn()

        const api = {
            upvoteComment: jest.fn()
        }

        api.upvoteComment.mockImplementation(() => ({success: true}))

        render(<UpvoteButtons {...propsMap}> </UpvoteButtons>, {loggedUser: true, api})

        const upvoteBtn = screen.getByTestId("upvote")

        fireEvent.click(upvoteBtn)

        expect(api.upvoteComment).toBeCalled()
        await waitFor(() => expect(propsMap.triggerEffect).toBeCalled())

    })

    test('Test for user downvote on comment', async () => {

        propsMap.rating = 0
        propsMap.comment = true
        propsMap.triggerEffect = jest.fn()

        const api = {
            downvoteComment: jest.fn()
        }

        api.downvoteComment.mockImplementation(() => ({success: true}))

        render(<UpvoteButtons {...propsMap}> </UpvoteButtons>, {loggedUser: true, api})

        const upvoteBtn = screen.getByTestId("downvote")

        fireEvent.click(upvoteBtn)

        expect(api.downvoteComment).toBeCalled()
        await waitFor(() => expect(propsMap.triggerEffect).toBeCalled())

    })

    test('Test for user downvote when not logged in', async () => {

        propsMap.rating = 0
        propsMap.comment = true
        propsMap.triggerEffect = jest.fn()

        const push = jest.fn()
        nextRouter.useRouter.mockImplementation(() => ({ route: '/' , query: {}, push}));
        const api = {
            downvoteArticle: jest.fn()
        }

        render(<UpvoteButtons {...propsMap}> </UpvoteButtons>, {loggedUser: false, api})

        const upvoteBtn = screen.getByTestId("downvote")

        fireEvent.click(upvoteBtn)

        expect(api.downvoteArticle).not.toBeCalled()
        await waitFor(() => expect(propsMap.triggerEffect).not.toBeCalled())
        await waitFor(() => expect(push).toBeCalled())

    })


})