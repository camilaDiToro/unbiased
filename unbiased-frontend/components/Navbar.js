import Link from "next/link"
import { useRouter } from "next/router";

import {useAppContext} from "../context";
import types from "../types";
import {useState} from "react";




export default function Navbar(props) {
    const {I18n, loggedUser} = useAppContext()
    const router = useRouter()
    const [query, setQuery] = useState(router.query.search || '')
    const goToSearchPage = (e) => {
        if (e.key === 'Enter') {
            router.push({
                pathname: '/',
                query: {...(router.query.type && {type: router.query.type}), ...(e.target.value && {search: e.target.value})}
            })
        }
    }

    const paths = ['/login', '/register']

    if (paths.includes(router.pathname)) {
        return <></>
    }

    return <nav className="d-flex navbar navbar-dark navbar-expand-sm bg-primary text-white h-75px">
        <div className="d-flex container-fluid h-100">
            <Link className="navbar-brand" href="/">
                <img src="/img/unbiased_navbar.png" height="35" alt=""
                     className="d-inline-block align-middle mr-2"/>
                <span className="text-info">unbiased</span>
            </Link>
            <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText"
                    aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>


            <div className="d-flex h-75 gap-2 align-items-center h-100">
                {loggedUser && !(router.pathname.startsWith('/admin'))  ? <Link href="/create-article">
                    <button type="button" className="btn btn-sm rounded-pill btn-info create_article_navbar_btn">
                        {I18n("home.createArticle.button")}
                    </button>
                </Link> : <></>}

                {router.pathname.startsWith('/admin') ? <></> : <form id="search-form" className="form-inline my-2 my-lg-0" method="GET" action="/TOP">
                    <div>
                        <input
                            style={{backgroundImage: 'url("/img/loupe-svgrepo-com.svg")'}}
                            className="search-form search form-control text-white"
                            type="search" placeholder={I18n("navbar.search")} id="query" name="query"
                            value={query}
                            onChange={(e) => setQuery(e.target.value)}
                            onKeyDown={goToSearchPage}
                        />
                    </div>
                    <div className="invisible position-fixed">
                        <input name="type"/>
                    </div>
                </form>}

                {loggedUser ? <div className="dropdown dropdown-p h-100">
                    <button className="btn btn-primary dropdown-toggle w-100 h-100 py-0" type="button" id="dropdownMenuButton"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <div
                            className="w-fit d-flex flex-row align-items-center gap-1 border-info justify-content-center min-w-150px">

                            <div className="img-container-navbar">

                                <div className="frame-navbar">
                                    <img src="/img/profile-image.png"
                                         className="rounded-circle object-fit-cover mr-1"/>
                                </div>
                            </div>


                            <b className="text-white">
                                {loggedUser.nameOrEmail}
                            </b>

                        </div>
                    </button>
                    <div id="navbar-dropdown" className="dropdown-menu bg-dropdown"
                    >

                        <Link className="dropdown-item" href={`/profile/${loggedUser.id}`}>
                            <div className="text-decoration-none text-white"
                            >
                                <img className="profile-img"
                                     src="/img/profile-svgrepo-com.svg"/>
                                {I18n("navbar.myProfile")}
                            </div>
                        </Link>


                        {loggedUser.isAdmin ? <Link className="dropdown-item" href="/admin/reported_news">
                            <div className="text-decoration-none text-white"
                            >
                                <img className="moderation-img"
                                     src="/img/panel-svgrepo-com.svg" alt="..."/>
                                {I18n("navbar.adminPanel")}
                            </div>
                        </Link>: <></>}


                        <Link className="dropdown-item " href="/logout">
                            <div className="text-decoration-none text-white" >
                                <img className="moderation-img"
                                     src="/img/log-out-svgrepo-com.svg" alt="..."/>
                                {I18n("navbar.logout")}
                            </div>
                        </Link>
                    </div>

                </div>
                 : <>
                        <Link className="btn btn-primary" href="/login">
                            {I18n("navbar.logIn")}
                        </Link>
                        <Link  className="btn btn-primary" href="/create">
                            {I18n("navbar.register")}
                        </Link></>}


            </div>
        </div>
    </nav>

}

Navbar.propTypes = types.Navbar

