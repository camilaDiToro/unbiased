<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<c:set var="pageTitle" scope="request" value="Login"/>
<c:set var="signInOrCreate" scope="request" value="${true}"/>
<%@include file="../../resources/head.jsp" %>
<link href="<c:url value="/resources/login.css"/>" rel="stylesheet">
<body class="text-center">
<c:url value="/login" var="loginUrl" />

<form class="form-signin" action="${loginUrl}" method="post">

    <h1 id="title-log" class="logo mb-4">
        <a  class="text-info link" href="<c:url value="/TOP"/>">
            unbiased
        </a>
    </h1>
    <h1 class="h3 mb-3 font-weight-normal text-light"> </h1>
        <div class="d-flex">
            <img class="size-img-modal-login align-self-center" src="<c:url value="/resources/profile-svgrepo-com.svg"/>" alt="..."/>
            <label for="username" class="sr-only"><spring:message code="login.mail.address" var="mailAddressMsg"/></label>
            <input type="text" id="username" name="username" class="form-control" placeholder="${mailAddressMsg}" required="" autofocus="">
        </div>
        <div class="mt-1 d-flex">
            <img class="size-img-modal-login align-self-center" src="<c:url value="/resources/lock-svgrepo-com.svg"/>" alt="..."/>
            <label for="password" class="sr-only"><spring:message code="login.password" var="passwordMsg"/></label>
            <input name="password" type="password" id="password" class="form-control" placeholder="${passwordMsg}">
        </div>
    <c:if test="${param.error}">
        <div class="text-danger text-nowrap form-text d-inline-block">
            <spring:message code="login.error"/>
        </div>
    </c:if>
    <c:if test="${param.unable}">
        <div class="text-danger text-nowrap form-text d-inline-block">
            <spring:message code="login.emailNotVerified"/>
        </div>
        <div class="text-danger text-nowrap form-text d-inline-block">
            <spring:message code="login.emailResended"/>
        </div>
    </c:if>
   <div class="checkbox mb-3">
        <label class="text-light">
            <input type="checkbox" name="rememberme"> <spring:message code="login.rememberMe"/>
        </label>
    </div>
    <button class="btn btn-md btn-info btn-block" type="submit"><spring:message code="login.signIn"/></button>
    <p class="mt-5 mb-3 text-muted">© 2022-2022</p>
    </form>

</body>
</html>