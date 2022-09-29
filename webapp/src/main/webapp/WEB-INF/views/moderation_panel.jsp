<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html>
<c:set var="pageTitle" scope="request" value="${pageTitle}"/>
<%@ include file="../../resources/head.jsp" %>
<script src="<c:url value="/resources/upvote-script.js"/>"></script>
<body>
<c:set var="news" value="${newsPage.content}"/>
<div class="d-flex h-100 flex-column">


    <%@ include file="../../resources/navbar.jsp" %>
    <div class="d-flex flex-column h-100">
        <div class="flex-grow-1 d-flex flex-row">

            <%--LEFT SIDE--%>
                <%@ include file="../../resources/moderation-left-side.jsp" %>

            <%--RIGHT SIDE--%>
            <div class="d-flex flex-column w-75">

                <%--TAB (top, new)--%>
                <div class="tab">
                    <c:set var = "activeClasses" scope = "session" value = "bg-info active"/>
                    <c:set var = "inactiveClasses" scope = "session" value = "text-secondary"/>
                    <ul class="my-4 nav bg-transparent nav-pills text-light p-2 rounded-lg d-flex ">
                        <c:forEach var="order" items="${orders}">
                            <li class="nav-item">
                                <a class="text-capitalize nav-link rounded-pill <c:out value = "${orderBy == order ? activeClasses : inactiveClasses}"/>" aria-current="page" href="<c:url value = "/admin/reported_news/${order}">

                    </c:url>"><spring:message code="${order.interCode}"/></a>
                            </li>
                        </c:forEach>

                    </ul>
                </div>

                <%--CARDS--%>
                <div class="tab">
                    <c:if test="${empty news}" >
                        <div class="h-75 d-flex flex-column justify-content-center align-items-center flex-grow-1 mt-5">

                        </div>
                    </c:if>

                    <c:if test="${!empty news}">

                        <div class="container-fluid">
                            <div class="row row-cols-1">
                                <c:set var="maxLength" value="${100}"/>
                                <c:forEach var="reportedNews" items="${news}">
                                    <c:set var="article" value="${reportedNews.news}"/>

                                    <c:set var="newsId" value="${article.newsId}"/>
                                <c:set var="creator" value="${reportedNews.newsOwner}"/>
                                    <!-- Modal -->
                                    <div class="modal fade" id="binModal${newsId}" tabindex="-1" aria-labelledby="binModalLabel" aria-hidden="true">
                                        <div class="modal-dialog modal-dialog-centered">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="binModalLabel"><spring:message code="profile.modal.question"/></h5>
                                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                        <span aria-hidden="true">&times;</span>
                                                    </button>
                                                </div>
                                                <div class="modal-body">
                                                    <spring:message code="profile.modal.msg"/>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code="profile.modal.cancel"/></button>
                                                    <form method="post" action="<c:url value="/admin/reported_news/${newsId}/delete"/>">
                                                        <button type="submit" class="btn btn-primary"><spring:message code="profile.modal.accept"/></button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col mb-4">
                                        <div class="card h-100 d-flex flex-row p-3" id="left-card">

                                            <div>
                                                <span class="reports-indicator badge badge-pill badge-danger d-flex align-items-center justify-content-center report-count" >
                                                    <c:out value="${reportedNews.reportCount}"/>
                                            </span>
                                            </div>

                                            <div class="d-flex flex-column justify-content-between w-100">
                                                <div class="d-flex w-100 ">
                                                    <div class="card-body-home pt-0">
                                                        <a href="<c:url value="/news/${article.newsId}"/>" class="link mh-10"><h5 class="text-ellipsis link-text"><c:out value="${article.title}"/></h5></a>
                                                        <h6 class="card-subtitle py-1 text-ellipsis-2"><c:out value="${article.subtitle}"/></h6>
                                                    </div>
                                                </div>
                                                <div class="d-flex justify-content-between w-100">
                                                    <div class="d-flex align-items-center w-auto gap-1">
                                                        <div class="img-container-article">
                                                            <c:if test="${creator.hasImage()}">
                                                                <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/profile/${creator.imageId}/image"/>" alt="">
                                                            </c:if>
                                                            <c:if test="${!creator.hasImage()}">
                                                                <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/resources/profile-image.png"/>" alt="">
                                                            </c:if>
                                                        </div>
                                                        <a href="<c:url value="/profile/${article.creatorId}"/>" class="link">
                                                            <div class="card-name-text text-ellipsis-1 link-text">${creator}</div>
                                                        </a>
                                                    </div>
                                                    <div class="d-flex align-items-center mr-2" role="group">

                                                             <button data-toggle="modal" data-target="#binModal${newsId}" class="btn bin-modal">
                                                                <img src="<c:url value="/resources/bin-svgrepo-com.svg" />" alt="..." class="bin-image"/>
                                                            </button>
                                                            <a   class="font-weight-bold hover-hand link" href="<c:url value="/admin/reported_news_detail/${newsId}"/>">
                                                                <div class="link-text">
                                                                    <spring:message code="moderation.details"/>
                                                                </div>
                                                            </a>

                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                        </div>
                    </c:if>
                </div>

            </div>


        </div>

        <c:if test="${not empty news}">
            <nav class="d-flex justify-content-center align-items-center">
                <ul class="pagination">
                    <li class="page-item"><a class="page-link" href="<c:url value = "/admin/reported_news/${newsOrder}">
                        <c:param name = "page" value = "1"/>
                        </c:url>"><spring:message code="home.pagination.first"/></a></li>


                    <c:forEach var = "i" begin = "${newsPage.minPage}" end = "${newsPage.maxPage}">
                        <li class="page-item"><a class="page-link ${i == newsPage.currentPage ? 'font-weight-bold' : ''}" href="<c:url value = "/admin/reported_news/${newsOrder}">
                        <c:param name = "page" value = "${i}"/>
                        </c:url>"><c:out value="${i}"/></a></li>
                    </c:forEach>

                    <li class="page-item"><a class="page-link" href="<c:url value = "/admin/reported_news/${newsOrder}">
                        <c:param name = "page" value = "${newsPage.totalPages}"/>
                        </c:url>"><spring:message code="home.pagination.last"/></a></li>
                </ul>
            </nav>
        </c:if>


    </div>
</div>
</body>
</html>