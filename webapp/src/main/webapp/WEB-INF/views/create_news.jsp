<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<body>
<h2>Noticia nueva</h2>
<c:url value="/news/create" var="postUrl"/>
<form:form modelAttribute="createNewsForm" enctype="multipart/form-data" action="${postUrl}" method="post" >
    <div>
        <form:errors path="title" element="p" cssStyle="color: red"/>
        <form:label path="title">Title:</form:label>
        <form:input type="text" path="title"/>
    </div>
    <div>
        <form:errors path="subtitle" element="p" cssStyle="color: red"/>
        <form:label path="subtitle">SubTitle:</form:label>
        <form:input type="text" path="subtitle"/>
    </div>
    <div>
        <form:errors path="body" element="p" cssStyle="color: red"/>
        <form:label path="body">body:</form:label>
        <form:input type="text" path="body"/>
    </div>
    <div>
        <form:errors path="creatorEmail" element="p" cssStyle="color: red"/>
        <form:label path="creatorEmail">Email:</form:label>
        <form:input type="text" path="creatorEmail"/>
    </div>
    <div>
        <form:label path="image">
            insertar imagen
        </form:label>
        <form:input type="file" path="image" accept="image/png, image/jpeg" />
        <form:errors path="image" element="p" cssStyle="color:red"/>
    </div>
    <div>
        <input type="submit" value="Create"/>
    </div>
</form:form>
</body>
</html>
