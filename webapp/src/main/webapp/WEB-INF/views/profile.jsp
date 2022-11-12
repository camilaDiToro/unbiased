<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<c:set var="signInOrCreate" scope="request" value="${false}"/>
<%@include file="../../resources/jsp/head.jsp" %>
<script src="<c:url value="/resources/js/upvote-script.js"/>"></script>
<c:if test="${hasErrors}">
    <script>
        $(document).ready(function(){
            $("#profileModal").modal('show');
        });
    </script>
</c:if>

<link href="<c:url value="/resources/css/profile.css"/>" rel="stylesheet">
<body>
<c:set var="news" value="${newsPage.content}"/>

<div class="d-flex h-100 flex-column">

<%@include file="../../resources/jsp/navbar.jsp" %>

<%--TAB (publicaciones, guardado, upvoteado, downvoteado)--%>
<div>
    <ul class="my-2 nav nav-tabs justify-content-center text-light p-2">
        <c:forEach var="cat" items="${categories}">
            <li class="nav-item">
                <a class="text-capitalize nav-link tabs <c:out value = "${category == cat ? 'active': ''}"/> bg-transparent" aria-current="page" href="<c:url value = "/profile/${profileUser.id}/${orderBy}">
                    <c:param name = "category" value = "${cat}"/>
                    </c:url>"><spring:message code="${cat.interCode}"/></a>
            </li>
        </c:forEach>
    </ul>
</div>

<div class="d-flex flex-column h-100">
    <div class="flex-grow-1 d-flex flex-row">
        <%--LEFT SIDE--%>
        <div class="d-flex flex-column w-70 align-items-start">

            <%--TAB (top, new)--%>
            <div class="tab">
                <c:set var = "activeClasses" scope = "session" value = "active"/>
                <c:set var = "inactiveClasses" scope = "session" value = "text-secondary"/>
                <ul class="my-4 nav bg-transparent nav-pills text-light p-2 rounded-lg d-flex ">
                    <c:forEach var="order" items="${orders}">
                        <li class="nav-item">
                            <a class="text-capitalize nav-link rounded-pill hover-pill ml-1 <c:out value = "${orderBy == order ? activeClasses : inactiveClasses}"/>" aria-current="page" href="<c:url value = "/profile/${profileUser.id}/${order}">
                    <c:param name = "category" value = "${category}"/>
                    </c:url>"><spring:message code="${order.interCode}"/></a>
                        </li>
                    </c:forEach>

                </ul>
            </div>

            <%--CARDS--%>
            <div class="tab">
                <c:if test="${empty news and empty pingedNews}" >
                    <div class="h-75 d-flex flex-column justify-content-center align-items-center flex-grow-1 mt-5">
                        <h2 class="fw-normal"><spring:message code="home.emptyCategory.sorry"/></h2>
                        <p class="lead">
                            <spring:message code="${category.interCode}" var="catString"/>
                            <spring:message code="profile.notFound" arguments="${catString}"/>
                        </p>
                    </div>
                </c:if>

                <c:if test="${not empty news or not empty pingedNews}">

                    <div class="container-fluid">
                        <div class="row row-cols-1">
<%--                            pinged news    --%>
                            <c:if test="${not empty pingedNews}">
                                    <c:set var="article" value="${pingedNews}"/>

                                    <c:set var="newsId" value="${article.newsId}"/>
                                    <c:set var="loggedParams" value="${article.loggedUserParameters}"/>
                                    <c:set var="positivityStats" value="${article.positivityStats}"/>

                                    <!-- Modal -->
                                    <div class="modal fade" id="binModalPinged" tabindex="-1" aria-labelledby="binModalLabel" aria-hidden="true">
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
                                                    <form method="post" action="<c:url value="/news/${newsId}/delete"/>">
                                                        <button type="submit" class="btn btn-primary"><spring:message code="profile.modal.accept"/></button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                <div class="modal fade" id="pingModalPinged" tabindex="-1" aria-labelledby="binModalLabel" aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title"><spring:message code="profile.unpin.question"/></h5>
                                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                            <div class="modal-body">
                                                <spring:message code="profile.unpin.body"/>
                                            </div>
                                            <div class="modal-footer">
                                                <form method="post" action="<c:url value="/profile/${article.user.id}/pingNews/${article.newsId}"/>">
                                                    <button type="submit" class="btn btn-primary"><spring:message code="profile.modal.accept"/></button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                    <div class="col mb-4">
                                        <div class="card h-100 d-flex flex-row max-h-300px">
                                            <c:set var="positivity" value="${positivityStats.positivity}"/>
                                            <img src="<c:url value="/resources/images/${positivity.imageName}"/> " alt="..." class="quality-indicator  <c:out value="${positivity}"/>" data-toggle="tooltip" data-placement="top" title="<spring:message code="home.upvotes" arguments="${positivityStats.getPercentageUpvoted()}"/> - <spring:message code="home.interactions" arguments="${positivityStats.getInteractions()}"/>" />
                                            <div class="d-flex flex-column justify-content-between ${article.hasImage() ? 'w-60' : 'w-100'}">
                                                <div class="d-flex w-100">
                                                    <div class="upvote-div-profile d-flex flex-column align-items-center m-3" news-id="<c:out value="${article.newsId}"/>">
                                                        <c:set var="rating" value="${loggedParams != null ? loggedParams.personalRating : ''}"/>

                                                        <c:if test="${loggedUser != null}">
                                                            <img id="upvote"  url="<c:url value = "/change-upvote"/>"  onclick="handleClick(this, 'news-id')" class="svg-btn hover-hand" src="<c:url value="/resources/images/upvote${rating.toString() == 'upvoted'? '-clicked' : ''}.svg"/>"/>
                                                            <div id="rating" class="${rating.toString()}"><c:out value="${positivityStats.getNetUpvotes()}"/></div>
                                                            <img id="downvote"  url="<c:url value = "/change-downvote"/>" onclick="handleClick(this, 'news-id')" class="svg-btn hover-hand" src="<c:url value="/resources/images/downvote${rating.toString() == 'downvoted' ? '-clicked' : ''}.svg"/>"/>
                                                        </c:if>
                                                        <c:if test="${loggedUser == null}">
                                                            <a href="<c:url value = "/create"/>">
                                                                <img  class="svg-btn" src="<c:url value="/resources/images/upvote.svg"/>"/>
                                                            </a>
                                                            <div  ><c:out value="${positivityStats.getNetUpvotes()}"/></div>
                                                            <a href="<c:url value = "/create"/>">
                                                                <img class="svg-btn" src="<c:url value="/resources/images/downvote.svg"/>"/>
                                                            </a>
                                                        </c:if>
                                                    </div>
                                                    <div class="card-body-home">
                                                            <%--                                                    <span class="badge badge-pill badge-primary m-1">Messi</span> <span class="badge badge-pill badge-primary">Messi</span>--%>
                                                        <a class="link max-h-10"  href="<c:url value="/news/${article.newsId}"/>"><h5 class="link-text text-ellipsis"><c:out value="${article.title}"/></h5></a>
                                                        <h6 class="  card-subtitle py-1 text-ellipsis-2"><c:out value="${article.subtitle}"/></h6>
                                                        <c:set var="timeAmount" value="${article.getAmountAgo()}"/>
                                                        <span class="font-weight-light"><spring:message code="${timeAmount.getInterCode()}" arguments="${timeAmount.getQty()}"/></span>

                                                        <p class="text-sm-left text-secondary mb-0">
                                                            <img src="<c:url value="/resources/images/clock-svgrepo-com.svg"/>" alt="..." class="read-clock"/>
                                                            <spring:message code="home.read" arguments="${article.readTime}"/>
                                                        </p>

                                                    </div>
                                                </div>
                                                <div class="d-flex justify-content-between p-2 w-100">
                                                    <div class="d-flex align-items-center w-auto gap-1">
                                                        <div class="img-container-article">
                                                            <c:if test="${article.user.hasImage()}">
                                                                <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/profile/${article.user.userId}/image"/>" alt="">
                                                            </c:if>
                                                            <c:if test="${!article.user.hasImage()}">
                                                                <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/resources/images/profile-image.png"/>" alt="">
                                                            </c:if>
                                                        </div>
                                                        <a class="link" href="<c:url value="/profile/${article.creatorId}"/>">
                                                            <div class="link-text card-name-text text-ellipsis-1">${article.user}</div>

                                                        </a>
                                                    </div>
                                                    <div class="d-flex align-items-center" role="group">

                                                        <c:if test="${isMyProfile && loggedUser == article.user}">

                                                            <button data-toggle="modal" data-target="#binModalPinged" class="btn bin-modal" id="bin_button">
                                                                <img src="<c:url value="/resources/images/bin-svgrepo-com.svg" />" alt="..." class="icon-profile" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.deleteNews"/> "/>
                                                            </button>
                                                            <div data-toggle="modal" data-target="#pingModalPinged" class="svg-btn hover-hand">
                                                                <img class="icon-profile svg-btn svg-bookmark" src="<c:url value="/resources/images/pin-clicked.svg"/>" alt="" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.unpin"/>">
                                                            </div>



                                                        </c:if>

                                                        <c:if test="${loggedUser != null}">
                                                            <div class=" m-1 h-50 max-h-40px d-flex justify-content-center align-items-center" >
                                                                <img class="icon-profile svg-btn svg-bookmark" id="bookmark" onclick="handleBookmarkClick(this)"  src="<c:url value="/resources/images/bookmark${loggedParams != null && loggedParams.saved ? '-clicked' : ''}.svg"/>" alt="" url="<c:url value="/news/${article.newsId}/save"/>" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.articleSave"/>">
                                                            </div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                            <c:if test="${maybeImage.isPresent()}">

                                                <div class="bg-secondary position-relative w-40 border-15px">


                                                    <img src="<c:url value="/news/${maybeImage.get()}/image"/>" class="object-fit-cover" alt="...">

                                                </div>
                                            </c:if>

                                        </div>
                                    </div>
                            </c:if>

                            <c:forEach var="article" items="${news}">
                                <c:set var="article" value="${article}"/>

                                <c:set var="newsId" value="${article.newsId}"/>
                                <c:set var="loggedParams" value="${article.loggedUserParameters}"/>
                                <c:set var="positivityStats" value="${article.positivityStats}"/>

                                <!-- Modal -->
                                <div class="modal fade" id="pingModal${newsId}"  aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title"><spring:message code="profile.pin.question"/></h5>
                                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                            <div class="modal-body">
                                                <spring:message code="profile.pin.body"/>
                                            </div>
                                            <div class="modal-footer">
                                                <form method="post" action="<c:url value="/profile/${article.user.id}/pingNews/${article.newsId}"/>">
                                                    <button type="submit" class="btn btn-primary"><spring:message code="profile.modal.accept"/></button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="modal fade" id="binModal${newsId}" tabindex="-1"  aria-hidden="true">
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
                                                <form method="post" action="<c:url value="/news/${newsId}/delete"/>">
                                                    <button type="submit" class="btn btn-primary"><spring:message code="profile.modal.accept"/></button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col mb-4">
                                    <div class="card h-100 d-flex flex-row max-h-300px">
                                        <c:set var="positivity" value="${positivityStats.positivity}"/>
                                        <img src="<c:url value="/resources/images/${positivity.imageName}"/> " alt="..." class="quality-indicator  <c:out value="${positivity}"/>" data-toggle="tooltip" data-placement="top" title="<spring:message code="home.upvotes" arguments="${positivityStats.getPercentageUpvoted()}"/> - <spring:message code="home.interactions" arguments="${positivityStats.getInteractions()}"/>" />
                                        <div class="d-flex flex-column justify-content-between ${article.hasImage() ? 'w-60' : 'w-100'}">
                                            <div class="d-flex w-100">
                                                <div class="upvote-div-profile d-flex flex-column align-items-center m-3" news-id="<c:out value="${article.newsId}"/>">
                                                    <c:set var="rating" value="${loggedParams != null ? loggedParams.personalRating : ''}"/>

                                                    <c:if test="${loggedUser != null}">
                                                        <img id="upvote"  url="<c:url value = "/change-upvote"/>"  onclick="handleClick(this, 'news-id')" class="svg-btn hover-hand" src="<c:url value="/resources/images/upvote${rating.toString() == 'upvoted'? '-clicked' : ''}.svg"/>"/>
                                                        <div id="rating" class="${rating.toString()}"><c:out value="${positivityStats.getNetUpvotes()}"/></div>
                                                        <img id="downvote"  url="<c:url value = "/change-downvote"/>" onclick="handleClick(this, 'news-id')" class="svg-btn hover-hand" src="<c:url value="/resources/images/downvote${rating.toString() == 'downvoted' ? '-clicked' : ''}.svg"/>"/>
                                                    </c:if>
                                                    <c:if test="${loggedUser == null}">
                                                        <a href="<c:url value = "/create"/>">
                                                            <img  class="svg-btn" src="<c:url value="/resources/images/upvote.svg"/>"/>
                                                        </a>
                                                        <div  ><c:out value="${positivityStats.getNetUpvotes()}"/></div>
                                                        <a href="<c:url value = "/create"/>">
                                                            <img class="svg-btn" src="<c:url value="/resources/images/downvote.svg"/>"/>
                                                        </a>
                                                    </c:if>
                                                </div>
                                                <div class="card-body-home">
<%--                                                    <span class="badge badge-pill badge-primary m-1">Messi</span> <span class="badge badge-pill badge-primary">Messi</span>--%>
                                                    <a class="link max-h-10"  href="<c:url value="/news/${article.newsId}"/>"><h5 class="link-text text-ellipsis"><c:out value="${article.title}"/></h5></a>
                                                    <h6 class="  card-subtitle py-1 text-ellipsis-2"><c:out value="${article.subtitle}"/></h6>
    <c:set var="timeAmount" value="${article.getAmountAgo()}"/>
    <span class="font-weight-light"><spring:message code="${timeAmount.getInterCode()}" arguments="${timeAmount.getQty()}"/></span>

    <p class="text-sm-left text-secondary mb-0">
                                                        <img src="<c:url value="/resources/images/clock-svgrepo-com.svg"/>" alt="..." class="read-clock"/>
                                                        <spring:message code="home.read" arguments="${article.readTime}"/>
                                                    </p>

                                                </div>
                                            </div>
                                            <div class="d-flex justify-content-between p-2 w-100">
                                                <div class="d-flex align-items-center w-auto gap-1">
                                                    <div class="img-container-article">
                                                        <c:if test="${article.user.hasImage()}">
                                                            <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/profile/${article.user.userId}/image"/>" alt="">
                                                        </c:if>
                                                        <c:if test="${!article.user.hasImage()}">
                                                            <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/resources/images/profile-image.png"/>" alt="">
                                                        </c:if>
                                                    </div>
                                                    <a class="link" href="<c:url value="/profile/${article.creatorId}"/>">
                                                        <div class="link-text card-name-text text-ellipsis-1">${article.user}</div>

                                                    </a>
                                                </div>
                                                <div class="d-flex align-items-center" role="group">

                                                    <c:if test="${isMyProfile && loggedUser == article.user}">
                                                            <button data-toggle="modal" data-target="#binModal${newsId}" class="btn bin-modal" id="bin_button">
                                                                <img src="<c:url value="/resources/images/bin-svgrepo-com.svg" />" alt="..." class="icon-profile" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.deleteNews"/> "/>
                                                            </button>

                                                        <div data-toggle="modal" data-target="#pingModal${newsId}" class="svg-btn hover-hand">
                                                            <img class="icon-profile svg-btn svg-bookmark" src="<c:url value="/resources/images/pin.svg"/>" alt="" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.pin"/>">
                                                        </div>



                                                    </c:if>

                                                    <c:if test="${loggedUser != null}">
                                                        <div class=" m-1 h-50 max-h-40px d-flex justify-content-center align-items-center" >
                                                            <img class="w-25px svg-btn" id="bookmark" onclick="handleBookmarkClick(this)"  src="<c:url value="/resources/images/bookmark${loggedParams != null && loggedParams.saved ? '-clicked' : ''}.svg"/>" alt="" url="<c:url value="/news/${article.newsId}/save"/>" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.articleSave"/>">
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                        <c:set var="maybeImage" value="${article.getImageId()}"/>
                                        <c:if test="${maybeImage.isPresent()}">

                                        <div class="bg-secondary position-relative w-40 border-15px">


                                                <img src="<c:url value="/news/${maybeImage.get()}/image"/>" class="object-fit-cover" alt="...">

                                        </div>
                                        </c:if>

                                    </div>
                                </div>

                            </c:forEach>
                        </div>

                    </div>
                </c:if>
            </div>

        </div>
        <%--RIGHT SIDE--%>
        <div class="d-flex flex-column w-30 justify-content-start pr-5">
        <div class="card right-card" id="right-card">
            <div class="profile">
                <c:if test="${isMyProfile}">

                        <span data-toggle="modal" data-target="#profileModal" class="hover-hand pencil-edit badge-info badge-pill d-flex align-items-center justify-content-center" id="pencil_button">
                        <div class="position-relative img-container-profile mr-1">
                            <img class="position-relative object-fit-contain" src="<c:url value="/resources/images/pencil-edit.png"/>" alt="...">
                        </div>
                        <spring:message code="profile.edit"/>
                        </span>

                </c:if>
                <c:if test="${profileUser.hasImage()}">

                    <c:if test="${followers >= 0 && followers < 1}">
                        <img id="default-frame-color" src="<c:url value="/profile/${profileUser.id}/image"/>" class="rounded-circle object-fit-cover img-div" width="80">
                    </c:if>

                    <c:if test="${followers >=1 && followers < 2}">
                        <img id="gold-frame-color" src="<c:url value="/profile/${profileUser.id}/image"/>" class="rounded-circle object-fit-cover img-div" width="80">
                    </c:if>

                    <c:if test="${followers >=2}">
                        <img id="platinum-frame-color" src="<c:url value="/profile/${profileUser.id}/image"/>" class="rounded-circle object-fit-cover img-div" width="80">
                    </c:if>
                </c:if>
                <c:if test="${!profileUser.hasImage()}">
                    <img src="<c:url value="/resources/images/profile-image.png"/>" class="rounded-circle object-fit-cover img-div" width="80">
                </c:if>
            </div>
            <c:if test="${profileUser.hasPositivityStats()}">
                <c:set var="profilePositivityStats" value="${profileUser.getPositivityStats()}"/>

                <c:set var="profilePositivity" value="${profilePositivityStats.getPositivity()}"/>
                <img src="<c:url value="/resources/images/${profilePositivity.imageName}"/> " alt="..." class="quality-indicator  <c:out value="${profilePositivity}"/>" data-toggle="tooltip" data-placement="top" title="<spring:message code="home.upvotes" arguments="${profilePositivityStats.getPercentageUpvoted()}"/> - <spring:message code="home.interactions" arguments="${profilePositivityStats.getInteractions()}"/>" />

            </c:if>

            <button data-toggle="modal" data-target="#infoModal" class="info-profile-btn bg-transparent border-0" style="background-image: url('<c:url value="/resources/images/info-svgrepo-com.svg"/>')"></button>

            <!-- Modal -->
            <div class="modal fade" id="infomodal" tabindex="-1" aria-labelledby="infoModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="infoModalLabel"><spring:message code="profile.modal.infoTitle"/></h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <h6><spring:message code="profile.modal.infoAllowedMsg"/></h6>

                            <div class="info-function d-flex flex-row mb-3">

                                <div class="d-flex">
                                    1. <spring:message code="profile.modal.infoChangeUsername"/>
                                </div>

                                <div class="d-flex info-enabled info-custom-box">
                                    <spring:message code="profile.modal.enabled"/>
                                </div>
                            </div>

                            <div class="info-function d-flex flex-row mb-3">

                                <div class="d-flex">
                                    2. <spring:message code="profile.modal.infoChangeProfileimg"/>
                                </div>

                                <div class="d-flex info-enabled info-custom-box">
                                    <spring:message code="profile.modal.enabled"/>
                                </div>
                            </div>

                            <div class="info-function d-flex flex-row">

                                <div class="d-flex">
                                    3. <spring:message code="profile.modal.infoChangeAddDescription"/>
                                </div>

                                <c:if test="${isJournalist}">
                                    <div class="d-flex info-enabled info-custom-box">
                                        <spring:message code="profile.modal.enabled"/>
                                    </div>
                                </c:if>

                                <c:if test="${!isJournalist}">
                                    <div class="d-flex info-disabled info-custom-box" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.infoDisabled"/> ">
                                        <spring:message code="profile.modal.disabled"/>
                                    </div>
                                </c:if>

                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <img src="<c:url value="/resources/images/front-page-profile.png"/>" class="card-img-top" alt="...">

            <div class="card-body">
                <h4 class="mb-0 card-title text-center"><c:out value="${profileUser.username}"/> </h4>
                <div class="d-flex flex-row align-items-center justify-content-center m-2 gap-2">
                    <span class="card-text text-muted d-block"><c:out value="${profileUser.email}"/> </span>
                    <c:if test="${loggedUser != null && !isMyProfile}">
                        <c:if test="${!isFollowing}">
                            <a class="btn d-flex btn-info btn-sm text-white align-items-center justify-content-center custom-btn-follow" href="<c:url value="/profile/${userId}/follow"/>"><spring:message code="profile.follow"/></a>
                        </c:if>
                        <c:if test="${isFollowing}">
                            <a class="btn d-flex btn-sm border text-white align-items-center justify-content-center" href="<c:url value="/profile/${userId}/unfollow"/>" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="profile.following"/> ">
                                <img src="<c:url value="/resources/images/following.svg"/>" alt="...">
                            </a>
                        </c:if>
                    </c:if>
                </div>

                <div class="d-flex flex-row align-items-center justify-content-center">
                    <div class="d-flex flex-row mr-5">
                        <p class="font-weight-bold">${followers}</p>
                        <p class="custom-follow-text"><spring:message code="profile.followers"/></p>
                    </div>

                    <div class="d-flex flex-row">
                        <p class="font-weight-bold">${following}</p>
                        <p class="custom-follow-text"><spring:message code="profile.following"/></p>
                    </div>
                </div>

                <div class="d-flex justify-content-center align-items-center">
                    <c:if test="${isJournalist}">
                        <div class="text-center font-weight-light m-1 overflow-wrap w-85"><c:out value="${profileUser.description}"/></div>
                    </c:if>
                </div>
            </div>

        </div>
            <c:if test="${isJournalist}">
            <div class="card right-card" >

                <div class="card-body">
                    <c:forEach var="cat" items="${newsCategories}">
                        <c:set var="statistic" value="${statisticsMap[cat]}"/>
                        <spring:message var="orderName" code="${cat.interCode}"/>
                        <div>
                                ${orderName}
                        </div>
                        <div class="progress m-2">
                            <div class="progress-bar" role="progressbar" style="width: ${statistic.percentage}%" aria-valuenow="${statistic.percentage}" aria-valuemin="0" aria-valuemax="100">${statistic.percentage}%</div>
                        </div>
                    </c:forEach>

                </div>
            </c:if>

            </div>







            <!-- Modal edit profile-->
            <div class="modal fade" id="profileModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel"><spring:message code="profile.user.settings"/></h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <c:url value="/profile/${profileUser.id}" var="postUrl"/>
                        <form:form modelAttribute="userProfileForm" enctype="multipart/form-data" action="${postUrl}" method="post">
                            <div class="modal-body">

                                    <spring:message code="profile.modal.changeUsername" var="changeUsername"/>
                                    <form:label path="username"><spring:message code="profile.modal.changeUsername"/></form:label>
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text" id="basic-addon1">@</span>
                                        </div>
                                        <form:input type="text" path="username" cssClass="form-control" id="username-input" placeholder="${changeUsername}" value="${profileUser.username}"/>
                                        <div class="w-100">
                                            <form:errors cssClass="text-danger" path="username" element="p"/>

                                        </div>
                                    </div>

                                    <spring:message code="profile.modal.changeProfilePicture" var="changeUserPicture"/>
                                    <form:label path="image"><spring:message code="profile.modal.changeProfilePicture"/> </form:label>
                                    <div class="input-group mb-3">
                                        <div class="custom-file">
                                            <form:input id="file-input" type="file" path="image" accept="image/png, image/jpeg" cssClass="custom-file-input"/>
                                            <form:label id="file-input-label" path="image" cssClass="custom-file-label" for="inputGroupFile01">${changeUserPicture}</form:label>
                                            <div class="w-100">
                                                <form:errors cssClass="text-danger" path="image" element="p"/>

                                            </div>
                                        </div>

                                        <script>
                                            $('#fileInput').on('change',function(){
                                                //get the file name
                                                var fileName = $(this).val();
                                                //replace the "Choose a file" label
                                                $(this).next('.custom-file-label').html(fileName);
                                            })
                                        </script>
                                    </div>

                                <c:if test="${isJournalist}">
                                    <spring:message code="profile.modal.description" var="descriptionText"/>
                                    <form:label path="description"><spring:message code="profile.modal.changeDescription"/> </form:label>
                                    <div class="input-group mb-3">
                                        <form:input type="text" path="description" cssClass="form-control"  id="description-input" placeholder="${descriptionText}" value="${profileUser.description}"/>
                                        <div class="w-100">
                                            <form:errors cssClass="text-danger" path="description" element="p"/>

                                        </div>
                                    </div>
                                </c:if>
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary h-40"><spring:message code="profile.modal.save"/></button>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
    </div>
    </div>

    <c:if test="${not empty news or not empty pingedNews}">
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