<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2><spring:message code="views.profile.title" arguments="${user.email}" htmlEscape="true"/></h2>
<h4><spring:message code="views.profile.desc" arguments="${user.id}" htmlEscape="true"/></h4>
<h4>${user.username}</h4>

<c:if test="${user.imageId!=null}">
    <img src="<c:url value="/profile/${user.imageId}/image"/>" class="user-section-img"/>
</c:if>

<form:form modelAttribute="userProfileForm" method="post" enctype="multipart/form-data" acceptCharset="utf-8">
    <div>
        <form:label path="image">
            insertar imagen
        </form:label>
        <form:input type="file" path="image" accept="image/png, image/jpeg" />
        <form:errors path="image" element="p" cssStyle="color:red"/>
    </div>
    <div>
        <form:errors path="username" element="p" cssStyle="color: red"/>
        <form:label path="username">Username:</form:label>
        <form:input type="text" path="username"/>
    </div>
    <button style="border-radius:4px; padding:4px; font-size: 18px; margin-top: 5px">guardar</button>
</form:form>
</body>
</html>