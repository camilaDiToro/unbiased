<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@include file="../../resources/head.jsp" %>
<script src="<c:url value="/resources/upvote-script.js"/>"></script>
<c:if test="${hasErrors}">
    <script>
        $(document).ready(function(){
            $("#reportModal").modal('show');
        });
    </script>
</c:if>
<link href="<c:url value="/resources/custom.css"/>" rel="stylesheet">
<body>

<%@include file="../../resources/navbar.jsp" %>




<c:set var="news" value="${fullNews.news}"/>
<c:set var="user" value="${fullNews.user}"/>
<c:set var="loggedParameters" value="${fullNews.loggedUserParameters}"/>
<c:set var="rating" value="${loggedParameters != null ? loggedParameters.personalRating : ''}"/>
<c:set var="positivityStats" value="${fullNews.positivityStats}"/>
<c:set var="positivity" value="${positivityStats.positivity}"/>



<a href="../TOP" style="position: absolute ; margin-left: 50px; margin-top: 50px">
    <input type="image" src="<c:url value="/resources/images/back_to_prev.png"/>" alt="..." style="max-width: 7%; max-height: 7%">
</a>


<div class="d-flex align-items-center justify-content-center w-100 py-4">
    <div class="h-auto w-75 d-flex flex-column ">
        <div class="d-flex align-items-center  ">
            <div class="d-flex flex-column align-items-center" news-id="<c:out value="${news.newsId}"/>">
                <c:if test="${loggedUser != null}">
                    <img id="upvote" url="<c:url value = "/change-upvote"/>" onclick="handleClick(this)" class="svg-btn" src="<c:url value="/resources/upvote${rating.toString() == 'upvoted'? '-clicked' : ''}.svg"/>"/>

                    <div id="rating" class="${rating.toString()}"><c:out value="${positivityStats.getNetUpvotes()}"/></div>
                    <img id="downvote" url="<c:url value = "/change-downvote"/>" onclick="handleClick(this)" class="svg-btn" src="<c:url value="/resources/downvote${rating.toString() == 'downvoted' ? '-clicked' : ''}.svg"/>"/>
                </c:if>
                <c:if test="${loggedUser == null}">
                    <a href="<c:url value = "/create"/>">
                        <img   class="svg-btn" src="<c:url value="/resources/upvote.svg"/>"/>
                    </a>
                    <div  ><c:out value="${positivityStats.getNetUpvotes()}"/></div>
                    <a href="<c:url value = "/create"/>">
                        <img    class="svg-btn" src="<c:url value="/resources/downvote.svg"/>"/>
                    </a>
                </c:if>
            </div>

                <h1 class="text-xl-center mx-auto max-w-75 m-3"><c:out value="${news.title}"/></h1>
<div>
    <div class="d-inline-block quality-indicator-news-view <c:out value="${positivity}"/>" data-toggle="tooltip" data-placement="top" title="${positivityStats.getPercentageUpvoted()}% <spring:message code="home.upvotes"/> - ${positivityStats.getInteractions()} <spring:message code="home.interactions"/>" >
    </div>

    </div>


        </div>
        <hr/>
        <c:if test="${news.hasImage()}">
            <img src="<c:url value="/news/${news.imageId}/image"/>" class="w-50 m-4 rounded mx-auto d-block img-thumbnail"/>

        </c:if>
        <div class="d-flex align-items-center justify-content-between">
            <div class="d-flex gap-1 align-items-center justify-content-between w-100">
                <div class="d-flex align-items-center justify-content-center">
                    <h4 class="text-lg-left mb-0"><c:out value="${news.subtitle}"/></h4>

                </div>
                <c:set var="saved" value="${loggedParameters != null ? loggedParameters.saved : false}"/>
                <c:if test="${loggedUser != null}">
                    <div class="d-flex flex-row">
                        <div class="ml-2 news-bookmark d-flex justify-content-center align-items-center" >
                            <img id="bookmark-news" onclick="handleBookmarkClick(this)" class="w-100 h-100 svg-btn svg-bookmark" src="<c:url value="/resources/bookmark${saved  ? '-clicked' : ''}.svg"/>" alt="" url="<c:url value="/news/${news.newsId}/save"/>">
                        </div>

                        <div class="news-bookmark d-flex justify-content-center align-items-center hover-hand" data-toggle="${hasReported ? 'tooltip' : ''}" data-placement="${hasReported ? 'top' : ''}" title="Article reported">
                            <img ${hasReported ? '' : 'data-toggle="modal" data-target="#reportModal"'} class="w-100 h-100 svg-btn svg-bookmark" src="<c:url value="/resources/flag${hasReported ? '-clicked' : ''}.svg"/>" alt="" >
                        </div>
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
                                    <form:form modelAttribute="reportNewsForm" enctype="multipart/form-data" action="${postUrl}" method="post" cssClass="h-auto w-50">

                                    <div class="input-group">

                                        <c:forEach var="item" items="${reportReasons}">
                                            <div class="form-check w-100">
                                                <form:radiobutton path="reason" cssClass="form-check-input" value="${item.toString()}" id="${item.toString()}"/>
                                                <form:label path="reason" cssClass="form-check-label" for="flexRadioDefault1"> <spring:message code="${item.interCode}"/> </form:label>

                                            </div>
                                        </c:forEach>
                                    </div>
                                    <div class="w-100">
                                        <form:errors cssClass="text-danger" path="reason" element="p"/>

                                    </div>

                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code="profile.modal.cancel"/></button>
                                        <%--                                    <form method="post" action="">--%>
                                    <button type="submit" class="btn btn-primary"><spring:message code="profile.modal.accept"/></button>
                                        <%--                                    </form>--%>
                                </div>
                                </form:form>

                            </div>
                        </div>
                    </div>
                </c:if>
            </div>

<%--            <div class="progress w-25" data-toggle="tooltip" data-placement="top" title="<spring:message code="${positivity.getInterCode()}"/>">--%>
<%--&lt;%&ndash;                <div class="progress-bar progress-bar-striped <c:out value="${fullNews.positivity}"/>" role="progressbar" style="width: ${fullNews.positiveValue*100}%"  aria-valuemin="0" aria-valuemax="100"></div>&ndash;%&gt;--%>
<%--    <div class="quality-indicator-news-view <c:out value="${positivity}"/>" data-toggle="tooltip" data-placement="top" title="${positivityStats.getPercentageUpvoted()}% <spring:message code="home.upvotes"/> - ${positivityStats.getInteractions()} <spring:message code="home.interactions"/>" >--%>
<%--    </div>--%>

            </div>
        <p class="text-sm-left text-secondary"><c:out value="${date}"/>&nbsp · &nbsp<c:out value="${fullNews.readTime}"/> min read</p>

        <div class="w-fit">
            <a href="<c:url value="/profile/${user.id}"/>" class="w-fit">
                <div class="w-fit d-flex flex-row align-items-center p-2 gap-1">

                    <div class="img-container-article">
                        <c:if test="${user.hasImage()}">
                            <img class="rounded-circle object-fit-cover mr-1"
                                 src="<c:url value="/profile/${user.imageId}/image"/>" alt="">

                        </c:if>
                        <c:if test="${!user.hasImage()}">
                            <img class="rounded-circle object-fit-cover mr-1"
                                 src="<c:url value="/resources/profile-image.png"/>" alt="">

                        </c:if>
                    </div>
                    <b><c:out value="${user.username != null ? user.username : user.email}"/></b>
                </div>
            </a>
        </div>
        <div class="w-50 d-flex flex-wrap align-items-center gap-1 mt-3">
            <c:if test="${not empty categories}">
                <div class="text-sm-left font-weight-bold">
                    <spring:message code="showNews.categories"/>
                </div>
            </c:if>
            <c:forEach var="category" items="${categories}">

            <a href="<c:url value = "/TOP">
            <c:param name = "category" value = "${category}"/>
            </c:url>"> <span class="badge badge-pill badge-info"><spring:message code="${category.interCode}"/></span>
                </a>
            </c:forEach>
        </div>

<%--        <div class="w-50 d-flex flex-wrap align-items-center gap-1">--%>
<%--            <div class="text-sm-left font-weight-bold">--%>
<%--                Tags:--%>
<%--            </div>--%>
<%--            <span class="badge badge-pill badge-primary">Messi</span>--%>
<%--        </div>--%>

        <div class="d-flex w-100 min-vh-65 justify-content-center align-items-start">
            <div class="article-body">
                <c:out value="${news.body}" escapeXml="false"/>
            </div>
        </div>

<%--        <c:url value="/news/${newsId}/report" var="postUrl"/>--%>
<%--        <form:form modelAttribute="reportNewsForm" enctype="multipart/form-data" action="${postUrl}" method="post" cssClass="h-auto w-50">--%>

<%--        <div class="input-group">--%>

<%--            <c:forEach var="item" items="${reportReasons}">--%>
<%--                <div class="form-check w-100">--%>
<%--                    <form:radiobutton path="reason" cssClass="form-check-input" value="${item.toString()}" id="${item.toString()}"/>--%>
<%--                    <form:label path="reason" cssClass="form-check-label" for="flexRadioDefault1"> <spring:message code="${item.interCode}"/> </form:label>--%>

<%--                </div>--%>
<%--            </c:forEach>--%>
<%--        </div>--%>
<%--        <div class="w-100">--%>
<%--            <form:errors cssClass="text-danger" path="reason" element="p"/>--%>

<%--        </div>--%>

<%--    </div>--%>
<%--    <div class="modal-footer">--%>
<%--        <button type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code="profile.modal.cancel"/></button>--%>
<%--            &lt;%&ndash;                                    <form method="post" action="">&ndash;%&gt;--%>
<%--        <button type="submit" class="btn btn-primary"><spring:message code="profile.modal.accept"/></button>--%>
<%--            &lt;%&ndash;                                    </form>&ndash;%&gt;--%>
<%--    </div>--%>
<%--    </form:form>--%>
        <div class="d-flex flex-column w-75 align-items-center justify-content-center align-self-center" id="comments">
            <h2 class="align-self-start my-2">Comments</h2>
            <c:if test="${loggedUser != null}">

            <div class="d-flex flex-column w-100 mb-4">

                    <div class="bg-white p-2 px-4">
                        <c:url value="/news/${newsId}/comment" var="postUrl"/>
                        <form:form modelAttribute="commentNewsForm" enctype="multipart/form-data" action="${postUrl}" method="post">
                        <div class="d-flex flex-column mt-4 mb-4">
                            <div class="form-group w-100">
                                <form:textarea path="comment" class="form-control w-100" rows="5" id="comment"></form:textarea>
                            </div>
                            <div class="w-100">
                                <form:errors cssClass="text-danger" path="comment" element="p"/>

                            </div>
                            <button class="btn btn-primary flex-grow-0 align-self-end" type="submit">Comment</button>
                            </form:form>

                        </div>

                    </div>
            </div>
            </c:if>

            <div class="d-flex flex-column w-100 ">
                <c:forEach var="comment" items="${commentsPage.content}">

                <c:set var="user" value="${comment.user}"/>
                    <div class="mb-4 w-100 p-4 bg-white">

                        <div >
                            <div class="d-flex flex-row gap-1 align-items-center">
                                <div class="img-container-navbar">
                                    <c:if test="${user.hasImage()}">
                                        <img class="object-fit-cover rounded-circle" src="<c:url value="/profile/${user.getImageId()}/image"/>" alt="Image Description">

                                    </c:if>
                                        <c:if test="${!user.hasImage()}">
                                            <img class="object-fit-cover rounded-circle" src="<c:url value="/resources/profile-image.png"/>" alt="Image Description">
                                        </c:if>
                                </div>
                                <a href="<c:url value="/profile/${user.id}"/>"><h5 class="mb-0"><c:out value="${user}"/></h5></a>
                            </div>
                            <span class="font-weight-light">${comment.getFormattedDate(locale)}</span>
                        </div>

                        <p id="comment"><c:out value="${comment.comment}"/></p>

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


</body>
</html>