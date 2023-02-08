import React from 'react'
import '@testing-library/jest-dom'
import {cleanup, getByText, render, screen, waitFor} from '@testing-library/react';
import Article from "../../components/Article";
import {ContextRender} from "../test_utils/contextRender";
import i18n from "../../i18n";

jest.mock('../../components/FormattedDate', ()=>{
    return jest.fn(() => {
        return "2022-11-14T20:40:33.588";
    });
})

describe('Article test', ()=>{

    beforeEach(()=>{
        ContextRender()
    })

    test('Article component should show correct title, description, date and TTR', async () => {
        const title = "My title"
        const subtitle = "My description"
        const dateTime = "2022-11-14T20:40:33.588"
        const readTime = 0

        ContextRender(<Article title={title} subtitle={subtitle} dateTime={dateTime} readTime={readTime}/>) //This wraps the Article component into the ContextRender without rendering the context again

        expect(screen.getByText(title)).toBeInTheDocument()
        expect(screen.getByText(subtitle)).toBeInTheDocument()
        expect(screen.getByText(dateTime)).toBeInTheDocument()
        expect(screen.getByText(`${readTime} min read`)).toBeInTheDocument()
    })
})
