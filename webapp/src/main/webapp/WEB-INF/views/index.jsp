<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<c:set var="pageTitle" scope="request" value="${pageTitle}"/>
<%@ include file="../../resources/head.jsp" %>
<body>

<div class="d-flex h-100 flex-column">
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
                <a href="./create_article">
                    <button type="button" class="btn btn-info">
                        <spring:message code="home.createArticle.button"/>
                    </button></a>
            </li>


        </ul>

        <div class="d-flex flex-column flex-md-row">
                <div class="w-75">
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
                    <c:if test="${empty newsMap}" >
                        <div class="h-75 d-flex flex-column justify-content-center align-items-center flex-grow-1">
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
                    <c:if test="${!empty newsMap}">

                    <div class="container-fluid">
                        <div class="row row-cols-1 row-cols-md-2">
                            <c:set var="maxLength" value="${100}"/>
                            <c:forEach var="article" items="${newsMap.keySet()}">



                                <div class="col mb-4">
                                    <c:set var="upvoted" value="${article.body.length() % 3 == 0}"/>
                                    <c:set var="downvoted" value="${article.body.length() % 3 == 1}"/>
                                    <div class="card h-100 d-flex flex-row" >

                                        <div class="d-flex flex-column justify-content-between w-60">
                                            <div class="d-flex w-100">
                                                <div class="w-10 d-flex flex-column align-items-center m-3 ">
                                                    <svg class="svg-btn" fill="${upvoted ? '#ff4500' : '#5d696a'}" width="24px" height="24px" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">            <path d="M4 14h4v7a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1v-7h4a1.001 1.001 0 0 0 .781-1.625l-8-10c-.381-.475-1.181-.475-1.562 0l-8 10A1.001 1.001 0 0 0 4 14z"></path>
                                                    </svg>
                                                    <div class="${upvoted ? 'upvoted' : (downvoted ? 'downvoted' : '')}">25</div>
                                                    <svg class="svg-btn" fill="${downvoted ? '#4e76db' : '#5d696a'}" width="24px" height="24px" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="M20.901 10.566A1.001 1.001 0 0 0 20 10h-4V3a1 1 0 0 0-1-1H9a1 1 0 0 0-1 1v7H4a1.001 1.001 0 0 0-.781 1.625l8 10a1 1 0 0 0 1.562 0l8-10c.24-.301.286-.712.12-1.059z"></path></svg>
                                                </div>
                                                <div class="card-body-home">
                                                    <span class="badge badge-pill badge-primary m-1">Messi</span> <span class="badge badge-pill badge-primary">Messi</span>
                                                    <a style="max-height: 10%" href="<c:url value="/news/${article.newsId}"/>"><h5 class="text-ellipsis"><c:out value="${article.title}"/></h5></a>
                                                    <h6 class="card-subtitle py-1 text-ellipsis"><c:out value="${article.subtitle}"/></h6>
                                                    <p class="text-sm-left text-secondary mb-0"><c:out value="${newsMap.get(article)}"/> min read</p>
                                                        <%--                                    <p class="card-text"><c:out value="${fn:substring(article.body, 0, maxLength)}${fn:length(article.body) > maxLength ? '...' : ''}"/></p>--%>

                                                </div>
                                            </div>
                                            <div class="d-flex justify-content-between p-2 w-100">
                                                <div class="d-flex align-items-center w-auto">
                                                    <img class="rounded-circle w-25 object-cover mr-1" src="<c:url value="/resources/stock_photo.webp"/>" alt="">
                                                    <div class="text-secondary card-name-text text-ellipsis-1">Nombre Apellido</div>
                                                </div>
                                                <div class="d-flex align-items-center" role="group">

                                                    <button type="button" class="btn btn-sm btn-outline-primary m-1 h-75 max-h-40px">
                                                        <svg class="h-75" version="1.1" id="Capa_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px"
                                                             viewBox="0 0 212.045 212.045" fill="currentColor" stroke="currentColor" xml:space="preserve">
<path d="M167.871,0H44.84C34.82,0,26.022,8.243,26.022,18v182c0,3.266,0.909,5.988,2.374,8.091c1.752,2.514,4.573,3.955,7.598,3.954
	c2.86,0,5.905-1.273,8.717-3.675l55.044-46.735c1.7-1.452,4.142-2.284,6.681-2.284c2.538,0,4.975,0.832,6.68,2.288l54.86,46.724
	c2.822,2.409,5.657,3.683,8.512,3.683c4.828,0,9.534-3.724,9.534-12.045V18C186.022,8.243,177.891,0,167.871,0z"/>
                                                            <g>
                                                            </g>
                                                            <g>
                                                            </g>
                                                            <g>
                                                            </g>
                                                            <g>
                                                            </g>
                                                            <g>
                                                            </g>
                                                            <g>
                                                            </g>
                                                            <g>
                                                            </g>
                                                            <g>
                                                            </g>
                                                            <g>
                                                            </g>
                                                            <g>
                                                            </g>
                                                            <g>
                                                            </g>
                                                            <g>
                                                            </g>
                                                            <g>
                                                            </g>
                                                            <g>
                                                            </g>
                                                            <g>
                                                            </g>
</svg>
                                                    </button>
                                                    <button type="button" class="btn btn-sm btn-outline-primary m-1 h-75 max-h-40px"><svg class="h-75" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" fill="none"><path fill="currentColor" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 4H5a2 2 0 0 0-2 2v15l3.467-2.6a2 2 0 0 1 1.2-.4H19a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2z"></path></svg></button>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="bg-secondary position-relative w-40">
                                            <div class="quality-indicator" data-toggle="tooltip" data-placement="top" title="Overall positive ratings">
                                            </div>
                                            <img src="<c:url value="/resources/stock_photo.webp"/>" class="object-fit-cover" alt="...">


                                        </div>




                                    </div>
                                </div>
                            </c:forEach>

                        </div>

                    </div>
                    </c:if>


                </div>
            <div class="card container w-25 w-md-25 p-4 h-auto m-2 h-fit">
                <h3 class="card-title">Top creators</h3>

                <c:forEach var="creator" items="${topCreators}">
                    <a class="m-1" href="<c:url value="/profile/${creator.id}"/>">
                            <div class="card bg-primary text-white d-flex flex-row p-2 creator-card align-items-center">
<div class="img-container">
    <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/resources/stock_photo.webp"/>" alt="">

</div>
                                <div class="card-body">${creator.username != null ? creator.username : creator.email}</div>
                            </div>
                    </a>
                </c:forEach>

            </div>
        </div>
    </div>
    <c:if test="${not empty newsMap}">
        <nav class="d-flex justify-content-center align-items-center">
            <ul class="pagination">

                <li class="page-item"><a class="page-link" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "1"/>
            <c:param name = "query" value = "${param.query}"/>
            </c:url>"><spring:message code="home.pagination.first"/></a></li>


                <c:forEach var = "i" begin = "${minPage}" end = "${maxPage}">
                    <li class="page-item"><a class="page-link ${i == page ? 'font-weight-bold' : ''}" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "${i}"/>
            <c:param name = "query" value = "${param.query}"/>
            </c:url>"><c:out value="${i}"/></a></li>
                </c:forEach>

                <li class="page-item"><a class="page-link" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "${totalPages}"/>
            <c:param name = "query" value = "${param.query}"/>
            </c:url>"><spring:message code="home.pagination.last"/></a></li>

            </ul>
        </nav>
    </c:if>
</div>
</body>
</html>