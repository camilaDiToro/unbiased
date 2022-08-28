<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<body>
<h2><spring:message code="views.register.title"/></h2>
<c:url value="/create" var="postUrl"/>
<form:form modelAttribute="registerForm" action="${postUrl}" method="post" >
    <div>
        <form:errors path="email" element="p" cssStyle="color: red"/>
        <form:label path="email">Email:</form:label>
        <form:input type="text" path="email"/>
    </div>
    <div>
        <form:errors path="password" element="p" cssStyle="color: red"/>
        <form:label path="password">Password: </form:label>
        <form:input type="password" path="password" />
    </div>
    <div>
        <input type="submit" value="Register!"/>
    </div>
</form:form>
</body>
</html>