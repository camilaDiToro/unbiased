


<nav class="d-flex navbar navbar-dark navbar-expand-sm bg-primary text-white">
    <div class="d-flex container-fluid" >
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
        <button type="button" class="btn btn-info rounded-pill" >
            <spring:message code="home.createArticle.button"/>
        </button></a>
</c:if>

            <form class="form-inline my-2 my-lg-0" method="GET" action="<c:url value="/TOP"/>">
                <spring:message code="navbar.search"  var="searchPlaceholder" />
                <input  style="background-image: url('<c:url value="/resources/images/lupa.png"/>');" class="search-form search form-control ml-2 btn-outline-info text-white bg-primary"
                       type="search" placeholder="${searchPlaceholder}" id="query" name="query"/>
            </form>
            <c:if test="${loggedUser != null}">
                <div class="dropdown dropdown-p">
                    <button class="btn btn-primary dropdown-toggle w-100" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                    <div class="w-fit d-flex flex-row align-items-center gap-1 border-info justify-content-center min-w-150px" >

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
                    <div id="navbar-dropdown" class="dropdown-menu bg-dropdown" aria-labelledby="dropdownMenuLink" >

                        <div class="dropdown-item">
                            <img  class="svg-btn profile-img" src="<c:url value="/resources/profile-svgrepo-com.svg"/> " alt="...">
                            <a class="text-decoration-none text-white" href="<c:url value="/profile/${loggedUser.id}"/>">
                                <spring:message code="navbar.myProfile"/>
                            </a>
                        </div>


                        <c:if test="${isAdmin}">
                            <div class="dropdown-item">
                                <img class="svg-btn moderation-img" src="<c:url value="/resources/panel-svgrepo-com.svg"/> " alt="...">
                                <a class="text-decoration-none text-white" href="<c:url value="/admin/reported_news"/>">
                                    <spring:message code="navbar.adminPanel"/>
                                </a>
                            </div>

                        </c:if>

                        <div class="dropdown-item ">
                            <img class="svg-btn moderation-img" src="<c:url value="/resources/log-out-svgrepo-com.svg"/> " alt="...">
                            <a class="text-decoration-none text-white" href="<c:url value="/logout"/>">
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
