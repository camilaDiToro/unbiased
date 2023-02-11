import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import { getDefaultLoggedUser } from "../test_utils/defaultLoggedUser";
import PinButton from "../../components/PinButton";
import Modal from "../../components/Modal";
import ModalTrigger from "../../components/ModalTrigger";

jest.mock('../../components/Modal', ()=>{
  return jest.fn(() => <Modal title="modal" role="modal">Mock modal</Modal>)
})

jest.mock('../../components/ModalTrigger', ()=>{
  return jest.fn(() => <ModalTrigger title="modal-trigger" role="modal-trigger">Mock modal trigger</ModalTrigger>);
})

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

  test('Pin Button show up if it is my profile', ()=>{

    propsMap = customPropsMap()
    const loggedUser = getDefaultLoggedUser()
    const isMyProfile = loggedUser && propsMap.creatorId === loggedUser.userId
    render(<PinButton {...propsMap}/>, {isMyProfile})
    console.log(propsMap)
    expect(screen.getByText('modal')).toBeInTheDocument
  })

  test('Pin Button does not show up if it is not my profile', ()=>{

    propsMap = customPropsMap()
    render(<PinButton {...propsMap}/>)
    expect(screen.queryByText('modal', {name: 'Mocked modal'})).toBeNull()
    expect(screen.queryByRole('modal-trigger', {name: 'Mocked modal trigger'})).toBeNull()
  })

})