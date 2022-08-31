<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 29/08/2022
  Time: 11:51
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<%@include file="../../resources/navbar.jsp" %>
<body>
<div style="display: flex; flex-direction: column; align-items: center; justify-content: center;" class="h-auto p-5">

    <%--preguntar si el tag de form-group es un tema de notacion o si es algo mas, porque estoy
    mezclando form group y form input--%>
    <c:url value="/news/create" var="postUrl"/>
<form:form class="h-auto w-50" modelAttribute="createNewsForm" enctype="multipart/form-data" action="${postUrl}" method="post" >
        <div class="form-group">
                <form:errors path="title" element="p" cssStyle="color: red"/>
                <form:label path="title">Title</form:label>
                <form:input type="text" class="form-control" path="title"/>
        </div>
        <div class="form-group">
                <form:errors path="subtitle" element="p" cssStyle="color: red"/>
                <form:label path="subtitle">Subtitle</form:label>
                <form:input type="text" class="form-control" path="subtitle"/>
        </div>

        <div class="form-group">

                <form:errors path="body" element="p" cssStyle="color: red"/>
                <form:label path="body" >Body</form:label>
                <form:textarea type="text" class="form-control" path="body"/>

        </div>

        <div class="form-group">
            <form:errors path="creatorEmail" element="p" cssStyle="color: red"/>
            <form:label path="creatorEmail">Email</form:label>
            <form:input type="text" class="form-control" id="exampleInputEmail1" path="creatorEmail"/>
        </div>

        <div class="form-group">
            <form:label path="image">
                insertar imagen
            </form:label>
            <form:input type="file" class="form-control-file" path="image" accept="image/png, image/jpeg" />
            <form:errors path="image" element="p" cssStyle="color:red"/>
        </div>

        <div style="width: 100%; display: flex; justify-content: end">
            <button class="btn btn-primary" type="submit">Guardar</button>
        </div>
    </form:form>

</div>


</body>
</html>
