import Link from "next/link"
import { useRouter } from "next/router";

import {useAppContext} from "../context";

export default function Navbar() {
    const router = useRouter();
    const ctx = useAppContext()

    return <nav className="d-flex navbar navbar-dark navbar-expand-sm bg-primary text-white">
        <div className="d-flex container-fluid">
            <Link className="navbar-brand" href="/">
                <img src="/unbiased_navbar.png" height="35" alt=""
                     className="d-inline-block align-middle mr-2"/>
                <span className="text-info">unbiased</span>
            </Link>
            <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText"
                    aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>


            <div className="d-flex h-75 gap-2 align-items-center">
                {/*<c:if test="${loggedUser != null && !adminPage}">*/}
                <a href="">
                    <button type="button" className="btn btn-sm rounded-pill btn-info create_article_navbar_btn">
                        {ctx.I18n("home.createArticle.button")}
                    </button>
                </a>
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
                    <div className="dropdown dropdown-p">
                        <button className="btn btn-primary dropdown-toggle w-100" type="button" id="dropdownMenuButton"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <div
                                className="w-fit d-flex flex-row align-items-center gap-1 border-info justify-content-center min-w-150px">

                                <div className="img-container-navbar">

                                    <div className="frame-navbar">
                                        <img src="/profile-image.png"
                                             className="rounded-circle object-fit-cover mr-1"/>
                                    </div>
                                </div>


                                <b className="text-white">
                                    {/*<c:out value="${loggedUser}"/>*/}
                                </b>

                            </div>
                        </button>
                        <div id="navbar-dropdown" className="dropdown-menu bg-dropdown"
                             aria-labelledby="dropdownMenuLink">

                            <div className="dropdown-item">
                                <a className="text-decoration-none text-white"
                                  >
                                    <img className="profile-img"
                                         src="/profile-svgrepo-com.svg"/>
                                        {/*<spring:message code="navbar.myProfile"/>*/}
                                </a>
                            </div>


                            {/*<c:if test="${isAdmin}">*/}
                                <div className="dropdown-item">
                                    <a className="text-decoration-none text-white"
                                       href="/admin/reported_news">
                                        <img className="moderation-img"
                                             src="/panel-svgrepo-com.svg" alt="..."/>
                                            {/*<spring:message code="navbar.adminPanel"/>*/}
                                    </a>
                                </div>

                            {/*</c:if>*/}

                            <div className="dropdown-item ">
                                <a className="text-decoration-none text-white" href="/logout">
                                    <img className="moderation-img"
                                         src="/log-out-svgrepo-com.svg" alt="..."/>
                                        {/*<spring:message code="navbar.logOut"/>*/}
                                </a>
                            </div>


                        </div>

                    </div>

                {/*</c:if>*/}

                {/*<c:if test="${loggedUser == null}">*/}
                    <a role="button" className="btn btn-primary" href="/login">
                        {/*<spring:message code="navbar.logIn"/>*/}
                    </a>
                    <a role="button" className="btn btn-primary" href="/create">
                        {/*<spring:message code="navbar.register"/>*/}
                    </a>
                {/*</c:if>*/}

            </div>
        </div>
    </nav>

}

