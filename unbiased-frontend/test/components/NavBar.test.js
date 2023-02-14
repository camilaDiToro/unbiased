import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import { getDefaultLoggedUser } from "../test_utils/defaultLoggedUser";
import Navbar from "../../components/Navbar";
import i18n from "../../i18n/i18n";

const {render, screen} = testingLibrary;

describe('NavBar test', ()=>{

  beforeEach(()=>{
    i18n.changeLanguage('en')
  })

  test('Shows Create button if is loggeduser and pathname do not start with admin', ()=>{
    const loggedUser = getDefaultLoggedUser()
    const query = {
      search: ''
    }
    render(<Navbar/>, {loggedUser, pathname: '/no/starts/with/admin', query})
    expect(screen.getByRole('button', {name: 'Create'})).toBeInTheDocument()
  })

  test('Do not Show Create button if is loggeduser and pathname start with admin', ()=>{
    const loggedUser = getDefaultLoggedUser()
    const query = {
      search: ''
    }
    render(<Navbar/>, {loggedUser, pathname: '/admin', query})
    expect(screen.queryByRole('button', {name: 'Create'})).toBeNull()
  })

  test('Shows form if is pathname do not start with admin', ()=>{
    const query = {
      search: ''
    }
    render(<Navbar/>, {query, pathname: '/no/starts/with/admin'})
    expect(screen.getByTestId('search-form')).toBeInTheDocument()
  })

  test('If it is a logged user shows dropdown and his items correctly and do not show Login/SignUp', ()=>{
    const loggedUser = getDefaultLoggedUser({nameOrEmail: 'user@example.com'}) //by default is admin
    const query = {
      search: ''
    }
    render(<Navbar/>, {query, loggedUser})
    expect(screen.getByTestId('dropdown')).toBeInTheDocument()
    expect(screen.getByText(loggedUser.nameOrEmail)).toBeInTheDocument()
    expect(screen.getByText('Administration panel')).toBeInTheDocument()
    expect(screen.queryByText('Log in')).toBeNull()
    expect(screen.queryByText('Sign up')).toBeNull()

  })

  test('If user is not admin should not show the Administration panel', ()=>{
    const loggedUser = getDefaultLoggedUser({nameOrEmail: 'user@example.com', isAdmin: false})
    const query = {
      search: ''
    }
    render(<Navbar/>, {query, loggedUser})
    expect(screen.queryByText('Administration panel')).toBeNull()

  })

  test('Shows Login and Signup button if is not a loggedUser', ()=>{
    const loggedUser = null
    const query = {
      search: ''
    }
    render(<Navbar/>, {loggedUser, query})
    expect(screen.getByText('Log in')).toBeInTheDocument()
    expect(screen.getByText('Sign up')).toBeInTheDocument()
  })
})