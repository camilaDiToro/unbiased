import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import { getResourcePath } from "../../constants";
import ProfilePic from "../../components/ProfilePic";

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
  const map = {
    image: "http://localhost:8080/webapp_war_exploded/api/users/1/image",
  }

  return { ...map, ...options };
};

describe('Profile Pic test', ()=>{

  test('Profile pic shows correct profile image', ()=>{
    render(<ProfilePic{...propsMap}/>)
    expect(screen.getByRole('img', {name: 'profileImage'})).toBeInTheDocument()
  })

  test('Profile pic shows default profile image', ()=>{
    propsMap = customPropsMap({image: null})
    render(<ProfilePic {...propsMap}/>)
    const img = screen.getByTestId('img-profile')
    expect(img.getAttribute('src')).toEqual(getResourcePath(`/img/profile-image.png`))
  })

})


