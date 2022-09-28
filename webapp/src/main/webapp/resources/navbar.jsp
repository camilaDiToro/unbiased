


<nav style="display: flex" class="navbar navbar-dark navbar-expand-sm bg-primary text-white">

    <div style="display: flex" class="container-fluid" style="border: solid red">
        <a class="navbar-brand  " href="<c:url value="/TOP"/>">
            <img src="<c:url value="/resources/unbiased_navbar.png"/>" height="35" alt="" class="d-inline-block align-middle mr-2">
            <span class="text-info">unbiased</span>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>



        <div class="d-flex h-75 gap-2 align-items-center">
<c:if test="${loggedUser != null}">
    <a href="<c:url value="/create_article"/>">
        <button type="button" class="btn btn-info" style="border-radius: 999px">
            <spring:message code="home.createArticle.button"/>
        </button></a>
</c:if>

            <form class="form-inline my-2 my-lg-0" method="GET" action="<c:url value="/TOP"/>">
                <spring:message code="navbar.search"  var="searchPlaceholder" />
                <input style="height: 30px; padding-left: 35px; background-image: url('<c:url value="/resources/images/lupa.png"/>'); background-repeat: no-repeat; background-position: left center; background-size: 10%;" class="form-control ml-2 btn-outline-info text-white bg-primary"
                       type="search" placeholder="${searchPlaceholder}" id="query" name="query"/>
            </form>
            <c:if test="${loggedUser != null}">
                <div class="dropdown dropdown-p">
                    <button class="btn btn-primary dropdown-toggle w-100" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                    <div class="w-fit d-flex flex-row align-items-center gap-1 border-info justify-content-center" style="min-width: 150px">

                                                        <div class="img-container-navbar">
                                                            <c:if test="${loggedUser.hasImage()}">
                                                                <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/profile/${loggedUser.imageId}/image"/>" alt="">

                                                            </c:if>

                                                            <c:if test="${!loggedUser.hasImage()}">
                                                                <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/resources/profile-image.png"/>" alt="">

                                                            </c:if>
                                                        </div>

                                                            <b class="text-white"><c:out value="${loggedUser}"/></b>

                                                    </div>
                    </button>
                    <div id="navbar-dropdown" class="dropdown-menu" aria-labelledby="dropdownMenuLink" style="background: #181414">
                        <%--<a class="dropdown-item" href="<c:url value="/profile/${loggedUser.id}"/>" style="padding-left: 30px; background-image: url('<c:url value="/resources/profile-svgrepo-com.svg"/>'); background-repeat: no-repeat; background-position: left center; background-size: 10%">
                            <spring:message code="navbar.myProfile"/>
                        </a>

                        <c:if test="${isAdmin}">
                            <a class="dropdown-item" href="<c:url value="/admin/reported_news"/>" style="padding-left: 30px; background-image: url('<c:url value="/resources/panel-svgrepo-com.svg"/>'); background-repeat: no-repeat; background-position: left center; background-size: 10%">
                                <spring:message code="navbar.adminPanel"/>
                            </a>
                        </c:if>

                        <a class="dropdown-item" href="<c:url value="/logout"/>" style="padding-left: 30px; background-image: url('<c:url value="/resources/log-out-svgrepo-com.svg"/>'); background-repeat: no-repeat; background-position: left center; background-size: 10%;">
                            <spring:message code="navbar.logOut"/>
                        </a>--%>

                        <div class="dropdown-item">
                            <img style="width: 24px; padding-right: 5px" class="svg-btn" src="<c:url value="/resources/profile-svgrepo-com.svg"/> " alt="...">
                            <a style="text-decoration: none; color: white" href="<c:url value="/profile/${loggedUser.id}"/>">
                                <spring:message code="navbar.myProfile"/>
                            </a>
                        </div>


                        <c:if test="${isAdmin}">
                            <div class="dropdown-item">
                                <img style="width: 24px; padding-right: 5px" class="svg-btn" src="<c:url value="/resources/panel-svgrepo-com.svg"/> " alt="...">
                                <a style="text-decoration: none; color: white" href="<c:url value="/admin/reported_news"/>">
                                    <spring:message code="navbar.adminPanel"/>
                                </a>
                            </div>

                        </c:if>

                        <div class="dropdown-item">
                            <img style="width: 24px; padding-right: 5px" class="svg-btn" src="<c:url value="/resources/log-out-svgrepo-com.svg"/> " alt="...">
                            <a style="text-decoration: none; color: white" href="<c:url value="/logout"/>">
                                <spring:message code="navbar.logOut"/>
                            </a>
                        </div>


                    </div>

                </div>

            </c:if>

            <c:if test="${loggedUser == null}">
                <a role="button" class="btn btn-primary" href="<c:url value="/login"/>"><spring:message code="navbar.logIn"/></a>
                <a role="button" class="btn btn-primary" href="<c:url value="/create"/>"><spring:message code="navbar.register"/></a>
            </c:if>

        </div>
    </div>
</nav>
