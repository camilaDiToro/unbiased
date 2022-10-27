<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<c:set var="pageTitle" scope="request" value="${pageTitle}"/>
<%@ include file="../../resources/jsp/head.jsp" %>
<script src="<c:url value="/resources/js/upvote-script.js"/>"></script>
<body>
<c:set var="reports" value="${reportedCommentPage.content}"/>
<div class="d-flex h-100 flex-column">
    <%@ include file="../../resources/jsp/navbar.jsp" %>
    <div class="d-flex flex-column h-100">
        <div class="flex-grow-1 d-flex flex-row">

            <%--LEFT SIDE--%>
                <%@ include file="../../resources/jsp/moderation-left-side.jsp" %>

            <%--RIGHT SIDE--%>
            <div class="d-flex w-75 flex-column">

                <div class="w-100 my-2">
                    <a  href="<c:url value="/admin/reported_comments"/>">
                        <img class="svg-btn hover-hand back-btn mt-3 mb-1" src="<c:url value="/resources/images/back-svgrepo-com.svg"/>" alt="..." data-toggle="tooltip" data-placement="bottom" title="<spring:message code="tooltip.clickToGoBack"/> "/>
                    </a>
                </div>
                <table class="table flex-grow-0">
                    <thead>
                    <tr>
                        <th scope="col"><spring:message code="moderation.user"/></th>
                        <th scope="col"><spring:message code="moderation.reason"/></th>
                        <th scope="col"><spring:message code="moderation.date"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="report" items="${reports}">
                        <tr>
                            <td><c:out value="${report.reporter}"/></td>
                            <td><spring:message code="${report.reason.interCode}"/></td>
                            <td><c:out value="${report.getFormattedDate(locale)}"/></td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
                    <button data-toggle="modal" data-target="#binModal" class="btn btn-danger delete-btn"><spring:message code="moderation.deleteComment"/></button>

                <div class="modal fade" id="binModal" tabindex="-1" aria-labelledby="binModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="binModalLabel"><spring:message code="showNews.deleteCommentQuestion"/></h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <spring:message code="showNews.deleteCommentBody"/>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code="profile.modal.cancel"/></button>
                                <form method="post" action="<c:url value="/admin/reported_comments/${commentId}/delete"/>">
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

                    <li class="page-item"><a class="page-link" href="<c:url value = "/admin/reported_comment_detail/${commentId}">
                        <c:param name = "page" value = "1"/>
                        </c:url>"><spring:message code="home.pagination.first"/></a></li>


                    <c:forEach var = "i" begin = "${reportedCommentPage.minPage}" end = "${reportedCommentPage.maxPage}">
                        <li class="page-item"><a class="page-link ${i == reportedCommentPage.currentPage ? 'font-weight-bold' : ''}" href="<c:url value = "/admin/reported_comment_detail/${commentId}">
                        <c:param name = "page" value = "${i}"/>
                        </c:url>"><c:out value="${i}"/></a></li>
                    </c:forEach>

                    <li class="page-item"><a class="page-link" href="<c:url value = "/admin/reported_comment_detail/${commentId}">
                        <c:param name = "page" value = "${reportedCommentPage.totalPages}"/>
                        </c:url>"><spring:message code="home.pagination.last"/></a></li>

                </ul>
            </nav>
        </c:if>


    </div>
</div>
</body>
</html>