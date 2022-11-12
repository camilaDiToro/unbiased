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
<c:set var="newsId" value="${news.newsId}"/>
<c:set var="user" value="${fullNews.user}"/>
<c:set var="loggedParameters" value="${fullNews.loggedUserParameters}"/>
<c:set var="rating" value="${loggedParameters != null ? loggedParameters.personalRating : ''}"/>
<c:set var="positivityStats" value="${fullNews.positivityStats}"/>
<c:set var="positivity" value="${positivityStats.positivity}"/>


<div class="d-flex flex-column h-100">
<a href="../TOP" class="back-button-show-news">
    <img class="svg-btn hover-hand back-btn" src="<c:url value="/resources/images/back-svgrepo-com.svg"/>" alt="..." data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.clickToGoBack"/> "/>
</a>


<div class="d-flex align-items-center justify-content-center w-100 py-4">
    <div class="h-auto w-75 d-flex flex-column ">
        <div class="d-flex align-items-center  ">
            <div class="d-flex flex-column align-items-center" news-id="<c:out value="${news.newsId}"/>">
                <c:if test="${loggedUser != null}">
                    <img id="upvote"  url="<c:url value = "/change-upvote"/>"  onclick="handleClick(this, 'news-id')" class="svg-btn hover-hand" src="<c:url value="/resources/images/upvote${rating.toString() == 'upvoted'? '-clicked' : ''}.svg"/>"/>
                    <div id="rating" class="${rating.toString()}"><c:out value="${positivityStats.getNetUpvotes()}"/></div>
                    <img id="downvote"  url="<c:url value = "/change-downvote"/>" onclick="handleClick(this, 'news-id')" class="svg-btn hover-hand" src="<c:url value="/resources/images/downvote${rating.toString() == 'downvoted' ? '-clicked' : ''}.svg"/>"/>
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

            <h1 class="text-xl-center mx-auto max-w-75 m-3 text-white overflow-wrap"><c:out value="${news.title}"/></h1>
            <div>
                <img src="<c:url value="/resources/images/${positivity.imageName}"/> " alt="..." class="quality-indicator-news-view  <c:out value="${positivity}"/>" data-toggle="tooltip" data-placement="top" title="<spring:message code="home.upvotes" arguments="${positivityStats.getPercentageUpvoted()}"/> - <spring:message code="home.interactions" arguments="${positivityStats.getInteractions()}"/>" />

            </div>


        </div>
        <hr/>
        <c:set var="maybeImage" value="${article.getImageId()}"/>
        <c:if test="${maybeImage.isPresent()}">
            <img src="<c:url value="/news/${maybeImage.get()}/image"/>" class="w-50 m-4 rounded mx-auto d-block img-thumbnail"/>

        </c:if>
        <div class="d-flex align-items-center justify-content-between">
            <div class="d-flex gap-1 align-items-center justify-content-between w-100">
                <div class="d-flex align-items-center justify-content-center">
                    <h4 class="text-lg-left mb-0 text-white"><c:out value="${news.subtitle}"/></h4>

                </div>
                <c:set var="saved" value="${loggedParameters != null ? loggedParameters.saved : false}"/>
                <c:if test="${loggedUser != null}">
                    <div class="d-flex flex-row align-items-center gap-4px">
                        <div class="ml-2 d-flex justify-content-center align-items-center" >
                            <img id="bookmark-news" onclick="handleBookmarkClick(this)" class="icon-news svg-btn svg-bookmark" src="<c:url value="/resources/images/bookmark${saved  ? '-clicked' : ''}.svg"/>" alt="" url="<c:url value="/news/${news.newsId}/save"/>" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.articleSave"/>">
                        </div>

                        <div class="ml-2 d-flex justify-content-center align-items-center" >
                            <img ${hasReported ? '' : 'data-toggle="modal" data-target="#reportModal"'} class="icon-news ${hasReported ? '' : 'svg-btn'} svg-bookmark" src="<c:url value="/resources/images/flag${hasReported ? '-clicked' : ''}.svg"/>" alt=""  data-toggle=tooltip" data-placement="bottom" title="<spring:message code="tooltip.articleReported"/>">
                        </div>

                        <c:if test="${myNews}">
                            ${msg}
                            <div data-toggle="modal" data-target="#binModal" class="svg-btn hover-hand d-flex justify-content-center align-items-center">
                                <img src="<c:url value="/resources/images/bin-svgrepo-com.svg" />" alt="..." class="icon-news-delete svg-bookmark" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.deleteNews"/> "/>
                            </div>
                            <div data-toggle="modal" data-target="#pingModal${news.newsId}" class="svg-btn hover-hand">
                                <c:choose>
                                    <c:when test="${pinned}">
                                        <img  class="icon-news svg-btn svg-bookmark" src="<c:url value="/resources/images/pin-clicked.svg"/>" alt="" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.unpin"/>">
                                    </c:when>
                                    <c:otherwise>
                                        <img  class="icon-news svg-bookmark" src="<c:url value="/resources/images/pin.svg"/>" alt="" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.pin"/>">
                                    </c:otherwise>
                            </c:choose>
                                <%--                                <img src="<c:url value="/resources/images/bin-svgrepo-com.svg" />" alt="..." class="svg-bookmark" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.deleteNews"/> "/>--%>
                            </div>
                            <div class="modal fade" id="pingModal${newsId}"  aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title">
                                                <c:choose>
                                                    <c:when test="${pinned}">
                                                        <spring:message code="profile.unpin.question"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <spring:message code="profile.pin.question"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <div class="modal-body">
                                            <c:choose>
                                                <c:when test="${pinned}">
                                                    <spring:message code="profile.unpin.body"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <spring:message code="profile.pin.body"/>
                                                </c:otherwise>
                                            </c:choose>

                                        </div>
                                        <div class="modal-footer">
                                            <form method="post" action="<c:url value="/news/${news.newsId}/pingNews"/>">
                                                <button type="submit" class="btn btn-primary"><spring:message code="profile.modal.accept"/></button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
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

                    <!-- Modal Report-->
                    <div class="modal fade" id="reportModal" tabindex="-1" aria-labelledby="binModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="binModalLabel"><spring:message code="showNews.reportNewsQuestion"/></h5>
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
                                                <spring:message code="${item.interCode}" var="code"/>
                                                <form:radiobutton path="reason" cssClass="form-check-input" value="${item.toString()}" id="${item.toString()}" label="${code}"/>

                                            </div>
                                        </c:forEach>
                                    </div>
                                    <div class="w-100">
                                        <form:errors cssClass="text-danger" path="reason" element="p"/>

                                    </div>

                                </div>
                                <div class="modal-footer">
                                    <button type="submit" class="btn btn-primary"><spring:message code="profile.modal.accept"/></button>
                                </div>
                                </form:form>

                            </div>
                        </div>
                    </div>
                </c:if>
            </div>


        </div>
        <p class="text-sm-left text-secondary">
            <c:out value="${date}"/>&nbsp · &nbsp
            <img src="<c:url value="/resources/images/clock-svgrepo-com.svg"/>" alt="..." class="read-clock"/>
            <spring:message code="home.read" arguments="${fullNews.readTime}"/>
        </p>

        <div class="w-fit">
            <a  href="<c:url value="/profile/${user.id}"/>" class="w-fit link">
                <div class="w-fit d-flex flex-row align-items-center p-2 gap-1">

                    <div class="img-container-article">
                        <div class="frame-navbar">
                            <c:if test="${user.hasImage()}">

                                <c:if test="${followers >= 0 && followers < 1}">
                                    <img id="default-frame-color" src="<c:url value="/profile/${user.id}/image"/>" class="rounded-circle object-fit-cover mr-1" >
                                </c:if>

                                <c:if test="${followers >=1 && followers < 2}">
                                    <img id="gold-frame-color" src="<c:url value="/profile/${user.id}/image"/>" class="rounded-circle object-fit-cover mr-1" >
                                </c:if>

                                <c:if test="${followers >=2}">
                                    <img id="platinum-frame-color" src="<c:url value="/profile/${user.id}/image"/>" class="rounded-circle object-fit-cover">
                                </c:if>
                            </c:if>
                            <c:if test="${!user.hasImage()}">
                                <img src="<c:url value="/resources/images/profile-image.png"/>" class="rounded-circle">
                            </c:if>
                        </div>
                    </div>
                    <b id="profile_name_card_show_news"><c:out value="${user.username != null ? user.username : user.email}"/></b>
                </div>
            </a>
        </div>
        <div class="w-50 d-flex flex-wrap align-items-center gap-1 mt-3">
            <c:if test="${not empty news.categories}">
                <div class="text-sm-left font-weight-bold text-white">
                    <spring:message code="showNews.categories"/>
                </div>
            </c:if>
            <c:forEach var="category" items="${news.categories}">

                <a href="<c:url value = "/TOP">
            <c:param name = "category" value = "${category}"/>
            </c:url>"> <span id="span_category" class="badge badge-pill badge-info"><spring:message code="${category.interCode}"/></span>
                </a>
            </c:forEach>
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
                            <c:url value="/news/${newsId}/comment" var="postUrl">
                                <c:param name = "order" value = "${orderBy}"/>
                            </c:url>
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

                <div class="d-flex flex-column w-100 " id="comment-section">
                    <c:if test="${loggedUser == null && empty commentsPage.content}">
                        <c:url var="login" value="/login"/>
                        <c:url var="signup" value="/create"/>
                        <h6 class="m-2 align-self-center"><spring:message code="showNews.emptyComments" arguments="${login},${signup}"/></h6>
                    </c:if>
                    <c:if test="${loggedUser != null && empty commentsPage.content}">
                        <h6 class="m-2 align-self-center"><spring:message code="showNews.emptyCommentsLogged"/></h6>
                    </c:if>
                    <c:set var = "activeClasses" scope = "session" value = "bg-info active"/>
                    <c:set var = "inactiveClasses" scope = "session" value = "text-secondary"/>
                    <ul class="my-4 mt-4 nav bg-transparent nav-pills text-light p-2 rounded-lg d-flex">
                    <c:forEach var="order" items="${orders}">
                        <li class="nav-item">
                            <a class="text-capitalize nav-link fromLeft rounded-pill hover-pill ml-1 <c:out value = "${orderBy == order ? activeClasses : inactiveClasses}"/>" aria-current="page" href="<c:url value = "/news/${newsId}">
                    <c:param name = "order" value = "${order.toString()}"/>
                    <c:param name = "page" value = "${param.page}"/>
                    </c:url>"><spring:message code="${order.interCode}"/></a>
                        </li>
                    </c:forEach>
                    </ul>
                        <c:forEach var="comment" items="${commentsPage.content}">

                        <c:set var="user" value="${comment.user}"/>
                        <c:set var="positivityStats" value="${comment.getPositivityStats()}"/>
                        <c:set var="rating" value="${commentRatings.get(comment.id)}"/>
                        <div class="mb-4 w-100 p-4 bg-black rounded-comment" >

                            <div >
                                <div class="d-flex flex-row gap-1 align-items-center p-1 mb-1">

                                    <div class="img-container-comment">
                                        <%--<c:if test="${user.hasImage()}">
                                            <img class="object-fit-cover rounded-circle" src="<c:url value="/profile/${user.userId}/image"/>" alt="Image Description">

                                        </c:if>
                                        <c:if test="${!user.hasImage()}">
                                            <img class="object-fit-cover rounded-circle" src="<c:url value="/resources/images/profile-image.png"/>" alt="Image Description">
                                        </c:if>--%>
                                            <div class="frame-navbar">
                                                <c:if test="${user.hasImage()}">

                                                    <c:if test="${followers >= 0 && followers < 1}">
                                                        <img id="default-frame-color" src="<c:url value="/profile/${user.id}/image"/>" class="rounded-circle object-fit-cover mr-1" >
                                                    </c:if>

                                                    <c:if test="${followers >=1 && followers < 2}">
                                                        <img id="gold-frame-color" src="<c:url value="/profile/${user.id}/image"/>" class="rounded-circle object-fit-cover mr-1" >
                                                    </c:if>

                                                    <c:if test="${followers >=2}">
                                                        <img id="platinum-frame-color" src="<c:url value="/profile/${user.id}/image"/>" class="rounded-circle object-fit-cover">
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${!user.hasImage()}">
                                                    <img src="<c:url value="/resources/images/profile-image.png"/>" class="rounded-circle">
                                                </c:if>
                                            </div>
                                    </div>
                                    <a class="link" href="<c:url value="/profile/${user.id}"/>"><h5 class="mb-0 link-text"><c:out value="${user}"/></h5></a>
                                </div>
                                    <%--                            <span class="font-weight-light">${comment.getFormattedDate(locale)}</span>--%>
                                <c:set var="timeAmount" value="${comment.getAmountAgo()}"/>
                                <span class="font-weight-light mt-1 mb-2"><spring:message code="${timeAmount.getInterCode()}" arguments="${timeAmount.getQty()}"/></span>

                            </div>

                            <div class="d-flex flex-column justify-content-between p-2 w-100">
                                <div class="d-flex align-items-center w-auto gap-1">
<%--                                    ${comment.deleted}--%>
                                    <c:choose>
                                        <c:when test="${comment.deleted}">
                                            <p name="comment-${comment.id}" id="comment-${comment.id}" class="comment-text font-italic"><spring:message code="showNews.deletedComment"/></p>

                                        </c:when>
                                        <c:otherwise>
                                            <p name="comment-${comment.id}" id="comment-${comment.id}" class="comment-text"><c:out value="${comment.comment}"/></p>

                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="d-flex align-items-center justify-content-between float-sm-right gap-1">
                                    <c:if test="${!comment.deleted}">
                                        <div class="d-flex flex-row align-items-center gap-1" comment-id="${comment.id}">
                                            <c:if test="${loggedUser != null}">
                                                <img id="upvote"  url="<c:url value = "/change-comment-upvote"/>"  onclick="handleClick(this, 'comment-id')" class="svg-btn hover-hand" src="<c:url value="/resources/images/upvote${rating.toString() == 'upvoted'? '-clicked' : ''}.svg"/>"/>
                                                <div id="rating" class="${rating.toString()}"><c:out value="${positivityStats.getNetUpvotes()}"/></div>
                                                <img id="downvote"  url="<c:url value = "/change-comment-downvote"/>" onclick="handleClick(this, 'comment-id')" class="svg-btn hover-hand" src="<c:url value="/resources/images/downvote${rating.toString() == 'downvoted' ? '-clicked' : ''}.svg"/>"/>
                                            </c:if>
                                            <c:if test="${loggedUser == null}">
                                                <a data-toggle="modal" data-target="#cardModal">
                                                    <img   class="svg-btn hover-hand" src="<c:url value="/resources/images/upvote.svg"/>"/>
                                                </a>
                                                <div  ><c:out value="${positivityStats.netUpvotes}"/></div>
                                                <a data-toggle="modal" data-target="#cardModal">
                                                    <img    class="svg-btn hover-hand" src="<c:url value="/resources/images/downvote.svg"/>"/>
                                                </a>

                                            </c:if>
                                        </div>
                                    </c:if>
                                    <div class="d-flex gap-1 align-items-center justify-content-between">
                                        <div data-toggle="modal" data-target="#binModal${comment.id}" class="svg-btn hover-hand h-fit">
                                            <c:if test="${loggedUser != null && !comment.deleted && comment.user.id == loggedUser.id}">
                                                <spring:message code="showNews.deleteComment" var="deleteComment"/>
                                                <img src="<c:url value="/resources/images/bin-svgrepo-com.svg" />" alt="..." class="icon-comment svg-bookmark" data-toggle="tooltip" data-placement="bottom" title="${deleteComment}"/>
                                            </c:if>
                                        </div>
                                        <!-- Modal delete comment-->
                                        <div class="modal fade" id="binModal${comment.id}"   aria-hidden="true">
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
                                                        <form method="post" action="<c:url value="/news/${newsId}/comment/${comment.id}/delete"/>">
                                                            <button type="submit" class="btn btn-primary"><spring:message code="showNews.deleteComment"/></button>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <c:if test="${loggedUser != null && !comment.deleted}">
                                            <c:set var="hasReportedComment" value="${hasReportedCommentMap.get(comment.id)}"/>

                                            <c:choose>

                                                <c:when test = "${hasReportedComment}">
                                                    <div class=" d-flex justify-content-center align-items-center h-fit" >
                                                        <img src="<c:url value="/resources/images/flag-clicked.svg"/>" alt="..." class="icon-comment svg-bookmark" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.commentReported"/>"/>

                                                            <%--                                                    <img  class="icon-comment" src="<c:url value="/resources/images/flag-clicked.svg"/>" alt="..." data-toggle=tooltip" data-placement="bottom" title="<spring:message code="tooltip.commentReported"/>">--%>
                                                    </div>
                                                </c:when>

                                                <c:otherwise>
                                                    <div class="d-flex justify-content-center align-items-center hover-hand h-fit" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.reportComment"/>">
                                                        <img data-toggle="modal" data-target="#reportComment${comment.id}Modal" class="icon-comment"  data-placement="bottom" src="<c:url value="/resources/images/flag.svg"/>" alt="" title="<spring:message code="tooltip.commentReported"/>" >
                                                    </div>
                                                    <!-- Modal report comment-->
                                                    <div class="modal fade" id="reportComment${comment.id}Modal" tabindex="-1"  aria-hidden="true">
                                                        <div class="modal-dialog modal-dialog-centered">
                                                            <div class="modal-content">
                                                                <div class="modal-header">
                                                                    <h5 class="modal-title" id="reportComment${comment.id}Modal"><spring:message code="showNews.reportCommentQuestion"/></h5>
                                                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                                        <span aria-hidden="true">&times;</span>
                                                                    </button>
                                                                </div>
                                                                <div class="modal-body">
                                                                    <c:url value="/news/${newsId}/comment/${comment.id}/report" var="postUrl"/>
                                                                    <form:form modelAttribute="reportNewsForm" enctype="multipart/form-data" action="${postUrl}" method="post" cssClass="h-auto w-100">

                                                                    <div class="input-group">

                                                                        <c:forEach var="item" items="${reportReasons}">
                                                                            <div class="form-check w-100">
                                                                                <spring:message code="${item.interCode}" var="code"/>
                                                                                <form:radiobutton path="reason" cssClass="form-check-input" value="${item.toString()}" id="${item.toString()}" label="${code}"/>

                                                                            </div>
                                                                        </c:forEach>
                                                                    </div>
                                                                    <div class="w-100">
                                                                        <form:errors cssClass="text-danger" path="reason" element="p"/>

                                                                    </div>

                                                                </div>
                                                                <div class="modal-footer">
                                                                    <button type="submit" class="btn btn-primary"><spring:message code="profile.modal.accept"/></button>
                                                                </div>
                                                                </form:form>

                                                            </div>
                                                        </div>
                                                    </div>

                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </div>


                                </div>
                            </div>
                        </div>
                    </c:forEach>

                </div>


            </div>

        </div>

    </div>
</div>
    <c:if test="${not empty commentsPage && not empty commentsPage.content}">
        <c:set var="page" value="${commentsPage}"/>
        <nav class="d-flex justify-content-center align-items-center">
            <ul class="pagination" >

                <li class="page-item"><a class="page-link" href="<c:url value = "/news/${newsId}">
                <c:param name = "page" value = "1"/>
</c:url>"><spring:message code="home.pagination.first"/></a></li>


                <c:forEach var = "i" begin = "${page.minPage}" end = "${page.maxPage}">
                    <li class="page-item"><a class="page-link ${i == page.currentPage ? 'font-weight-bold' : ''}" href="<c:url value = "/news/${newsId}">
<c:param name = "page" value = "${i}"/></c:url>"><c:out value="${i}"/></a></li>
                </c:forEach>

                <li class="page-item"><a class="page-link" href="<c:url value = "/news/${newsId}">
                <c:param name = "page" value = "${page.totalPages}"/>
</c:url>"><spring:message code="home.pagination.last"/></a></li>

            </ul>
        </nav>

    </c:if>

</div>
</body>
</html>