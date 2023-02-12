import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import { getDefaultLoggedUser } from "../test_utils/defaultLoggedUser";
import Navbar from "../../components/Navbar";

jest.mock("next/link", () => {
  return jest.fn(() => <nav role="button">Log In</nav>)
});

jest.mock("next/link", () => {
  return jest.fn(() => <nav role="button">Sign Up</nav>)
});

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
  const map = {
    creator: {
      hasImage: true,
      Image: "http://localhost:8080/webapp_war_exploded/api/users/1/image",
      id: 1,
      nameOrEmail: "My name",
      tier: "default",
    },
    triggerEffect: jest.fn()
  }

  return { ...map, ...options };
};

describe('NavBar test', ()=>{

  beforeEach(()=>{
    propsMap = customPropsMap()
  })

  test('NavBar show up Log In and Sign In if it is not a loggedUser', ()=>{
    render(<Navbar  {...propsMap}/>)
    expect(screen.getByRole('button', {name: 'Log In'})).toBeInTheDocument()
    expect(screen.getByRole('button', {name: 'Sign Up'})).toBeInTheDocument()
  })
})