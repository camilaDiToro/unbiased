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
<style>
    <c:set var="labelText"><spring:message code="createArticle.label"/></c:set>
    .custom-file-input~.custom-file-label::after{content:'${labelText}'!important}
</style>
<%@include file="../../resources/head.jsp" %>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">
<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>

<body>
<%@include file="../../resources/navbar.jsp" %>
<div class="back-button" >
    <input class="back-button-img" type="image" src="<c:url value="/resources/images/back_to_prev.png"/>" alt="..."  data-toggle="modal" data-target="#exampleModal">
</div>
<div class="d-flex flex-col align-items-center justify-content-center p-5">


    <c:url value="/create_article" var="postUrl"/>
    <form:form modelAttribute="createNewsForm" enctype="multipart/form-data" action="${postUrl}" method="post" cssClass="h-auto w-50">

        <div>
            <form:label path="title"><spring:message code="createArticle.title"/></form:label>
            <div class="form-group">
                <div class="input-group mb-3">
                    <spring:message code="createArticle.title.placeholder" var="titlePlaceholder" />
                    <form:input placeholder="${titlePlaceholder}" cssClass="form-control ${validate && errors != null && errors.getFieldErrorCount('title') > 0 ? 'is-invalid' : validate ? 'is-valid' : ''}"  type="text" path="title"/>
                    <form:errors cssClass="invalid-feedback" path="title" element="p"/>

                </div>
            </div>
        </div>


        <div>
            <form:label path="subtitle"><spring:message code="createArticle.description"/></form:label>
            <div class="form-group">
                <div class="input-group mb-3">
                    <spring:message code="createArticle.description.placeholder"  var="descPlaceholder" />
                    <form:input type="text" path="subtitle" cssClass="form-control ${validate && errors != null && errors.getFieldErrorCount('subtitle') > 0 ? 'is-invalid' : validate ? 'is-valid' : ''}" placeholder="${descPlaceholder}"/>
                    <form:errors cssClass="invalid-feedback" path="subtitle" element="p"/>
                </div>
            </div>
        </div>


        <div>
            <form:label path="body"><spring:message code="createArticle.body"/></form:label>
            <div class="form-group">
                <form:textarea id="body-text" type="text" path="body" cssClass="form-control ${validate && errors != null && errors.getFieldErrorCount('body') > 0 ? 'is-invalid' : validate ? 'is-valid' : ''}"/>
                <form:errors path="body" element="p" cssClass="invalid-feedback"/>
            </div>

        </div>

    <form:label path="image"><spring:message code="createArticle.imageMsg"/> </form:label>
    <div class="input-group mb-3">
            <div class="custom-file">
                <form:input id="fileInput" type="file" path="image" accept="image/png, image/jpeg" cssClass="custom-file-input ${validate && errors != null && errors.getFieldErrorCount('image') > 0 ? 'is-invalid' : validate ? 'is-valid' : ''}"/>
                <form:label path="image" cssClass="custom-file-label" for="inputGroupFile01"><spring:message code="createArticle.selectFile"/></form:label>

            </div>


        <script>
            $('#fileInput').on('change',function(){
                //get the file name
                var fileName = $(this).val();
                //replace the "Choose a file" label
                $(this).next('.custom-file-label').html(fileName);
            })
        </script>
        </div>
    <form:errors path="image" element="div" cssClass="text-danger mb-3"  />
    <div class="dropdown" id="categories-dropdown">
        <button class="btn btn-primary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <spring:message code="createArticle.category.choose"/>
        </button>
        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
            <c:forEach var="category" items="${categories}">
                <div class="form-check  w-100">
                    <form:checkbox path="categories" value="${category.interCode}" id="${category.interCode}"/>
                    <label class="form-check-label" for="${category.interCode}">
                        <spring:message code="${category.interCode}"/>
                    </label>
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="w-100 d-flex justify-content-end">
        <button class="btn btn-primary" type="submit"><spring:message code="createArticle.save"/></button>
    </div>
    </div>






</div>



    </form:form>
    <script>
        var simplemde = new SimpleMDE({ element: document.getElementById("body-text") , spellChecker: false});
    </script>
    <!-- Modal -->
    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel"><spring:message code="createArticle.modal.question"/></h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <spring:message code="createArticle.modal.msg"/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code="createArticle.modal.cancel"/></button>
                    <a href="./TOP">
                        <button type="button" class="btn btn-primary"><spring:message code="createArticle.modal.accept"/></button>
                    </a>

                </div>
            </div>
        </div>
    </div>

</div>


</body>
</html>
