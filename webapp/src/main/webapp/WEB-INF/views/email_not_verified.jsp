<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<c:set var="signInOrCreate" scope="request" value="${true}"/>
<%@include file="../../resources/jsp/head.jsp" %>
<link href="<c:url value="/resources/css/login.css"/>" rel="stylesheet">
<body class="text-center">

<c:url value="/email_not_verified/${status}" var="postUrl"/>

<form:form cssClass="form-signin" modelAttribute="resendEmailForm" action="${postUrl}" method="post">
    <h1 class="logo mb-4 text-info">unbiased</h1>
    <h1 class="h2 mb-2 font-weight-normal text-light"><spring:message code="${errorMsg}"/></h1>

    <div class="mb-3">
        <form:label path="email" cssClass="sr-only"><spring:message code="register.mail.address" var="mailAddressMsg"/></form:label>
        <form:input type="text" path="email"  cssClass="form-control" placeholder="Email address" required="${mailAddressMsg}" autofocus=""/>
        <form:errors path="email" element="p" cssClass="text-danger"/>
    </div>

    <button class="btn btn-lg btn-info btn-block" type="submit"><spring:message code="login.resendVerificationEmail.button"/></button>
    <p class="mt-5 mb-3 text-muted">© 2022-2022</p>
</form:form>
</body>
</html>


