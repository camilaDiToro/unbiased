<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<c:set var="pageTitle" scope="request" value="${news.title}"/>
<%@include file="../../resources/head.jsp" %>
<link href="<c:url value="/resources/custom.css"/>" rel="stylesheet">
<body>
<%@include file="../../resources/navbar.jsp" %>
<div style="position: absolute ; margin-left: 4%; margin-top: 2%">
    <a href="../TOP">
        <input type="image" src="<c:url value="/resources/images/back_to_prev.png"/>" alt="..." style="max-width: 7%; max-height: 7%">
    </a>
</div>

<div class="d-flex align-items-center justify-content-center w-100 py-4">
    <div class="h-auto w-75">
        <div class="d-flex align-items-center  ">
            <div class="w-10 d-flex flex-column align-items-center m-3 position-absolute">
                <svg class="svg-btn" fill="${upvoted ? '#ff4500' : '#5d696a'}" width="24px" height="24px" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">            <path d="M4 14h4v7a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1v-7h4a1.001 1.001 0 0 0 .781-1.625l-8-10c-.381-.475-1.181-.475-1.562 0l-8 10A1.001 1.001 0 0 0 4 14z"></path>
                </svg>
                <div class="${upvoted ? 'upvoted' : (downvoted ? 'downvoted' : '')}">25</div>
                <svg class="svg-btn" fill="${downvoted ? '#4e76db' : '#5d696a'}" width="24px" height="24px" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="M20.901 10.566A1.001 1.001 0 0 0 20 10h-4V3a1 1 0 0 0-1-1H9a1 1 0 0 0-1 1v7H4a1.001 1.001 0 0 0-.781 1.625l8 10a1 1 0 0 0 1.562 0l8-10c.24-.301.286-.712.12-1.059z"></path></svg>
            </div>
            <h1 class="text-xl-center mx-auto max-w-75 text-ellipsis-1 m-3"><c:out value="${news.title}"/></h1>
        </div>
        <hr/>
        <img src="<c:url value="/resources/stock_photo.webp"/>" class="w-50 m-4 rounded mx-auto d-block img-thumbnail"/>
        <div class="d-flex align-items-center justify-content-between">
            <h4 class="text-lg-left"><c:out value="${news.subtitle}"/></h4>

            <div class="progress w-25" data-toggle="tooltip" data-placement="top" title="Overall positive ratings"">
                <div class="progress-bar progress-bar-striped bg-success" role="progressbar" style="width: 30%" aria-valuenow="30" aria-valuemin="0" aria-valuemax="100"></div>
            </div>
        </div>
        <p class="text-sm-left text-secondary"><c:out value="${date}"/>&nbsp · &nbsp<c:out value="${timeToRead}"/> min read</p>


        <a href="<c:url value="/profile/${user.id}"/> ">
            <div class="card w-fit d-flex flex-row align-items-center p-2 gap-1">
                <div class="img-container-article">
                    <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/resources/stock_photo.webp"/>" alt="">
                </div>
                <b><c:out value="${user.username != null ? user.username : user.email}"/></b>
            </div>
        </a>
        <div class="w-50 d-flex flex-wrap align-items-center gap-1 mt-3">
            <div class="text-sm-left font-weight-bold">
                Categories:
            </div>
            <c:forEach var="category" items="${categories}">

                <span class="badge badge-pill badge-info"><spring:message code="${category.interCode}"/></span>

            </c:forEach>
        </div>

        <div class="w-50 d-flex flex-wrap align-items-center gap-1">
            <div class="text-sm-left font-weight-bold">
                Tags:
            </div>
            <span class="badge badge-pill badge-primary">Messi</span>
        </div>

<%--        <p class="article-body"><c:out value="${news.body}"/></p>--%>

        <div class="d-flex w-100 justify-content-center align-items-center">
            <div class="article-body">
                <c:out value="${news.body}" escapeXml="false"/>
            </div>
        </div>

<%--        <c:if test="${news.hasImage()}">--%>
<%--        <img src="<c:url value="/news/${newsId}/image"/>" class="user-section-img"/>--%>
<%--        </c:if>--%>

    </div>
</div>


</body>
</html>