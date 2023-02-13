import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import {getResourcePath} from "../../constants";
import ProfileTabs from "../../components/ProfileTabs";
import { getDefaultLoggedUser } from "../test_utils/defaultLoggedUser";

const {render, screen, fireEvent} = testingLibrary;


let propsMap;

const customPropsMap = (options = {}) => {
  const map = {
    userId: 1,
    orders: [
      { text: 'Posts', params: {cat: 'MY_POSTS'} },
      { text: 'Saved', params: {cat: 'SAVED'}},
      { text: 'Upvoted', params: {cat: 'UPVOTED'}},
      { text: 'Downvoted', params: {cat: 'DOWNVOTED'}}
    ],
    selected: 'Saved',
    triggerEffect: jest.fn()
  }

  return { ...map, ...options };
};

describe('Profile Tabs test', ()=> {
  beforeEach(()=>{
    propsMap = customPropsMap()
  })

  test('Profile Tab show the correct tab selection', ()=>{

    propsMap.selected = 'Posts'
    render(<ProfileTabs {...propsMap}> </ProfileTabs>)

    expect(screen.getByText('Posts')).toBeVisible()
    expect(screen.getByText('Posts').classList).toContain('active')
  })

  test('Profile Tab show the correct tab selection when is loggeduser', ()=>{

    const loggedUser = getDefaultLoggedUser()
    propsMap.selected = 'Posts'
    render(<ProfileTabs {...propsMap}/>, {loggedUser})

    expect(screen.getByText('Posts')).toBeVisible()
    expect(screen.getByText('Posts').classList).toContain('active')
  })
})