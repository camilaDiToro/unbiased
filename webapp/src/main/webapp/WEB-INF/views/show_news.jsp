<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@include file="../../resources/head.jsp" %>
<script src="<c:url value="/resources/upvote-script.js"/>"></script>

<link href="<c:url value="/resources/custom.css"/>" rel="stylesheet">
<body>

<%@include file="../../resources/navbar.jsp" %>
<div style="position: absolute ; margin-left: 4%; margin-top: 2%">
    <a href="../TOP">
        <input type="image" src="<c:url value="/resources/images/back_to_prev.png"/>" alt="..." style="max-width: 7%; max-height: 7%">
    </a>
</div>

<c:set var="news" value="${fullNews.news}"/>


<div class="d-flex align-items-center justify-content-center w-100 py-4">
    <div class="h-auto w-75">
        <div class="d-flex align-items-center  ">
            <div class="position-absolute w-10 d-flex flex-column align-items-center m-3" news-id="<c:out value="${news.newsId}"/>">
                <img url="<c:url value = "/change-upvote"/>" id="upvote" onclick="handleClick(this)" class="svg-btn" src="<c:url value="/resources/upvote${rating.toString() == 'upvoted'? '-clicked' : ''}.svg"/>"/>

                <div id="rating" class="${rating.toString()}"><c:out value="${fullNews.upvotes}"/></div>
                <img id="downvote" url="<c:url value = "/change-downvote"/>" onclick="handleClick(this)" class="svg-btn" src="<c:url value="/resources/downvote${rating.toString() == 'downvoted' ? '-clicked' : ''}.svg"/>"/>

            </div>

                <h1 class="text-xl-center mx-auto max-w-75 text-ellipsis-1 m-3"><c:out value="${news.title}"/></h1>


        </div>
        <hr/>
        <c:if test="${news.hasImage()}">
            <img src="<c:url value="/news/${news.imageId}/image"/>" class="w-50 m-4 rounded mx-auto d-block img-thumbnail"/>

        </c:if>
        <div class="d-flex align-items-center justify-content-between">
            <div class="d-flex gap-1 align-items-center justify-content-center">
                <div class="d-flex align-items-center justify-content-center">
                    <h4 class="text-lg-left mb-0"><c:out value="${news.subtitle}"/></h4>

                </div>
                <c:if test="${user != null}">
                    <div class=" m-1 news-bookmark d-flex justify-content-center align-items-center" >
                        <img onclick="handleBookmarkClick(this)" class="w-100 h-100 svg-btn" src="<c:url value="/resources/bookmark${saved ? '-clicked' : ''}.svg"/>" alt="" url="<c:url value="/news/${news.newsId}/save"/>">
                    </div>
                </c:if>
            </div>

            <div class="progress w-25" data-toggle="tooltip" data-placement="top" title="<spring:message code="${fullNews.positivity.getInterCode()}"/>">
                <div class="progress-bar progress-bar-striped <c:out value="${fullNews.positivity}"/>" role="progressbar" style="width: ${fullNews.positiveValue*100}%"  aria-valuemin="0" aria-valuemax="100"></div>
            </div>
        </div>
        <p class="text-sm-left text-secondary"><c:out value="${date}"/>&nbsp · &nbsp<c:out value="${fullNews.readTime}"/> min read</p>

            <div class="w-fit">
                <a href="<c:url value="/profile/${user.id}"/>" class="w-fit">
                    <div class="card w-fit d-flex flex-row align-items-center p-2 gap-1">

                <div class="img-container-article">
                    <c:if test="${user.hasImage()}">
                        <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/profile/${user.imageId}/image"/>" alt="">

                    </c:if>
                    <c:if test="${!user.hasImage()}">
                        <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/resources/profile-image.png"/>" alt="">

                    </c:if>
                </div>
                <b><c:out value="${user.username != null ? user.username : user.email}"/></b>
                    </div>
                </a>
            </div>
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

        <div class="d-flex w-100 justify-content-center align-items-center">
            <div class="article-body">
                <c:out value="${news.body}" escapeXml="false"/>
            </div>
        </div>



    </div>
</div>


</body>
</html>