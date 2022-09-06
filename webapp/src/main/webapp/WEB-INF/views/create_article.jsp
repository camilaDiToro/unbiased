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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<c:set var="pageTitle" scope="request" value="Create"/>
<%@include file="../../resources/navbar.jsp" %>
<body>

<div style="position: absolute ; margin-left: 4%; margin-top: 2%">
    <a href="./TOP">
    <input type="image" src="<c:url value="/resources/images/back_to_prev.png"/>" alt="..." style="max-width: 7%; max-height: 7%">
    </a>
</div>
<div style="display: flex; flex-direction: column; align-items: center; justify-content: center" class="h-auto p-5">

    <c:url value="/create_article" var="postUrl"/>
    <form:form modelAttribute="createNewsForm" enctype="multipart/form-data" action="${postUrl}" method="post" cssClass="h-auto w-50">

        <div>
            <form:errors path="title" element="p" cssStyle="color: red"/>
            <form:label path="title"><spring:message code="createArticle.title"/></form:label>
            <div class="form-group">
                <div class="input-group mb-3">
                    <spring:message code="createArticle.title.placeholder" var="titlePlaceholder" />
                    <form:input placeholder="${titlePlaceholder}" cssClass="form-control"  type="text" path="title"/>
                </div>
            </div>
        </div>


        <div>
            <form:errors path="subtitle" element="p" cssStyle="color: red"/>
            <form:label path="subtitle"><spring:message code="createArticle.description"/></form:label>
            <div class="form-group">
                <div class="input-group mb-3">
                    <spring:message code="createArticle.description.placeholder"  var="descPlaceholder" />
                    <form:input type="text" path="subtitle" cssClass="form-control" placeholder="${descPlaceholder}"/>
                </div>
            </div>
        </div>


        <div>
            <form:errors path="body" element="p" cssStyle="color: red"/>
            <form:label path="body"><spring:message code="createArticle.body"/></form:label>
            <div class="form-group">
                <div class="input-group">
                    <form:textarea type="text" path="body" cssClass="form-control"/>
                </div>
            </div>
        </div>

        <div>
            <form:errors path="creatorEmail" element="p" cssStyle="color: red"/>
            <form:label path="creatorEmail"><spring:message code="createArticle.email"/></form:label>
            <div class="form-group">
                <spring:message code="createArticle.email.placeholder"  var="emailPlaceholder" />
                <form:input placeholder="${emailPlaceholder}" type="email" cssClass="form-control" path="creatorEmail"/>
            </div>
        </div>



        <%--<div class="form-group">
            <label for="FormControlFile">Imagen de la noticia</label>
            <input type="file" class="form-control-file" id="FormControlFile">
        </div>--%>

        <div class="dropdown">
            <button class="btn btn-primary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <spring:message code="createArticle.category.choose"/>
            </button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                <c:forEach var="category" items="${categories}">
                    <div class="form-check">
                        <form:checkbox path="categories" value="${category.interCode}" id="${category.interCode}"/>
                        <label class="form-check-label" for="${category.interCode}">
                            <spring:message code="${category.interCode}"/>
                        </label>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div style="width: 100%; display: flex; justify-content: end">
            <button class="btn btn-primary" type="submit"><spring:message code="createArticle.save"/></button>
        </div>
    </form:form>

</div>


</body>
</html>
