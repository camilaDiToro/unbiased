import Link from "next/link"
import { useRouter } from "next/router";

import {useAppContext} from "../context";

export default function Navbar() {
    const ctx = useAppContext()

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
                {/*<c:if test="${loggedUser != null && !adminPage}">*/}
                <Link href="">
                    <button type="button" className="btn btn-sm rounded-pill btn-info create_article_navbar_btn">
                        {ctx.I18n("home.createArticle.button")}
                    </button>
                </Link>
                {/*</c:if>*/}

                {/*<c:if test="${!adminPage}">*/}
                    <form id="search-form" className="form-inline my-2 my-lg-0" method="GET" action="/TOP">
                        <div>
                            {/*<spring:message code="navbar.search" var="searchPlaceholder"/>*/}
                            <input
                                style={{backgroundImage: 'url("/loupe-svgrepo-com.svg")'}}
                                className="search-form search form-control text-white"
                                type="search" placeholder="placeholder" id="query" name="query"
                                />
                        </div>
                        <div className="invisible position-fixed">
                            <input name="type"/>
                        </div>
                        <script>

                        </script>
                    </form>
                {/*</c:if>*/}

                {/*<c:if test="${loggedUser != null}">*/}
                    <div className="dropdown dropdown-p h-100">
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
                                    Usuario
                                </b>

                            </div>
                        </button>
                        <div id="navbar-dropdown" className="dropdown-menu bg-dropdown"
                             >

                            <div className="dropdown-item">
                                <a className="text-decoration-none text-white"
                                  >
                                    <img className="profile-img"
                                         src="/img/profile-svgrepo-com.svg"/>
                                        AAAAA
                                </a>
                            </div>


                            {/*<c:if test="${isAdmin}">*/}
                                <div className="dropdown-item">
                                    <a className="text-decoration-none text-white"
                                       href="/admin/reported_news">
                                        <img className="moderation-img"
                                             src="/img/panel-svgrepo-com.svg" alt="..."/>
                                            BBBBB
                                    </a>
                                </div>

                            {/*</c:if>*/}

                            <div className="dropdown-item ">
                                <Link className="text-decoration-none text-white" href="/logout">
                                    <img className="moderation-img"
                                         src="/img/log-out-svgrepo-com.svg" alt="..."/>
                                        CCCCC
                                </Link>
                            </div>


                        </div>

                    </div>

                {/*</c:if>*/}

                {/*<c:if test="${loggedUser == null}">*/}
                    <Link className="btn btn-primary" href="/login">
                        {/*<spring:message code="navbar.logIn"/>*/}
                    </Link>
                    <Link  className="btn btn-primary" href="/create">
                        {/*<spring:message code="navbar.register"/>*/}
                    </Link>
                {/*</c:if>*/}

            </div>
        </div>
    </nav>

}

