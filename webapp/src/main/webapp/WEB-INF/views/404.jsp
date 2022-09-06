<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<c:set var="pageTitle" scope="request" value="Not Found"/>
<%@ include file="../../resources/head.jsp" %>
<body>
<h2><spring:message code="error.pageNotFound"/></h2>
</body>
</html>