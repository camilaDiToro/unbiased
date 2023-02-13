import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import ProgressBar from "../../components/ProgressBar";
import i18n from "../../i18n/i18n";

const {render, screen} = testingLibrary;

let propsMap;

const customPropsMap = (options = {}) => {
  const map = {
    i18n: true,
    progress: 0.05555555555555555,
    title: "categories.tourism"
  }

  return { ...map, ...options };
};

describe('Progress Bar test', ()=> {
  beforeEach(()=>{
    i18n.changeLanguage('en')
    propsMap = customPropsMap()
  })

  test('Shows I18n title of the category', ()=> {
    render(<ProgressBar {...propsMap}/>)
    expect(screen.getByText('Tourism')).toBeInTheDocument()
  })

  test('Shows props title when the one is not a code in i18n', ()=>{
    propsMap = customPropsMap({i18n: false, title: "My custom title"})
    render(<ProgressBar {...propsMap}/>)
    expect(screen.queryByText(propsMap.title)).toBeInTheDocument()
  })

})
