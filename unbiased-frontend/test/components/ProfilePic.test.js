import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import ProfilePic from "../../components/ProfilePic";

jest.mock('../../constants', () => ({
  getResourcePath: jest.fn(() => 'mocked/path/to/image.png'),
}));

const {render, screen} = testingLibrary;

let propsMap

const customPropsMap = (options = {}) => {
  const map = {
    image: "http://localhost:8080/webapp_war_exploded/api/users/1/image",
  }

  return { ...map, ...options };
};

describe('Profile Pic test', ()=>{

  test('Shows correct profile image if the user have one', ()=>{
    propsMap = customPropsMap()
    render(<ProfilePic{...propsMap}/>)
    const image = screen.getByRole('img', {name: 'profileImage'})
    expect(image).toBeInTheDocument()
    expect(image.getAttribute('src')).toBe(propsMap.image)
  })

  test('Shows default image if the user do no have one', ()=>{
    propsMap = customPropsMap({image: null})
    render(<ProfilePic{...propsMap}/>)
    const image = screen.getByRole('img', {name: 'profileImage'})
    expect(image).toBeInTheDocument()
    expect(image.getAttribute('src')).toBe('mocked/path/to/image.png')
  })

})


