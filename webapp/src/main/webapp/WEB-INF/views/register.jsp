<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<c:set var="pageTitle" scope="request" value="Register"/>
<c:set var="signInOrCreate" scope="request" value="${true}"/>
<%@include file="../../resources/head.jsp" %>
<body class="text-center">
<%--<h2><spring:message code="views.register.title"/></h2>--%>
<c:url value="/create" var="postUrl"/>
<%--<form:form modelAttribute="registerForm" action="${postUrl}" method="post" >--%>
<%--    <div>--%>
<%--        <form:errors path="email" element="p" cssStyle="color: red"/>--%>
<%--        <form:label path="email">Email:</form:label>--%>
<%--        <form:input type="text" path="email"/>--%>
<%--    </div>--%>
<%--    <div>--%>
<%--        <form:errors path="password" element="p" cssStyle="color: red"/>--%>
<%--        <form:label path="password">Password: </form:label>--%>
<%--        <form:input type="password" path="password" />--%>
<%--    </div>--%>
<%--    <div>--%>
<%--        <input type="submit" value="Register!"/>--%>
<%--    </div>--%>
<%--</form:form>--%>



<form:form cssClass="form-signin" modelAttribute="registerForm" action="${postUrl}" method="post">
    <h1 class="logo mb-4 text-info">unbiased</h1>
    <h1 class="h3 mb-3 font-weight-normal text-light"><spring:message code="views.register.title"/></h1>

    <div>
        <form:label path="email" cssClass="sr-only"><spring:message code="register.mail.address" var="mailAddressMsg"/></form:label>
        <form:input type="text" path="email"  cssClass="form-control" placeholder="Email address" required="${mailAddressMsg}" autofocus=""/>
                    <form:errors path="email" element="p" cssStyle="color: red"/>
    </div>
    <div>
        <form:label path="password" cssClass="sr-only"><spring:message code="register.password" var="passwordMsg"/></form:label>
        <form:input path="password" type="password"  cssClass="form-control" placeholder="${passwordMsg}"/>
        <form:errors path="password" element="p" cssStyle="color: red"/>

    </div>

    <button class="btn btn-lg btn-info btn-block" type="submit"><spring:message code="register.signUp"/></button>
    <p class="mt-5 mb-3 text-muted">© 2022-2022</p>
</form:form>
</body>
</html>