<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<c:set var="pageTitle" scope="request" value="Login"/>
<c:set var="signInOrCreate" scope="request" value="${false}"/>
<%@include file="../../resources/head.jsp" %>
<%--<body>
<%@include file="../../resources/navbar.jsp" %>
<h2><spring:message code="views.profile.title" arguments="${user.email}" htmlEscape="true"/></h2>
<h4><spring:message code="views.profile.desc" arguments="${user.id}" htmlEscape="true"/></h4>

<form:form modelAttribute="userProfileForm" method="post" enctype="multipart/form-data" acceptCharset="utf-8">
    <form:label path="image">
        insertar imagen
    </form:label>
    <form:input type="file" path="image" accept="image/png, image/jpeg" />
    <form:errors path="image" element="p" cssStyle="color:red"/>
    <button style="border-radius:4px; padding:4px; font-size: 18px; margin-top: 5px">guardar</button>
</form:form>
</body>--%>

<link href="<c:url value="/resources/profile.css"/>" rel="stylesheet">
<body>
<%@include file="../../resources/navbar.jsp" %>

<div style=" display: flex; flex-direction: row; width: 100%; height: 100%">
    <%--LEFT SIDE--%>
    <div style="width: 70%; height: 100%">
        <div style="margin-left: 5%; margin-top: 2%; font-family: 'Helvetica Neue', sans-serif; font-weight: 700; font-size: 42px">
            Alejo Caeiro
        </div>
    </div>

    <div style="border-left:1px grey; border-right:1px solid grey; height:100%; opacity: 50%"></div>

    <%--RIGHT SIDE--%>
    <div style="display: flex; width: 30%; height: 100%;justify-content: center; margin-top: 5%">
        <div class="card" style="width: 18rem; height: 12rem">
            <img src="<c:url value="/resources/front-page-profile.png"/>" class="card-img-top" alt="...">
            <div class="card-body">
                <%--<h5 class="card-title">Card title</h5>
                <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                <a href="#" class="btn btn-primary">Go somewhere</a>--%>
                <h4 class="mb-0 card-title text-center">Alejo Caeiro</h4>
                <span class="card-text text-muted d-block mb-2 text-center"><c:out value="${user.email}"/> </span>
            </div>
        </div>

        <div class="profile">
            <img src="<c:url value="/resources/profile-image.png"/>" class="rounded-circle" width="80">
        </div>
    </div>
</div>

</body>
</html>