<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<c:set var="pageTitle" scope="request" value="${pageTitle}"/>
<%@ include file="../../resources/head.jsp" %>
<script src="<c:url value="/resources/upvote-script.js"/>"></script>
<c:if test="${param.unable || param.error}">
    <script>
        $(document).ready(function(){
            $("#cardModal").modal('show');
        });
    </script>
</c:if>
<body>


<div class="d-flex h-100 flex-column">
<%--    <c:set var="loggedUser" scope="request" value="${user}"/>--%>
    <%@ include file="../../resources/navbar.jsp" %>
    <div class="container-xxl container-fluid flex-grow-1">
        <c:set var = "activeClasses" scope = "session" value = "bg-info active"/>
        <c:set var = "inactiveClasses" scope = "session" value = "text-secondary"/>
        <ul class="${empty query ? 'my-4' :''} mt-4 nav bg-transparent nav-pills text-light p-2 rounded-lg d-flex ${type == 'creator' ? 'invisible' : 'visible'}">
            <c:forEach var="order" items="${orders}">
                <li class="nav-item">
                    <a class="text-capitalize nav-link fromLeft rounded-pill <c:out value = "${orderBy == order ? activeClasses : inactiveClasses}"/>" aria-current="page" href="<c:url value = "/${order}">
                    <c:param name = "category" value = "${category}"/>
                    <c:if test="${!empty query}"><c:param name = "query" value = "${param.query}"/></c:if>
                    </c:url>"><spring:message code="${order.interCode}"/></a>
                </li>
            </c:forEach>


        </ul>

        <div class="d-flex flex-column flex-xl-row ">
            <div class="w-100 w-xl-75 ">
                    <c:if test="${query == ''}">
                        <ul class="my-2 nav nav-tabs justify-content-center text-light p-2">
                            <li class="nav-item">
                                <a class="text-capitalize text-white nav-link tabs <c:out value = "${category.toString() == 'ALL' ? 'active' : ''}"/>" aria-current="page" href="<c:url value = "/${orderBy}">
                    <c:param name = "query" value = "${param.query}"/>
                    </c:url>"><spring:message code="categories.all"/></a>
                            </li>
                            <c:forEach var="cat" items="${categories}">
                                <li class="nav-item">
                                    <a class="text-capitalize text-white nav-link tabs <c:out value = "${category.toString() != 'ALL' && category == cat ? 'active': ''}"/>" aria-current="page" href="<c:url value = "/${orderBy}">
                    <c:param name = "category" value = "${cat}"/>

                    </c:url>"><spring:message code="${cat.interCode}"/></a>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                <c:if test="${query != ''}">
                    <div class="m-3 ">
                        <a class="link" href="<c:url value="/${orderBy}"/>"><div class="link-text"><spring:message code="search.filter" arguments="${query}"/></div></a>
                    </div>
                    <ul class="my-2 nav nav-tabs justify-content-center text-light p-2">

                            <li class="nav-item">
                                <a class="text-capitalize nav-link tabs <c:out value = "${type == 'article' ? 'active' : ''}"/>" aria-current="page" href="<c:url value = "/${orderBy}">
                    <c:param name = "type" value = "article"/>
                    <c:if test="${!empty query}"><c:param name = "query" value = "${param.query}"/></c:if>
                    </c:url>"><spring:message code="home.type.article"/></a>
                            </li>
                            <li class="nav-item">
                                <a class="text-capitalize nav-link tabs <c:out value = "${type == 'creator' ? 'active' : ''}"/>" aria-current="page" href="<c:url value = "/${orderBy}">
                    <c:param name = "type" value = "creator"/>
                    <c:if test="${!empty query}"><c:param name = "query" value = "${param.query}"/></c:if>
                    </c:url>"><spring:message code="home.type.creator"/></a>
                            </li>



<%--                        <c:forEach var="cat" items="${categories}">--%>
<%--                            <li class="nav-item">--%>
<%--                                <a class="text-capitalize text-white nav-link tabs <c:out value = "${category.toString() != 'ALL' && category == cat ? 'active': ''}"/>" aria-current="page" href="<c:url value = "/${orderBy}">--%>
<%--                    <c:param name = "category" value = "${cat}"/>--%>

<%--                    </c:url>"><spring:message code="${cat.interCode}"/></a>--%>
<%--                            </li>--%>
<%--                        </c:forEach>--%>
                    </ul>
                </c:if>

                    <c:if test="${type=='article'  && empty newsPage.content}" >
                        <div class="h-75 d-flex flex-column justify-content-center align-items-center flex-grow-1 mt-5">
                            <h2 class="fw-normal"><spring:message code="home.emptyCategory.sorry"/></h2>
                                <%--                    <p class="fs-1"> <span class="text-info font-weight-bold">Oops!</span> </p>--%>
                            <p class="lead">
                                <c:if test="${query == '' && category.toString() != 'ALL'}">
                                    <spring:message code="categories.notFound"/> "<spring:message code="${category.interCode}"/>"
                                </c:if>
                                <c:if test="${ query != ''}">
                                    <spring:message code="search.notFound"/> "<c:out value="${query}"/>"
                                </c:if>

                                <c:if test="${category.toString() == 'ALL' && query == ''}">
                                    <spring:message code="categories.notFound"/> "<spring:message code="categories.all"/>"
                                </c:if>


                            </p>
                        </div>
                    </c:if>
                    <c:if test="${type=='article' && not empty newsPage.content}">

                    <div class="container-fluid">
                        <div class="row row-cols-1 row-cols-md-2">
                            <c:set var="news" value="${newsPage.content}"/>

                            <c:forEach var="fullNews" items="${news}">
                                <c:set var="article" value="${fullNews.news}"/>

                                <c:set var="newsId" value="${article.newsId}"/>

                                <c:set var="loggedParameters" value="${fullNews.loggedUserParameters}"/>
                                <c:set var="rating" value="${loggedParameters != null ? loggedParameters.personalRating : ''}"/>
                                <c:set var="saved" value="${loggedParameters != null ? loggedParameters.saved : false}"/>

                                <div class="col mb-4">
                                    <div class="card h-100 d-flex flex-row" >
                                        <c:set var="positivityStats" value="${fullNews.positivityStats}"/>

                                        <c:set var="positivity" value="${positivityStats.positivity}"/>
                                        <div class="quality-indicator <c:out value="${positivity}"/>" data-toggle="tooltip" data-placement="top" title="<spring:message code="home.upvotes" arguments="${positivityStats.getPercentageUpvoted()}"/> - <spring:message code="home.interactions" arguments="${positivityStats.getInteractions()}"/>" >
                                        </div>
                                        <div class="d-flex flex-column justify-content-between ${article.hasImage() ? 'w-60' : 'w-100'}">
                                            <div class="d-flex w-100">
                                                <div class="upvote-div-home d-flex flex-column align-items-center m-3" news-id="<c:out value="${article.newsId}"/>">
                                                    <c:set var="rating" value="${rating}"/>

                                                     <c:if test="${loggedUser != null}">
                                                         <img style="width: 24px" url="<c:url value = "/change-upvote"/>" id="upvote" onclick="handleClick(this)" class="svg-btn" src="<c:url value="/resources/upvote${rating.toString() == 'upvoted'? '-clicked' : ''}.svg"/>"/>
                                                         <div id="rating" class="${rating.toString()}"><c:out value="${positivityStats.getNetUpvotes()}"/></div>
                                                         <img style="width: 24px" id="downvote" url="<c:url value = "/change-downvote"/>" onclick="handleClick(this)" class="svg-btn" src="<c:url value="/resources/downvote${rating.toString() == 'downvoted' ? '-clicked' : ''}.svg"/>"/>
                                                     </c:if>
                                                    <c:if test="${loggedUser == null}">
                                                        <%--href="<c:url value = "/create"/>"--%>
                                                        <a data-toggle="modal" data-target="#cardModal">
                                                            <img   class="svg-btn" src="<c:url value="/resources/upvote.svg"/>"/>
                                                        </a>
                                                        <div  ><c:out value="${positivityStats.netUpvotes}"/></div>
                                                        <a data-toggle="modal" data-target="#cardModal">
                                                            <img    class="svg-btn" src="<c:url value="/resources/downvote.svg"/>"/>
                                                        </a>

                                                    </c:if>

                                                </div>
                                                <div class="card-body-home">
<%--                                                    <span class="badge badge-pill badge-primary m-1">Messi</span> <span class="badge badge-pill badge-primary">Messi</span>--%>
                                                    <a id="title-principal-card" class="link" style="max-height: 10%" href="<c:url value="/news/${article.newsId}"/>"><h5 class="link-text text-ellipsis-3"><c:out value="${article.title}"/></h5></a>
                                                    <h6 class="card-subtitle py-1 text-ellipsis-2 text-white"><c:out value="${article.subtitle}"/></h6>

                                                    <div>
                                                        <p class="text-sm-left text-secondary mb-0 text-white d-flex align-content-center gap-1" style="opacity: 0.9">
                                                            <img src="<c:url value="/resources/clock-svgrepo-com.svg"/>" alt="..." style="width: 15px; margin-top: 1px"/>
                                                            <spring:message code="home.read" arguments="${fullNews.readTime}"/>
                                                        </p>
                                                    </div>


                                                </div>
                                            </div>
                                            <div class="d-flex justify-content-between p-2 w-100">
                                                <div class="d-flex align-items-center w-auto gap-1">
                                                    <div class="img-container-article">
                                                        <c:if test="${fullNews.user.hasImage()}">
                                                            <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/profile/${fullNews.user.imageId}/image"/>" alt="">
                                                        </c:if>
                                                        <c:if test="${!fullNews.user.hasImage()}">
                                                            <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/resources/profile-image.png"/>" alt="">
                                                        </c:if>
                                                    </div>
                                                    <a class="link" href="<c:url value="/profile/${article.creatorId}"/>">
                                                        <div id="profile_name_card" class="card-name-text text-ellipsis-1">${fullNews.user}</div>

                                                    </a>
                                                </div>
                                                <div class="d-flex align-items-center" role="group">

                                                    <c:if test="${loggedUser != null}">
                                                        <div class=" m-1 h-50 max-h-40px d-flex justify-content-center align-items-center" >
                                                            <img style="width: 24px !important;" class="bookmark" onclick="handleBookmarkClick(this)" class="svg-btn svg-bookmark" src="<c:url value="/resources/bookmark${saved ? '-clicked' : ''}.svg"/>" alt="" url="<c:url value="/news/${article.newsId}/save"/>">
                                                        </div>
                                                    </c:if>
<%--                                                    <button type="button" class="btn btn-sm btn-outline-primary m-1 h-75 max-h-40px"><svg class="h-75" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" fill="none"><path fill="currentColor" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 4H5a2 2 0 0 0-2 2v15l3.467-2.6a2 2 0 0 1 1.2-.4H19a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2z"></path></svg></button>--%>
                                                </div>
                                            </div>
                                        </div>

                                        <c:if test="${article.hasImage()}">

                                        <div class="bg-secondary position-relative w-40">


                                                <img src="<c:url value="/news/${article.imageId}/image"/>" class="object-fit-cover" alt="...">
<%--                                                <c:if test="${!article.hasImage()}">--%>
<%--                                                    <img src="<c:url value="/resources/stock_photo.webp"/>" class="object-fit-cover" alt="...">--%>
<%--                                                </c:if>--%>
                                            </div>
                                        </c:if>

                                    </div>
                                </div>
                            </c:forEach>

                        </div>

                    </div>
                    </c:if>
                <c:if test="${type=='creator'  && empty usersPage.content}" >
                    <div class="h-75 d-flex flex-column justify-content-center align-items-center flex-grow-1 mt-5">
                        <h2 class="fw-normal"><spring:message code="home.emptyCategory.sorry"/></h2>
                            <%--                    <p class="fs-1"> <span class="text-info font-weight-bold">Oops!</span> </p>--%>
                        <p class="lead">
                            <c:if test="${ query != ''}">
                                <spring:message code="search.notFound"/> "<c:out value="${query}"/>"
                            </c:if>

                        </p>
                    </div>
                </c:if>
                <c:if test="${type=='creator' && not empty usersPage.content}">

                    <div class="container-fluid">
                        <div class="row row-cols-1 row-cols-md-3">
                            <c:forEach var="user" items="${usersPage.content}">
                                <div class="col mb-4">
                                    <a class="link" href="<c:url value="/profile/${user.id}"/>">

                                    <div class="card h-100 d-flex flex-row" >
                                        <c:set var="positivityStats" value="${user.positivityStats}"/>

                                        <c:set var="positivity" value="${positivityStats.positivity}"/>
                                        <div class="quality-indicator <c:out value="${positivity}"/>" data-toggle="tooltip" data-placement="top" title="<spring:message code="home.upvotes" arguments="${positivityStats.getPercentageUpvoted()}"/> - <spring:message code="home.interactions" arguments="${positivityStats.getInteractions()}"/>" >
                                        </div>
                                        <div class="d-flex justify-content-between p-2 w-100">
                                            <div class="d-flex align-items-center w-auto gap-1">
                                                <div class="img-container-article">
                                                    <c:if test="${user.hasImage()}">
                                                        <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/profile/${user.imageId}/image"/>" alt="">
                                                    </c:if>
                                                    <c:if test="${!user.hasImage()}">
                                                        <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/resources/profile-image.png"/>" alt="">
                                                    </c:if>
                                                </div>
                                                    <div class="link-text card-name-text text-ellipsis-1">${user}</div>

                                            </div>
                                        </div>


                                    </div>
                                    </a>

                                </div>
                            </c:forEach>

                        </div>

                    </div>
                </c:if>



            </div>
            <div class="card container w-100 w-xl-25 p-4 h-auto m-2 h-fit align-self-xl-start" id="none_shadow">

                <h5 style="color: white; padding-left: 35px; background-image: url('<c:url value="/resources/crown-svgrepo-com.svg"/>'); background-repeat: no-repeat; background-position: left center; background-size: 10%;" class="card-title"><spring:message code="home.topCreators"/></h5>


                <c:forEach var="creator" items="${topCreators}">
                    <a style="text-decoration: none" class="m-1 link" href="<c:url value="/profile/${creator.id}"/>" >
                            <div class="card text-white d-flex flex-row p-2 creator-card align-items-center" id="none_shadow_creator">
<div class="img-container">
<c:if test="${creator.hasImage()}">
    <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/profile/${creator.imageId}/image"/>" alt="">

</c:if>
    <c:if test="${!creator.hasImage()}">
        <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/resources/profile-image.png"/>" alt="">

    </c:if>
</div>
                                <div class="mx-2 text-ellipsis-1 link-text"><c:out value="${creator.username != null ? creator.username : creator.email}"/></div>
                            </div>
                    </a>
                </c:forEach>

            </div>
        </div>
    </div>
    <c:if test="${not empty newsPage && not empty newsPage.content || not empty usersPage && not empty usersPage.content}">
        <c:set var="page" value="${empty newsPage ? usersPage : newsPage}"/>
        <nav class="d-flex justify-content-center align-items-center">
            <ul class="pagination" >

                <li class="page-item"><a class="page-link" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "1"/>
            <c:param name = "query" value = "${param.query}"/>
             <c:param name = "type" value = "${param.type}"/>
             <c:param name = "category" value = "${param.category}"/>
            </c:url>"><spring:message code="home.pagination.first"/></a></li>


                <c:forEach var = "i" begin = "${page.minPage}" end = "${page.maxPage}">
                    <li class="page-item"><a class="page-link ${i == page.currentPage ? 'font-weight-bold' : ''}" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "${i}"/>
            <c:param name = "query" value = "${param.query}"/>
                         <c:param name = "type" value = "${param.type}"/>
                         <c:param name = "category" value = "${param.category}"/>

            </c:url>"><c:out value="${i}"/></a></li>
                </c:forEach>

                <li class="page-item"><a class="page-link" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "${page.totalPages}"/>
            <c:param name = "query" value = "${param.query}"/>
            <c:param name = "type" value = "${param.type}"/>
            <c:param name = "category" value = "${param.category}"/>

            </c:url>"><spring:message code="home.pagination.last"/></a></li>

            </ul>
        </nav>
    </c:if>
</div>

<!-- Principal card modal-->
<div class="modal fade" id="cardModal" tabindex="-1" aria-labelledby="cardModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="cardModalLabel"><spring:message code="home.modal.signIn"/> </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <c:url value="/login?home=true" var="loginUrl" >
                    <c:param name="redirectTo" value="/${orderBy}?category=${category}&query=${query}"/>
                    </c:url>
                <form action="${loginUrl}" method="post">
                    <div >
                        <label for="username" class="sr-only"><spring:message code="login.mail.address" var="mailAddressMsg"/></label>
                        <input type="text" id="username" name="username" class="form-control" placeholder="${mailAddressMsg}" required="" autofocus="">

                    </div>
                    <div style="margin-top: 2%" >
                        <label for="password" class="sr-only"><spring:message code="login.password" var="passwordMsg"/></label>
                        <input name="password" type="password" id="password" class="form-control" placeholder="${passwordMsg}">
                    </div>

                    <c:if test="${param.error}">
                        <div class="text-danger text-nowrap form-text d-inline-block">
                            <spring:message code="login.error"/>
                        </div>
                    </c:if>
                    <c:if test="${param.unable}">
                        <div class="text-danger text-nowrap form-text d-inline-block">
                            <spring:message code="login.emailNotVerified"/>
                        </div>
                        <div class="text-danger text-nowrap form-text d-inline-block">
                            <spring:message code="login.emailResended"/>
                        </div>
                    </c:if>
                    <button class="btn btn-lg btn-info btn-block" type="submit"><spring:message code="login.signIn"/></button>
                </form>

            </div>
        </div>
    </div>
</div>
</body>
</html>