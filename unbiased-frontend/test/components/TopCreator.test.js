import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import * as nextRouter from 'next/router';


import TimeSelector from "../../components/TimeSelector";
import PropTypes from "prop-types";
import TopCreator from "../../components/TopCreator";



const {render, screen, fireEvent} = testingLibrary;


let propsMap;

const customPropsMap = (options = {}) => {
    const map = {
        nameOrEmail: "example",
        id: 1,
        tier: "default",
        image: "aaaaa"
    }

    return { ...map, ...options };
};

describe('TopCreator test', ()=>{


    propsMap = customPropsMap()

    test('Test for correct name display', ()=>{


        render(<TopCreator {...propsMap}> </TopCreator>)


        expect(screen.getByText(propsMap.nameOrEmail)).toBeVisible()

    })

    test('Test for correct link', ()=>{

        render(<TopCreator {...propsMap}> </TopCreator>)


        expect(screen.getByText(propsMap.nameOrEmail).closest('a').getAttribute('href')).toContain(`${propsMap.id}`)

    })
})