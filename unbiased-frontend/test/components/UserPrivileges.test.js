import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import * as nextRouter from 'next/router';


import TimeSelector from "../../components/TimeSelector";
import PropTypes from "prop-types";
import TopCreator from "../../components/TopCreator";
import TopCreatorsPanel from "../../components/TopCreatorsPanel";
import {useTranslation} from "react-i18next";
import UserPrivileges from "../../components/UserPrivileges";



const {render, screen, fireEvent} = testingLibrary;


let propsMap;

const customPropsMap = (options = {}) => {
    const map = {
        isJournalist: false
    }

    return { ...map, ...options };
};

describe('TopCreator test', ()=>{

    beforeEach(() => {
        propsMap = customPropsMap()
    })



    test('Test for check absent when not journalist', ()=>{


        render(<UserPrivileges {...propsMap}> </UserPrivileges>)


        expect(screen.queryByTestId("journalist-only")).toBeNull()

    })

    test('Test for check present when journalist', ()=>{
        propsMap.isJournalist = true

        render(<UserPrivileges {...propsMap}> </UserPrivileges>)


        expect(screen.getByTestId("journalist-only")).toBeVisible()

    })


})