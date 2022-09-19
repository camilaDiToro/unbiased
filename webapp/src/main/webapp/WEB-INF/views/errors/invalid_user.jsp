<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<c:set var="pageTitle" scope="request" value="User not found"/>
<%@ include file="../../../resources/head.jsp" %>
<div class="d-flex align-items-center justify-content-center h-75">
    <div class="text-center">
        <h1 class="display-1 fw-bold">400</h1>
        <p class="fs-1"> <span class="text-info font-weight-bold"><spring:message code="genericError.ops"/></span> <spring:message code="genericError.message.400"/></p>
        <p class="lead">
            <spring:message code="error.invalidUser"/>
        </p>
        <a href="<c:url value="/"/>" class="btn btn-primary"><spring:message code="genericError.button.goHome"/></a>
    </div>
</div>
</body>
</html>