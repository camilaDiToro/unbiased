<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<c:set var="pageTitle" scope="request" value="Login"/>
<c:set var="signInOrCreate" scope="request" value="${true}"/>
<%@include file="../../resources/jsp/head.jsp" %>
<body class="text-center">


<div class="w-100 h-100 d-flex justify-content-center align-items-center" >
  <div>
    <h1 class="logo mb-4 text-info">unbiased</h1>
    <h1 class="h3 mb-3 font-weight-normal text-light"><spring:message code="email.verification.alreadyVerified"/></h1>
    <a class="btn btn-lg btn-info btn-block" type="submit" href="<c:url value="/login"/>"><spring:message code="verifyEmail.login"/> </a>
  </div>
</div>

</body>
</html>