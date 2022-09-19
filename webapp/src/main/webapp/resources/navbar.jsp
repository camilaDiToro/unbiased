


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
    <a href="./create_article">
        <button type="button" class="btn btn-info">
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
                                                    <div class="w-fit d-flex flex-row align-items-center gap-1 border-info">

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
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                        <a class="dropdown-item" href="<c:url value="/profile/${loggedUser.id}"/>">My Profile</a>
                        <a class="dropdown-item" href="<c:url value="/logout"/>">Logout</a>
                    </div>

                </div>




            </c:if>

            <c:if test="${loggedUser == null}">
                <a role="button" class="btn btn-primary" href="<c:url value="/login"/>">Login</a>
                <a role="button" class="btn btn-primary" href="<c:url value="/create"/>">Register</a>
            </c:if>

        </div>
    </div>
</nav>
