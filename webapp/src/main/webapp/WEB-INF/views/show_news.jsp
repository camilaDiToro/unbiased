<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<c:set var="pageTitle" scope="request" value="${news.title}"/>
<%@include file="../../resources/head.jsp" %>
<body>
<%@include file="../../resources/navbar.jsp" %>
<div style="position: absolute ; margin-left: 4%; margin-top: 2%">
    <a href="../TOP">
        <input type="image" src="<c:url value="/resources/images/back_to_prev.png"/>" alt="..." style="max-width: 7%; max-height: 7%">
    </a>
</div>

<div class="d-flex align-items-center justify-content-center w-100 py-4">
    <div class="h-auto w-75">

        <h1 class="text-xl-center"><c:out value="${news.title}"/></h1>
        <hr/>
        <img src="<c:url value="/resources/stock_photo.webp"/>" class="float-sm-right w-50 m-4"/>
        <h4 class="text-lg-left"><c:out value="${news.subtitle}"/></h4>
        <p class="text-sm-left text-secondary"><c:out value="${news.creationDate}"/></p>
        <b><spring:message code="showNews.createdBy"/><c:out value=" "/><c:out value="${user.username != null ? user.username : user.email}"/></b>

        <div style="margin-top: 20px">
            <c:forEach var="category" items="${categories}">
                <p>
                    <span class="badge badge-pill badge-info"><spring:message code="${category.interCode}"/></span>
                </p>
            </c:forEach>
        </div>

        <p class="text-justify"><c:out value="${news.body}"/></p>

<%--        <c:if test="${news.hasImage()}">--%>
<%--        <img src="<c:url value="/news/${newsId}/image"/>" class="user-section-img"/>--%>
<%--        </c:if>--%>

    </div>
</div>


</body>
</html>