import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import {getResourcePath} from "../../constants";
import { getDefaultLoggedUser } from "../test_utils/defaultLoggedUser";
import ProfileTabs from "../../components/ProfileTabs";
import Tabs from "../../components/Tabs";
import ProgressBar from "../../components/ProgressBar";

const {render, screen, fireEvent} = testingLibrary;


let propsMap;

const customPropsMap = (options = {}) => {
  const map = {
    progress: 20,
    title: "TOURISM",
    i18n: true,
    categoryMap: {
      "categories.tourism": "TOURISM",
      "categories.entertainment": "SHOW",
      "categories.politics": "POLITICS",
      "categories.economics": "ECONOMICS",
      "categories.sports": "SPORTS",
      "categories.technology": "TECHNOLOGY"
    },
    triggerEffect: jest.fn()
  }

  return { ...map, ...options };
};

describe('Progress Bar test', ()=> {
  beforeEach(()=>{
    propsMap = customPropsMap()
  })

  test('Progress Bar show percent progress for one category', ()=>{
    const progress = propsMap.progress*100 + "%"
    render(<ProgressBar {...propsMap}/>)
    expect(screen.getByRole("progressbar")).toBeInTheDocument()
    expect(screen.getByText(progress)).toBeInTheDocument()
  })

  test('Progress Bar show progress for each category', ()=>{
    propsMap.title = propsMap.categoryMap["categories.economics"]
    render(<ProgressBar {...propsMap}/>)
    expect(screen.getByText("ECONOMICS")).toBeInTheDocument()
  })

})
