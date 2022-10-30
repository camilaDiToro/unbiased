<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html>
<c:set var="pageTitle" scope="request" value="${pageTitle}"/>
<%@ include file="../../resources/jsp/head.jsp" %>
<script src="<c:url value="/resources/js/upvote-script.js"/>"></script>
<body>
<c:set var="reports" value="${reportedNewsPage.content}"/>
<div class="d-flex h-100 flex-column">
    <%@ include file="../../resources/jsp/navbar.jsp" %>
    <div class="d-flex flex-column h-100">
        <div class="flex-grow-1 d-flex flex-row">

            <%--LEFT SIDE--%>
                <%@ include file="../../resources/jsp/moderation-left-side.jsp" %>
            <%--RIGHT SIDE--%>
            <div class="d-flex w-75 flex-column align-items-center">

                <div class="w-100 my-3 d-flex flex-row justify-content-center">

                    <form class=" d-flex w-100 form-inline m-2 my-lg-0 " method="GET" action="<c:url value="/owner/add_admin_page"/>">
                        <div class="d-flex w-100 justify-content-center">
                            <spring:message code="moderation.searchAdmin"  var="searchPlaceholder" />
                            <input id="searchBar_addAdmin" style="background-image: url('<c:url value="/resources/images/loupe-svgrepo-com.svg"/>')!important;" class="search-form search form-control text-white w-55"
                                   type="search" placeholder="${searchPlaceholder}" name="query" value=""/>
                        </div>

                    </form>

                    <div data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.addAdmin"/> ">
                        <button data-toggle="modal" data-target="#addAdminModal" class="mr-5 mt-1 add-admin-btn bg-transparent border-color-transparent" style="background-image: url('<c:url value="/resources/images/plus-svgrepo-com.svg"/>')" ></button>
                    </div>

                    <!-- Modal -->
                    <div class="modal fade" id="addAdminModal" tabindex="-1" aria-labelledby="cardModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="cardModalLabel"><spring:message code="moderation.makeUserAdmin"/></h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <c:url value="/owner/add_admin" var="postUrl"/>
                                    <div id="form-login-index">
                                        <form:form modelAttribute="createAdminForm" action="${postUrl}" method="POST" cssClass="d-flex flex-column align-items-center">
                                            <%--<form:label cssClass="font-weight-bold mb-0" path="email"><spring:message code="moderation.makeUserAdmin"/> </form:label>--%>

                                            <div class="d-flex mb-3">
                                                <img class="size-img-modal-login align-self-center" src="<c:url value="/resources/images/profile-svgrepo-com.svg"/>" alt="..."/>
                                                <label for="email-input" class="sr-only"><spring:message code="login.mail.address" var="mailAddressMsg"/></label>
                                                <form:input path="email" cssClass="form-control text-white w-100" id="email-input" placeholder="${mailAddressMsg}"/>
                                                <form:errors cssClass="text-danger mt-4" path="email" element="small"/>
                                                <c:if test="${addedAdmin}">
                                                    <small class="text-success mt-4">
                                                        <spring:message code="moderation.admin.succesfull"/>
                                                    </small>
                                                </c:if>
                                            </div>

                                            <button class="btn btn-info" type="submit"><spring:message code="moderation.add"/> </button>
                                        </form:form>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="container-fluid mt-3">
                    <div class="row row-cols-1 row-cols-md-3">
                        <c:forEach var="user" items="${usersPage.content}">
                            <div class="col mb-4">


                                    <div id="add_admin_card" class="card h-100 d-flex flex-row" >
                                        <button class="bg-transparent h-fit btn text-white focus-box-shadow-none" data-toggle="modal" data-target="#removeAdminModal${user.id}">
                                            x
                                        </button>
                                        <c:if test="${user.hasPositivityStats()}">
                                            <c:set var="positivityStats" value="${user.positivityStats}"/>

                                            <c:set var="positivity" value="${positivityStats.positivity}"/>
                                            <img src="<c:url value="/resources/images/${positivity.imageName}"/> " alt="..." class="quality-indicator  <c:out value="${positivity}"/>" data-toggle="tooltip" data-placement="top" title="<spring:message code="home.upvotes" arguments="${positivityStats.getPercentageUpvoted()}"/> - <spring:message code="home.interactions" arguments="${positivityStats.getInteractions()}"/>" />

                                        </c:if>
                                        <div class="d-flex justify-content-between p-2 w-100">
                                            <div class="d-flex align-items-center w-auto gap-1">
                                                <div class="img-container-article">
                                                    <c:if test="${user.hasImage()}">
                                                        <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/profile/${user.id}/image"/>" alt="">
                                                    </c:if>
                                                    <c:if test="${!user.hasImage()}">
                                                        <img class="rounded-circle object-fit-cover mr-1" src="<c:url value="/resources/images/profile-image.png"/>" alt="">
                                                    </c:if>
                                                </div>
                                                <a class="link" href="<c:url value="/profile/${user.id}"/>">
                                                <div class="link-text card-name-text text-ellipsis-1">${user}</div>
                                                </a>


                                            </div>
                                        </div>


                                    </div>
                                <div class="modal fade" id="removeAdminModal${user.id}" tabindex="-1" aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="binModalLabel"><spring:message code="owner.removeAdminTitle"/></h5>
                                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                            <div class="modal-body">
                                                <spring:message code="owner.removeAdminMsg"/>
                                            </div>
                                            <div class="modal-footer">
                                                <form method="get" action="<c:url value="/owner/delete_admin_page/${user.id}"/>">
                                                    <button type="submit" class="btn btn-primary"><spring:message code="profile.modal.accept"/></button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>


                            </div>
                        </c:forEach>

                    </div>

                </div>




            </div>


        </div>

        <c:if test="${not empty usersPage.content}">
            <nav class="d-flex justify-content-center align-items-center">
                <ul class="pagination">

                    <li class="page-item"><a class="page-link" href="<c:url value = "/owner/add_admin_page">
                        <c:param name = "page" value = "1"/>
                        </c:url>"><spring:message code="home.pagination.first"/></a></li>


                    <c:forEach var = "i" begin = "${usersPage.minPage}" end = "${usersPage.maxPage}">
                        <li class="page-item"><a class="page-link ${i == usersPage.currentPage ? 'font-weight-bold' : ''}" href="<c:url value = "/owner/add_admin_page">
                        <c:param name = "page" value = "${i}"/>
                        </c:url>"><c:out value="${i}"/></a></li>
                    </c:forEach>

                    <li class="page-item"><a class="page-link" href="<c:url value = "/owner/add_admin_page">
                        <c:param name = "page" value = "${usersPage.totalPages}"/>
                        </c:url>"><spring:message code="home.pagination.last"/></a></li>

                </ul>
            </nav>
        </c:if>


    </div>
</div>
</body>
</html>