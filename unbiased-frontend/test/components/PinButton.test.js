import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import { getDefaultLoggedUser } from "../test_utils/defaultLoggedUser";
import PinButton from "../../components/PinButton";

jest.mock('../../constants', () => ({
  getResourcePath: jest.fn(),
}));

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
  const map = {
    id: 1,
    pinned: true,
    creatorId: 1,
    triggerEffect: jest.fn()
  }

  return { ...map, ...options };
};

describe('Pin Button test', ()=>{

  beforeEach(()=>{
    propsMap = customPropsMap()
  })

  test('Pin Button show up if it is my profile', ()=>{
    const loggedUser = getDefaultLoggedUser({id: 1})
    render(<PinButton {...propsMap}/>, {loggedUser})
    expect(screen.getByAltText('pin-image')).toBeInTheDocument()
  })

  test('Pin Button does not show up if it is not my profile', ()=>{
    const loggedUser = getDefaultLoggedUser({id: 2})
    render(<PinButton {...propsMap}/>, {loggedUser})
    expect(screen.queryByAltText('pin-image')).toBeNull()
  })

})