import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import * as nextRouter from 'next/router';


import TimeSelector from "../../components/TimeSelector";
import PropTypes from "prop-types";
import TopCreator from "../../components/TopCreator";
import TopCreatorsPanel from "../../components/TopCreatorsPanel";
import {useTranslation} from "react-i18next";



const {render, screen, fireEvent} = testingLibrary;


let propsMap;

const customPropsMap = (options = {}) => {
    const map = {
        creators: []
    }

    return { ...map, ...options };
};

describe('TopCreator test', ()=>{

    beforeEach(() => {
        propsMap = customPropsMap()
    })



    test('Test for msg when empty', ()=>{


        render(<TopCreatorsPanel {...propsMap}> </TopCreatorsPanel>)


        expect(screen.getByTestId("no-creators")).toBeVisible()

    })

    test('Test for no msg when there are elements', ()=>{
        propsMap.creators.push(<div data-testid="test-elem" key="1"></div>)
        render(<TopCreatorsPanel {...propsMap}> </TopCreatorsPanel>)

        expect(screen.queryByTestId("no-creators")).toBeNull()

    })

    test('Test for correct children display', ()=>{
        propsMap.creators.push(<div data-testid="test-elem" key="1"></div>)
        render(<TopCreatorsPanel {...propsMap}> </TopCreatorsPanel>)

        expect(screen.getByTestId("test-elem")).toBeVisible()

    })
})