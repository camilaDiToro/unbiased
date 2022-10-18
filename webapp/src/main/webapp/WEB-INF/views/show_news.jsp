<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@include file="../../resources/jsp/head.jsp" %>
<script src="<c:url value="/resources/js/upvote-script.js"/>"></script>
<c:if test="${hasErrors}">
    <script>
        $(document).ready(function(){
            $("#reportModal").modal('show');
        });
    </script>
</c:if>
<link href="<c:url value="/resources/css/custom.css"/>" rel="stylesheet">
<body>

<%@include file="../../resources/jsp/navbar.jsp" %>




<c:set var="news" value="${fullNews}"/>
<c:set var="user" value="${fullNews.user}"/>
<c:set var="loggedParameters" value="${fullNews.loggedUserParameters}"/>
<c:set var="rating" value="${loggedParameters != null ? loggedParameters.personalRating : ''}"/>
<c:set var="positivityStats" value="${fullNews.positivityStats}"/>
<c:set var="positivity" value="${positivityStats.positivity}"/>



<a href="../TOP" class="back-button-show-news">
    <img class="svg-btn hover-hand back-btn" src="<c:url value="/resources/images/back-svgrepo-com.svg"/>" alt="..." data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.clickToGoBack"/> "/>
</a>


<div class="d-flex align-items-center justify-content-center w-100 py-4">
    <div class="h-auto w-75 d-flex flex-column ">
        <div class="d-flex align-items-center  ">
            <div class="d-flex flex-column align-items-center" news-id="<c:out value="${news.newsId}"/>">
                <c:if test="${loggedUser != null}">
                    <img id="upvote"  url="<c:url value = "/change-upvote"/>"  onclick="handleClick(this)" class="svg-btn hover-hand" src="<c:url value="/resources/images/upvote${rating.toString() == 'upvoted'? '-clicked' : ''}.svg"/>"/>
                    <div id="rating" class="${rating.toString()}"><c:out value="${positivityStats.getNetUpvotes()}"/></div>
                    <img id="downvote"  url="<c:url value = "/change-downvote"/>" onclick="handleClick(this)" class="svg-btn hover-hand" src="<c:url value="/resources/images/downvote${rating.toString() == 'downvoted' ? '-clicked' : ''}.svg"/>"/>
                </c:if>
                <c:if test="${loggedUser == null}">
                    <a href="<c:url value = "/create"/>">
                        <img   class="svg-btn" src="<c:url value="/resources/images/upvote.svg"/>"/>
                    </a>
                    <div  ><c:out value="${positivityStats.getNetUpvotes()}"/></div>
                    <a href="<c:url value = "/create"/>">
                        <img    class="svg-btn" src="<c:url value="/resources/images/downvote.svg"/>"/>
                    </a>
                </c:if>
            </div>

                <h1 class="text-xl-center mx-auto max-w-75 m-3 text-white"><c:out value="${news.title}"/></h1>
<div>
    <img src="<c:url value="/resources/images/${positivity.imageName}"/> " alt="..." class="quality-indicator-news-view  <c:out value="${positivity}"/>" data-toggle="tooltip" data-placement="top" title="<spring:message code="home.upvotes" arguments="${positivityStats.getPercentageUpvoted()}"/> - <spring:message code="home.interactions" arguments="${positivityStats.getInteractions()}"/>" />

    </div>


        </div>

        <div class="container">
            <div class="row">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-6">
                        <hr style="border: 2px solid black; border-radius: 2px; !important;"/>
                        <p class="text-sm-left text-secondary"><c:out value="${date}"/></p>
                        <p><spring:message code="showNews.createdBy"/><c:out value=" "/></p>
                        <div class="w-fit">
                            <a  href="<c:url value="/profile/${user.id}"/>" class="w-fit link">
                                <div class="w-fit d-flex flex-row align-items-center p-2 gap-1">

                                    <div class="img-container-article">
                                        <c:if test="${user.hasImage()}">
                                            <img class="rounded-circle object-fit-cover mr-1"
                                                 src="<c:url value="/profile/${user.imageId}/image"/>" alt="">

                                        </c:if>
                                        <c:if test="${!user.hasImage()}">
                                            <img class="rounded-circle object-fit-cover mr-1"
                                                 src="<c:url value="/resources/images/profile-image.png"/>" alt="">

                                        </c:if>
                                    </div>
                                    <b id="profile_name_card_show_news"><c:out value="${user.username != null ? user.username : user.email}"/></b>
                                </div>
                            </a>
                        </div>
                        <div class="d-flex flex-wrap align-items-center gap-1 mt-3">
                            <c:if test="${not empty categories}">
                                <div class="text-sm-left font-weight-bold text-white">
                                    <spring:message code="showNews.categories"/>
                                </div>
                            </c:if>
                            <c:forEach var="category" items="${categories}">

                                <a href="<c:url value = "/TOP">
                <c:param name = "category" value = "${category}"/>
                </c:url>"> <span id="span_category" class="badge badge-pill badge-info"><spring:message code="${category.interCode}"/></span>
                                </a>
                            </c:forEach>
                        </div>
                        <p class="text-sm-left text-secondary"><spring:message code="home.read" arguments="${fullNews.readTime}"/></p>
                    </div>
                    <div class="col-md-6">
                        <c:if test="${news.hasImage()}">
                            <img src="<c:url value="/news/${news.imageId}/image"/>" class="img-fluid d-block m-l-none" style="padding-top: 1rem"/>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        </div>

            <br/>

        <%--<div class="h-auto w-75 min-vh-75 justify-content-center align-center-xl ">
            <hr style="border: 2px solid black; border-radius: 2px; width: 50%; margin-right:50% !important;"/>
            <c:if test="${news.hasImage()}">
                <img src="<c:url value="/news/${news.imageId}/image"/>" class="float-sm-right w-50 m-4"/>

            </c:if>
            <p class="text-sm-left text-secondary"><c:out value="${date}"/></p>
            <p><spring:message code="showNews.createdBy"/><c:out value=" "/></p>
            <div class="w-fit">
                <a  href="<c:url value="/profile/${user.id}"/>" class="w-fit link">
                    <div class="w-fit d-flex flex-row align-items-center p-2 gap-1">

                        <div class="img-container-article">
                            <c:if test="${user.hasImage()}">
                                <img class="rounded-circle object-fit-cover mr-1"
                                     src="<c:url value="/profile/${user.imageId}/image"/>" alt="">

                            </c:if>
                            <c:if test="${!user.hasImage()}">
                                <img class="rounded-circle object-fit-cover mr-1"
                                     src="<c:url value="/resources/images/profile-image.png"/>" alt="">

                            </c:if>
                        </div>
                        <b id="profile_name_card_show_news"><c:out value="${user.username != null ? user.username : user.email}"/></b>
                    </div>
                </a>
            </div>
            <div class="d-flex flex-wrap align-items-center gap-1 mt-3">
                <c:if test="${not empty categories}">
                    <div class="text-sm-left font-weight-bold text-white">
                        <spring:message code="showNews.categories"/>
                    </div>
                </c:if>
                <c:forEach var="category" items="${categories}">

                    <a href="<c:url value = "/TOP">
                <c:param name = "category" value = "${category}"/>
                </c:url>"> <span id="span_category" class="badge badge-pill badge-info"><spring:message code="${category.interCode}"/></span>
                    </a>
                </c:forEach>
            </div>
            <p class="text-sm-left text-secondary"><c:out value="${fullNews.readTime}"/> min read</p>

        </div>--%>



        <div class="d-flex align-items-center justify-content-between">
            <div class="d-flex gap-1 align-items-center justify-content-between w-100">
                <div class="d-flex align-items-center justify-content-center">
                    <h5 class="article-subtitle text-lg-left mb-0"><c:out value="${news.subtitle}"/></h5>

                </div>
                <c:set var="saved" value="${loggedParameters != null ? loggedParameters.saved : false}"/>
                <c:if test="${loggedUser != null}">
                    <div class="d-flex flex-row align-items-center">
                        <div class="ml-2 news-bookmark d-flex justify-content-center align-items-center" >
                            <img id="bookmark-news" onclick="handleBookmarkClick(this)" class="w-100 h-100 svg-btn svg-bookmark" src="<c:url value="/resources/images/bookmark${saved  ? '-clicked' : ''}.svg"/>" alt="" url="<c:url value="/news/${news.newsId}/save"/>">
                        </div>

                        <div class="news-bookmark d-flex justify-content-center align-items-center hover-hand" data-toggle="${hasReported ? 'tooltip' : ''}" data-placement="${hasReported ? 'top' : ''}" title="Article reported">
                            <img ${hasReported ? '' : 'data-toggle="modal" data-target="#reportModal"'} class="w-100 h-100 ${hasReported ? '' : 'svg-btn'} svg-bookmark" src="<c:url value="/resources/images/flag${hasReported ? '-clicked' : ''}.svg"/>" alt="" >
                        </div>

                        <c:if test="${loggedUser != null && news.creatorId == loggedUser.id}">
                            <div data-toggle="modal" data-target="#binModal" class="svg-btn hover-hand">
                                <img src="<c:url value="/resources/images/bin-svgrepo-com.svg" />" alt="..." class="svg-bookmark" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.deleteNews"/> "/>
                            </div>

                            <div class="modal fade" id="binModal" tabindex="-1" aria-labelledby="binModalLabel" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title"><spring:message code="profile.modal.question"/></h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <div class="modal-body">
                                            <spring:message code="profile.modal.msg"/>
                                        </div>
                                        <div class="modal-footer">
                                            <form method="post" action="<c:url value="/news/${newsId}/delete"/>">
                                                <button type="submit" class="btn btn-primary"><spring:message code="profile.modal.accept"/></button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </c:if>
                    </div>

                    <!-- Modal -->
                    <div class="modal fade" id="reportModal" tabindex="-1" aria-labelledby="binModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="binModalLabel"><spring:message code="profile.modal.question"/></h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <c:url value="/news/${newsId}/report" var="postUrl"/>
                                    <form:form modelAttribute="reportNewsForm" enctype="multipart/form-data" action="${postUrl}" method="post" cssClass="h-auto w-100">

                                    <div class="input-group">

                                        <c:forEach var="item" items="${reportReasons}">
                                            <div class="form-check w-100">
                                                <spring:message code="${item.interCode}" var="label"/>
                                                <form:radiobutton path="reason" cssClass="form-check-input" value="${item.toString()}" id="${item.toString()}" label="${label}"/>
                                            </div>
                                        </c:forEach>
                                    </div>
                                    <div class="w-100">
                                        <form:errors cssClass="text-danger" path="reason" element="p"/>

                                    </div>

                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code="profile.modal.cancel"/></button>
                                    <button type="submit" class="btn btn-primary"><spring:message code="profile.modal.accept"/></button>
                                </div>
                                </form:form>

                            </div>
                        </div>
                    </div>
                </c:if>
            </div>


        </div>


        <div class="d-flex w-100 min-vh-65 align-items-center flex-column">
            <div class="article-body">
                <c:out value="${news.body}" escapeXml="false"/>
            </div>
            <div class="d-flex flex-column w-75 align-items-center justify-content-center align-self-center" id="comments">
                <h2 class="align-self-start my-2 text-white"><spring:message code="showNews.comments"/></h2>
                <c:if test="${loggedUser != null}">

                    <div class="d-flex flex-column w-100 mb-4">

                        <div class="bg-transparent">
                            <c:url value="/news/${newsId}/comment" var="postUrl"/>
                            <form:form modelAttribute="commentNewsForm" enctype="multipart/form-data" action="${postUrl}" method="post">
                            <div class="d-flex flex-column mt-4 mb-4">
                                <div class="form-group w-100">
                                    <form:textarea path="comment" cssClass="form-control w-100 custom-comment-area text-white" rows="5" id="comment"></form:textarea>
                                </div>
                                <div class="w-100">
                                    <form:errors cssClass="text-danger" path="comment" element="p"/>

                                </div>
                                <button class="btn btn-primary flex-grow-0 align-self-end" type="submit"><spring:message code="showNews.comment.submit"/></button>
                                </form:form>

                            </div>

                        </div>
                    </div>
                </c:if>

                <div class="d-flex flex-column w-100 ">
                    <c:if test="${loggedUser == null && empty commentsPage.content}">
                        <c:url var="login" value="/login"/>
                        <c:url var="signup" value="/create"/>
                        <h6 class="m-2 align-self-center"><spring:message code="showNews.emptyComments" arguments="${login},${signup}"/></h6>
                    </c:if>
                    <c:if test="${loggedUser != null && empty commentsPage.content}">
                        <h6 class="m-2 align-self-center"><spring:message code="showNews.emptyCommentsLogged"/></h6>
                    </c:if>
                    <c:forEach var="comment" items="${commentsPage.content}">

                        <c:set var="user" value="${comment.user}"/>
                        <div class="mb-4 w-100 p-4 bg-transparent border-bottom" >

                            <div >
                                <div class="d-flex flex-row gap-1 align-items-center">
                                    <div class="img-container-navbar">
                                        <c:if test="${user.hasImage()}">
                                            <img class="object-fit-cover rounded-circle" src="<c:url value="/profile/${user.getImageId()}/image"/>" alt="Image Description">

                                        </c:if>
                                        <c:if test="${!user.hasImage()}">
                                            <img class="object-fit-cover rounded-circle" src="<c:url value="/resources/images/profile-image.png"/>" alt="Image Description">
                                        </c:if>
                                    </div>
                                    <a class="link" href="<c:url value="/profile/${user.id}"/>"><h5 class="mb-0 link-text"><c:out value="${user}"/></h5></a>
                                </div>
                                    <%--                            <span class="font-weight-light">${comment.getFormattedDate(locale)}</span>--%>
                                <c:set var="timeAmount" value="${comment.getAmountAgo()}"/>
                                <span class="font-weight-light mt-1 mb-2"><spring:message code="${timeAmount.getInterCode()}" arguments="${timeAmount.getQty()}"/></span>

                            </div>

                            <div class="d-flex justify-content-between p-2 w-100">
                                <div class="d-flex align-items-center w-auto gap-1">
                                    <p id="comment"><c:out value="${comment.comment}"/></p>
                                </div>
                                <div class="d-flex align-items-center float-sm-right">
                                    <div data-toggle="modal" data-target="#binModal" class="svg-btn hover-hand ">
                                        <c:if test="${loggedUser != null && comment.user.id == loggedUser.id}">

                                            <img src="<c:url value="/resources/images/bin-svgrepo-com.svg" />" alt="..." class="svg-bookmark" data-toggle="tooltip" data-placement="bottom" title="Borrar comentario"/>

                                            <div class="modal fade" id="binModal" tabindex="-1" aria-labelledby="binModalLabel" aria-hidden="true">
                                                <div class="modal-dialog modal-dialog-centered">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title"><spring:message code="showNews.deleteCommentQuestion"/></h5>
                                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                                <span aria-hidden="true">&times;</span>
                                                            </button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <spring:message code="showNews.deleteCommentBody"/>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <form method="post" action="">
                                                                <button type="submit" class="btn btn-primary"><spring:message code="showNews.deleteComment"/></button>
                                                            </form>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>


                                <%--                    <ul class="list-inline d-sm-flex my-0">--%>
                                <%--                        <li class="list-inline-item ">--%>
                                <%--                            <a class="" href="#!">--%>
                                <%--                                <i class="fa fa-thumbs-up g-pos-rel g-top-1 g-mr-3"></i>--%>
                                <%--                                178--%>
                                <%--                            </a>--%>
                                <%--                        </li>--%>
                                <%--                        <li class="list-inline-item ">--%>
                                <%--                            <a href="#!">--%>
                                <%--                                <i ></i>--%>
                                <%--                                34--%>
                                <%--                            </a>--%>
                                <%--                        </li>--%>
                                <%--                        <li class="list-inline-item ml-auto">--%>
                                <%--                            <a  href="#!">--%>
                                <%--                                <i class="fa fa-reply g-pos-rel g-top-1 g-mr-3"></i>--%>
                                <%--                                Reply--%>
                                <%--                            </a>--%>
                                <%--                        </li>--%>
                                <%--                    </ul>--%>
                        </div>
                    </c:forEach>

                </div>


            </div>

        </div>




    </div>
</div>


</body>
</html>