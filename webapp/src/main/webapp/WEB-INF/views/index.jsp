<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<c:set var="pageTitle" scope="request" value="${pageTitle}"/>
<%@ include file="../../resources/head.jsp" %>
<script src="<c:url value="/resources/upvote-script.js"/>"></script>
<body>
<c:set var="news" value="${newsPage.content}"/>
<div class="d-flex h-100 flex-column">
<%--    <c:set var="loggedUser" scope="request" value="${user}"/>--%>
    <%@ include file="../../resources/navbar.jsp" %>
    <div class="container-xxl container-fluid flex-grow-1">
        <c:set var = "activeClasses" scope = "session" value = "bg-secondary active"/>
        <c:set var = "inactiveClasses" scope = "session" value = "text-secondary"/>
        <ul class="my-4 nav bg-primary nav-pills text-light p-2 rounded-lg d-flex ">
            <c:forEach var="order" items="${orders}">
                <li class="nav-item">
                    <a class="text-capitalize nav-link rounded-pill <c:out value = "${orderBy == order ? activeClasses : inactiveClasses}"/>" aria-current="page" href="<c:url value = "/${order}">
                    <c:param name = "category" value = "${category}"/>
                    <c:if test="${!empty query}"><c:param name = "query" value = "${param.query}"/></c:if>
                    </c:url>"><spring:message code="${order.interCode}"/></a>
                </li>
            </c:forEach>
            <li class="nav-item ml-auto">
                <c:if test="${user != null}">
                    <a href="./create_article">
                        <button type="button" class="btn btn-info">
                            <spring:message code="home.createArticle.button"/>
                        </button></a>
                </c:if>
            </li>


        </ul>

        <div class="d-flex flex-column flex-xl-row ">
            <div class="w-100 w-xl-75 ">
                    <c:if test="${query == ''}">
                        <ul class="my-2 nav nav-tabs justify-content-center text-light p-2">
                            <li class="nav-item">
                                <a class="text-capitalize nav-link <c:out value = "${category.toString() == 'ALL' ? 'active' : ''}"/>" aria-current="page" href="<c:url value = "/${orderBy}">
                    <c:param name = "query" value = "${param.query}"/>
                    </c:url>"><spring:message code="categories.all"/></a>
                            </li>
                            <c:forEach var="cat" items="${categories}">
                                <li class="nav-item">
                                    <a class="text-capitalize nav-link <c:out value = "${category.toString() != 'ALL' && category == cat ? 'active': ''}"/>" aria-current="page" href="<c:url value = "/${orderBy}">
                    <c:param name = "category" value = "${cat}"/>

                    </c:url>"><spring:message code="${cat.interCode}"/></a>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                    <c:if test="${query != ''}">
                        <div class="m-4">
                            <a href="<c:url value="/${orderBy}"/>"><spring:message code="search.filter"/> <c:out value=": \"${query}\""/></a>
                        </div>
                    </c:if>
                    <c:if test="${empty news}" >
                        <div class="h-75 d-flex flex-column justify-content-center align-items-center flex-grow-1 mt-5">
                            <h2 class="fw-normal"><spring:message code="home.emptyCategory.sorry"/></h2>
                                <%--                    <p class="fs-1"> <span class="text-info font-weight-bold">Oops!</span> </p>--%>
                            <p class="lead">
                                <c:if test="${query == '' && category.toString() != 'ALL'}">
                                    <spring:message code="categories.notFound"/> "<spring:message code="${category.interCode}"/>"
                                </c:if>
                                <c:if test="${ query != '' && category.toString() == 'ALL'}">
                                    <spring:message code="search.notFound"/> "<c:out value="${query}"/>"
                                </c:if>

                                <c:if test="${category.toString() == 'ALL' && query == ''}">
                                    <spring:message code="categories.notFound"/> "<spring:message code="categories.all"/>"
                                </c:if>


                            </p>
                        </div>
                    </c:if>
                    <c:if test="${!empty news}">

                    <div class="container-fluid">
                        <div class="row row-cols-1 row-cols-md-2">
                            <c:set var="maxLength" value="${100}"/>
                            <c:forEach var="fullNews" items="${news}">
                                <c:set var="article" value="${fullNews.news}"/>

                                <c:set var="newsId" value="${article.newsId}"/>


                                <div class="col mb-4">
                                    <div class="card h-100 d-flex flex-row" >

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
                                                        <c:if test="${fullNews.user.hasImage()}">
                                                            <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/profile/${fullNews.user.imageId}/image"/>" alt="">
                                                        </c:if>
                                                        <c:if test="${!fullNews.user.hasImage()}">
                                                            <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/resources/profile-image.png"/>" alt="">
                                                        </c:if>
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
                                            <div class="bg-secondary position-relative w-40">
                                                <c:set var="positivity" value="${fullNews.positivity}"/>
                                                <div class="quality-indicator <c:out value="${positivity}"/>" data-toggle="tooltip" data-placement="top" title="<spring:message code="${positivity.getInterCode()}"/>">
                                                </div>
                                                <c:if test="${article.hasImage()}">

                                                <img src="<c:url value="/news/${article.imageId}/image"/>" class="object-fit-cover" alt="...">
                                                </c:if>

                                                <c:if test="${!article.hasImage()}">

                                                    <img src="<c:url value="/resources/stock_photo.webp"/>" class="object-fit-cover" alt="...">
                                                </c:if>

                                            </div>




                                    </div>
                                </div>
                            </c:forEach>

                        </div>

                    </div>
                    </c:if>


                </div>
            <div class="card container w-100 w-xl-25 p-4 h-auto m-2 h-fit align-self-xl-start">
                <h3 class="card-title"><spring:message code="home.topCreators"/></h3>

                <c:forEach var="creator" items="${topCreators}">
                    <a class="m-1" href="<c:url value="/profile/${creator.id}"/>">
                            <div class="card bg-primary text-white d-flex flex-row p-2 creator-card align-items-center">
<div class="img-container">
<c:if test="${creator.hasImage()}">
    <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/profile/${creator.imageId}/image"/>" alt="">

</c:if>
    <c:if test="${!creator.hasImage()}">
        <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/resources/profile-image.png"/>" alt="">

    </c:if>
</div>
                                <div class="mx-2 text-ellipsis-1"><c:out value="${creator.username != null ? creator.username : creator.email}"/></div>
                            </div>
                    </a>
                </c:forEach>

            </div>
        </div>
    </div>
    <c:if test="${not empty news}">
        <nav class="d-flex justify-content-center align-items-center">
            <ul class="pagination">

                <li class="page-item"><a class="page-link" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "1"/>
            <c:param name = "query" value = "${param.query}"/>
            </c:url>"><spring:message code="home.pagination.first"/></a></li>


                <c:forEach var = "i" begin = "${newsPage.minPage}" end = "${newsPage.maxPage}">
                    <li class="page-item"><a class="page-link ${i == newsPage.currentPage ? 'font-weight-bold' : ''}" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "${i}"/>
            <c:param name = "query" value = "${param.query}"/>
            </c:url>"><c:out value="${i}"/></a></li>
                </c:forEach>

                <li class="page-item"><a class="page-link" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "${newsPage.totalPages}"/>
            <c:param name = "query" value = "${param.query}"/>
            </c:url>"><spring:message code="home.pagination.last"/></a></li>

            </ul>
        </nav>
    </c:if>
</div>
</body>
</html>