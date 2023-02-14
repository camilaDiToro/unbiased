import React from 'react'
import * as testingLibrary from "../test_utils/contextRender";
import Creator from "../../components/Creator";

const {render, screen} = testingLibrary;

jest.mock('../../components/PositivityIndicator', ()=>{
    return jest.fn(()=> <div title="positivityIndicator">Mocked positivity indicator</div>)
})

jest.mock('../../components/ModalTrigger', ()=>{
    return jest.fn(()=> <div title="modalTrigger">Mocked modal trigger</div>)
})

jest.mock('../../constants', () => ({
    getResourcePath: jest.fn(() => 'mocked/path/to/image.png'),
}));

let propsMap

const customPropsMap = (options = {}) => {
    const map = {
        admin: true,
        hasPositivity: true,
        isJournalist: true,
        tier: "default",
        id: 1,
        nameOrEmail: "Aleca",
        hasImage: true,
        image: "http://localhost:8080/webapp_war_exploded/users/1/image"
    }

    return { ...map, ...options };
};

describe('Creator test', ()=>{

    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('Show positivity indicator when the user have one', ()=>{
        render(<Creator {...propsMap}/>)
        expect(screen.getByTitle('positivityIndicator')).toBeInTheDocument()
    })

    test('Do not show positivity indicator when the user do not have one', ()=>{
        propsMap = customPropsMap({hasPositivity: false})
        render(<Creator {...propsMap}/>)
        expect(screen.queryByTitle('positivityIndicator')).toBeNull()
    })

    test('Shows modal trigger if the user is an admin', ()=>{
        render(<Creator {...propsMap}/>)
        expect(screen.getByTitle('modalTrigger')).toBeInTheDocument()
    })

    test('Do not show modal trigger if the user is not an admin', ()=>{
        propsMap = customPropsMap({admin: false})
        render(<Creator {...propsMap}/>)
        expect(screen.queryByTitle('modalTrigger')).toBeNull()
    })

    test('Use the profile image of the user', ()=>{
        render(<Creator {...propsMap}/>)
        const image = screen.getByAltText('profile-image')
        expect(image.getAttribute('src')).toBe(propsMap.image)
    })

    test('Use the default image for the user', ()=>{
        propsMap = customPropsMap({hasImage: false})
        render(<Creator {...propsMap}/>)
        const image = screen.getByAltText('profile-image')
        expect(image.getAttribute('src')).toBe('mocked/path/to/image.png')
    })

})