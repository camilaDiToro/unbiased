<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<body>
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
</body>
</html>