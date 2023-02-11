import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import { getDefaultLoggedUser } from "../test_utils/defaultLoggedUser";

jest.mock("next/link", () => {
  return ({children}) => {
    return children;
  }
});
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

describe('NavBar test', ()=>{

  test('NavBar show up Log In and Sign In if it is a loggedUser', ()=>{

  })
})