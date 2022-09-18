<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<c:set var="pageTitle" scope="request" value="Login"/>
<c:set var="signInOrCreate" scope="request" value="${false}"/>
<%@include file="../../resources/head.jsp" %>
<script src="<c:url value="/resources/upvote-script.js"/>"></script>
<%--<body>
<%@include file="../../resources/navbar.jsp" %>
<h2><spring:message code="views.profile.title" arguments="${user.email}" htmlEscape="true"/></h2>
<h4><spring:message code="views.profile.desc" arguments="${user.id}" htmlEscape="true"/></h4>
<h4>${user.username}</h4>

<c:if test="${user.imageId!=null}">
    <img src="<c:url value="/profile/${user.imageId}/image"/>" class="user-section-img"/>
</c:if>

<form:form modelAttribute="userProfileForm" method="post" enctype="multipart/form-data" acceptCharset="utf-8">
    <div>
        <form:label path="image">
            insertar imagen
        </form:label>
        <form:input type="file" path="image" accept="image/png, image/jpeg" />
        <form:errors path="image" element="p" cssStyle="color:red"/>
    </div>
    <div>
        <form:errors path="username" element="p" cssStyle="color: red"/>
        <form:label path="username">Username:</form:label>
        <form:input type="text" path="username"/>
    </div>
    <button style="border-radius:4px; padding:4px; font-size: 18px; margin-top: 5px">guardar</button>
</form:form>
</body>--%>

<link href="<c:url value="/resources/profile.css"/>" rel="stylesheet">
<body>
<c:set var="news" value="${newsPage.content}"/>

<%--<%@include file="../../resources/navbar.jsp" %>--%>
<div class="d-flex h-100 flex-column">
    <c:set var="loggedUser" scope="request" value="${user}"/>
    <c:set var="userProfile" scope="request" value="${profileUser}"/>
<%@include file="../../resources/navbar.jsp" %>

<%--TAB (publicaciones, guardado, upvoteado, downvoteado)--%>
<div>
    <ul class="my-2 nav nav-tabs justify-content-center text-light p-2">
        <c:forEach var="cat" items="${categories}">
            <li class="nav-item">
                <a class="text-capitalize nav-link <c:out value = "${category == cat ? 'active': ''}"/>" aria-current="page" href="<c:url value = "/profile/${profileUser.id}/${orderBy}">
                    <c:param name = "category" value = "${cat}"/>
                    </c:url>"><spring:message code="${cat.interCode}"/></a>
            </li>
        </c:forEach>
    </ul>
</div>

<div class="d-flex flex-column h-100">
    <div class="flex-grow-1 d-flex flex-row">
        <%--LEFT SIDE--%>
        <div style="display: flex; width: 70%; flex-direction: column">

            <%--TAB (top, new)--%>
            <div style="display: flex; flex-direction: column; width: 85%; margin: 0 auto ">
                <c:set var = "activeClasses" scope = "session" value = "bg-secondary active"/>
                <c:set var = "inactiveClasses" scope = "session" value = "text-secondary"/>
                <ul class="my-4 nav bg-primary nav-pills text-light p-2 rounded-lg d-flex ">
                    <c:forEach var="order" items="${orders}">
                        <li class="nav-item">
                            <a class="text-capitalize nav-link rounded-pill <c:out value = "${orderBy == order ? activeClasses : inactiveClasses}"/>" aria-current="page" href="<c:url value = "/profile/${profileUser.id}/${order}">
                    <c:param name = "category" value = "${category}"/>
                    </c:url>"><spring:message code="${order.interCode}"/></a>
                        </li>
                    </c:forEach>
<%--                    <li class="nav-item ml-auto">--%>
<%--                        <c:if test="${user != null}">--%>
<%--                            <a href="./create_article">--%>
<%--                                <button type="button" class="btn btn-info">--%>
<%--                                    <spring:message code="home.createArticle.button"/>--%>
<%--                                </button></a>--%>
<%--                        </c:if>--%>
<%--                    </li>--%>
                </ul>
            </div>

            <%--CARDS--%>
            <div style="display: flex; flex-direction: column; width: 85%; margin: 0 auto ">
                <c:if test="${empty news}" >
                    <div class="h-75 d-flex flex-column justify-content-center align-items-center flex-grow-1 mt-5">
                        <h2 class="fw-normal"><spring:message code="home.emptyCategory.sorry"/></h2>
                            <%--                    <p class="fs-1"> <span class="text-info font-weight-bold">Oops!</span> </p>--%>
                        <p class="lead">
                            <spring:message code="categories.notFound"/> "<spring:message code="${category.interCode}"/>"
                        </p>
                    </div>
                </c:if>
                <c:if test="${!empty news}">

                    <div class="container-fluid">
                        <div class="row row-cols-1">
                            <c:set var="maxLength" value="${100}"/>
                            <c:forEach var="fullNews" items="${news}">
                                <c:set var="article" value="${fullNews.news}"/>

                                <c:set var="newsId" value="${article.newsId}"/>


                                <div class="col mb-4">
                                    <div class="card h-100 d-flex flex-row" id="left-card">

                                        <div class="d-flex flex-column justify-content-between w-60">
                                            <div class="d-flex w-100">
                                                <div class="w-10 d-flex flex-column align-items-center m-3" news-id="<c:out value="${article.newsId}"/>">
                                                    <c:set var="rating" value="${ratingMap.get(newsId)}"/>

                                                    <img url="<c:url value = "/change-upvote"/>" id="upvote" onclick="handleClick(this)" class="svg-btn" src="<c:url value="/resources/upvote${rating.toString() == 'upvoted'? '-clicked' : ''}.svg"/>"/>
                                                    <div id="rating" class="${rating.toString()}"><c:out value="${fullNews.upvotes}"/></div>
                                                    <img id="downvote" url="<c:url value = "/change-downvote"/>" onclick="handleClick(this)" class="svg-btn" src="<c:url value="/resources/downvote${rating.toString() == 'downvoted' ? '-clicked' : ''}.svg"/>"/>

                                                </div>
                                                <div class="card-body-home">
                                                    <span class="badge badge-pill badge-primary m-1">Messi</span> <span class="badge badge-pill badge-primary">Messi</span>
                                                    <a style="max-height: 10%" href="<c:url value="/news/${article.newsId}"/>"><h5 class="text-ellipsis"><c:out value="${article.title}"/></h5></a>
                                                    <h6 class="card-subtitle py-1 text-ellipsis"><c:out value="${article.subtitle}"/></h6>
                                                    <p class="text-sm-left text-secondary mb-0"><c:out value="${fullNews.readTime}"/> <spring:message code="home.read"/></p>
                                                        <%--                                    <p class="card-text"><c:out value="${fn:substring(article.body, 0, maxLength)}${fn:length(article.body) > maxLength ? '...' : ''}"/></p>--%>

                                                </div>
                                            </div>
                                            <div class="d-flex justify-content-between p-2 w-100">
                                                <div class="d-flex align-items-center w-auto gap-1">
                                                    <div class="img-container-article">
                                                        <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/resources/stock_photo.webp"/>" alt="">
                                                    </div>
                                                    <a href="<c:url value="/profile/${article.creatorId}"/>">
                                                        <div class="text-secondary card-name-text text-ellipsis-1">${fullNews.user}</div>

                                                    </a>
                                                </div>
                                                <div class="d-flex align-items-center" role="group">

                                                    <c:if test="${user != null}">
                                                        <div class=" m-1 h-50 max-h-40px d-flex justify-content-center align-items-center" >
                                                            <img onclick="handleBookmarkClick(this)" class="w-100 h-100 svg-btn" src="<c:url value="/resources/bookmark${savedMap.get(newsId) ? '-clicked' : ''}.svg"/>" alt="" url="<c:url value="/news/${article.newsId}/save"/>">
                                                        </div>
                                                    </c:if>
                                                        <%--                                                    <button type="button" class="btn btn-sm btn-outline-primary m-1 h-75 max-h-40px"><svg class="h-75" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" fill="none"><path fill="currentColor" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 4H5a2 2 0 0 0-2 2v15l3.467-2.6a2 2 0 0 1 1.2-.4H19a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2z"></path></svg></button>--%>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="bg-secondary position-relative w-40" style="border-radius: 15px">
                                            <c:set var="positivity" value="${fullNews.positivity}"/>
                                            <div class="quality-indicator <c:out value="${positivity}"/>" data-toggle="tooltip" data-placement="top" title="<spring:message code="${positivity.getInterCode()}"/>">
                                            </div>
                                            <c:if test="${article.hasImage()}">

                                                <img src="<c:url value="/news/${article.imageId}/image"/>" class="object-fit-cover" alt="...">
                                            </c:if>

                                            <c:if test="${!article.hasImage()}">
                                                <img src="<c:url value="/resources/stock_photo.webp"/>" class="object-fit-cover" alt="..." >
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                    </div>
                </c:if>
            </div>

        </div>
        <%--RIGHT SIDE--%>
        <div style="display: flex; width: 30%; justify-content: center;">
        <div class="card" style="width: 18rem; height: 12rem; margin-top: 4%" id="right-card">
            <img src="<c:url value="/resources/front-page-profile.png"/>" class="card-img-top" alt="...">
            <div class="card-body">
                <h4 class="mb-0 card-title text-center"><c:out value="${profileUser.username}"/> </h4>
                <span class="card-text text-muted d-block mb-2 text-center"><c:out value="${profileUser.email}"/> </span>
            </div>
        </div>

        <div class="profile">
            <img src="<c:url value="/resources/profile-image.png"/>" class="rounded-circle" width="80">
        </div>


        <c:if test="${isMyProfile}">
            <div class="pencil-edit">
                <button style="border: none; background-color: white; outline: none" data-toggle="modal" data-target="#exampleModal">
                <span class="badge badge-pill badge-info">
                   <img src="<c:url value="/resources/pencil-edit.png"/>" alt="...">
                    Editar
                </span>
                </button>
            </div>
        </c:if>


            <!-- Modal -->
            <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel"><spring:message code="createArticle.modal.question"/></h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <c:url value="/profile/${profileUser.id}" var="postUrl"/>
                        <form:form modelAttribute="userProfileForm" enctype="multipart/form-data" action="${postUrl}" method="post">
                            <div class="modal-body">

                                    <form:label path="image">Cambiar nombre de usuario</form:label>
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text" id="basic-addon1">@</span>
                                        </div>
                                        <form:input type="text" path="username" cssClass="form-control" placeholder="username"/>
                                    </div>

                                    <form:label path="image">Cambiar imagen de perfil</form:label>
                                    <div class="input-group mb-3">
                                        <div class="custom-file">
                                            <form:input type="file" path="image" accept="image/png, image/jpeg" cssClass="custom-file-input"/>
                                            <form:label path="image" cssClass="custom-file-label" for="inputGroupFile01">Elegir imagen</form:label>
                                        </div>
                                    </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code="createArticle.modal.cancel"/></button>
                                <button type="submit" class="btn btn-primary"><spring:message code="createArticle.modal.accept"/></button>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
    </div>
    </div>

    <c:if test="${not empty news}">
        <nav class="d-flex justify-content-center align-items-center">
            <ul class="pagination">

                <li class="page-item"><a class="page-link" href="<c:url value = "/profile/${profileUser.id}/${newsOrder}">
                        <c:param name = "page" value = "1"/>
                        <c:param name = "category" value = "${param.category}"/>
                        </c:url>"><spring:message code="home.pagination.first"/></a></li>


                <c:forEach var = "i" begin = "${newsPage.minPage}" end = "${newsPage.maxPage}">
                    <li class="page-item"><a class="page-link ${i == newsPage.currentPage ? 'font-weight-bold' : ''}" href="<c:url value = "/profile/${profileUser.id}/${newsOrder}">
                        <c:param name = "page" value = "${i}"/>
                        <c:param name = "category" value = "${param.category}"/>
                        </c:url>"><c:out value="${i}"/></a></li>
                </c:forEach>

                <li class="page-item"><a class="page-link" href="<c:url value = "/profile/${profileUser.id}/${newsOrder}">
                        <c:param name = "page" value = "${newsPage.totalPages}"/>
                        <c:param name = "category" value = "${param.category}"/>
                        </c:url>"><spring:message code="home.pagination.last"/></a></li>

            </ul>
        </nav>
    </c:if>



</div>







</body>
</html>