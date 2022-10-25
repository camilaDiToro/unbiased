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

                <div class="w-100 my-2">
                    <a  href="<c:url value="/admin/reported_news"/>">
                        <%--<input type="image" src="<c:url value="/resources/images/back_to_prev.png"/>" alt="..." class="w-50px">--%>
                        <img class="svg-btn hover-hand back-btn mt-3" src="<c:url value="/resources/images/back-svgrepo-com.svg"/>" alt="..." data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.clickToGoBack"/> " />
                    </a>
                </div>
                <c:url value="/owner/add_admin" var="postUrl"/>
                <div id="custom-form-group" class="m-2 w-50 max-w-750px ">
                    <form:form modelAttribute="createAdminForm" action="${postUrl}" method="POST" cssClass="d-flex flex-column align-items-center">

                        <div class="form-group m-2 w-100 p-3">
                            <form:label cssClass="w-100 font-weight-bold" path="email"><spring:message code="moderation.makeUserAdmin"/> </form:label>
                            <form:input placeholder="Email:" path="email" cssClass="form-control text-white w-100"/>
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
                <div class="modal fade" id="binModal" tabindex="-1" aria-labelledby="binModalLabel" aria-hidden="true">
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

            </div>


        </div>

        <c:if test="${not empty reports}">
            <nav class="d-flex justify-content-center align-items-center">
                <ul class="pagination">

                    <li class="page-item"><a class="page-link" href="<c:url value = "/admin/reported_news_detail/${newsId}">
                        <c:param name = "page" value = "1"/>
                        </c:url>"><spring:message code="home.pagination.first"/></a></li>


                    <c:forEach var = "i" begin = "${reportedNewsPage.minPage}" end = "${reportedNewsPage.maxPage}">
                        <li class="page-item"><a class="page-link ${i == reportedNewsPage.currentPage ? 'font-weight-bold' : ''}" href="<c:url value = "/admin/reported_news_detail/${newsId}">
                        <c:param name = "page" value = "${i}"/>
                        </c:url>"><c:out value="${i}"/></a></li>
                    </c:forEach>

                    <li class="page-item"><a class="page-link" href="<c:url value = "/admin/reported_news_detail/${newsId}">
                        <c:param name = "page" value = "${reportedNewsPage.totalPages}"/>
                        </c:url>"><spring:message code="home.pagination.last"/></a></li>

                </ul>
            </nav>
        </c:if>


    </div>
</div>
</body>
</html>