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
    <div style=" width: 30%; height: 100%">

    </div>
</div>

</body>
</html>