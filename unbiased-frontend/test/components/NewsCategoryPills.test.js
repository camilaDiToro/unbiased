import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import NewsCategoryPills from "../../components/NewsCategoryPills";

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
    const map = {
        categories: ['categories.sports', 'categories.technology', 'categories.economics']
    }

    return { ...map, ...options };
};

describe('News category pills test', ()=>{

    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('Shows the visible categories and redirects to correct path', ()=>{
        const categoryMap = {
            "categories.tourism": "TOURISM",
            "categories.entertainment": "SHOW",
            "categories.politics": "POLITICS",
            "categories.economics": "ECONOMICS",
            "categories.sports": "SPORTS",
            "categories.technology": "TECHNOLOGY"
        };
        render(<NewsCategoryPills {...propsMap}/>)
        propsMap.categories.forEach((c)=>{
            const categoryName = c.split(".")[1]
            const capitalizedCategoryName = categoryName.charAt(0).toUpperCase() + categoryName.slice(1);
            const categoryLink = screen.getByRole('link', { name:  capitalizedCategoryName});
            expect(categoryLink).toBeInTheDocument()
            expect(categoryLink).toHaveAttribute('href', `/?cat=${categoryMap[c]}`)
        })
    })

})