import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import Pagination from "../../components/Pagination";
import i18n from "../../i18n/i18n";

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
    const map = {
        currentPage: 1,
        lastPage: 2,
    }

    return { ...map, ...options };
};

describe('Pagination test', ()=>{

    beforeEach(()=>{
        propsMap = customPropsMap()
        i18n.changeLanguage('en')
    })

    test('All pagination pages are well built', ()=>{
        const pages = [1,2]
        render(<Pagination {...propsMap}/>)
        expect(screen.getByText('First').closest('a')).toHaveAttribute('href', '/?page=1')

        pages.map((c) => {
            expect(screen.getByText(`${c}`).closest('a')).toHaveAttribute('href', `/?page=${c}`)
        })

        expect(screen.getByText('Last').closest('a')).toHaveAttribute('href', '/?page=2')
    })


})