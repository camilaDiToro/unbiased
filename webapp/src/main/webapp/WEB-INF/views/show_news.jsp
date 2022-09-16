<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<script src="<c:url value="/resources/upvote-script.js"/>"></script>

<link href="<c:url value="/resources/custom.css"/>" rel="stylesheet">
<body>
<c:set var="user" scope="request" value="${loggedUser}"/>

<%@include file="../../resources/navbar.jsp" %>
<div style="position: absolute ; margin-left: 4%; margin-top: 2%">
    <a href="../TOP">
        <input type="image" src="<c:url value="/resources/images/back_to_prev.png"/>" alt="..." style="max-width: 7%; max-height: 7%">
    </a>
</div>

<div class="d-flex align-items-center justify-content-center w-100 py-4">
    <div class="h-auto w-75">
        <h1 class="text-xl-center"><c:out value="${news.title}"/></h1>
        <hr class="hr-style"/>
        <%--<img src="<c:url value="/resources/stock_photo.webp"/>" class="w-50 m-4 rounded mx-auto d-block img-thumbnail"/>--%>
        <img src="<c:url value="/resources/stock_photo.webp"/>" class="float-sm-right w-50 m-4"/>
        <%--<h4 class="text-lg-left"><c:out value="${news.subtitle}"/></h4>--%>
        <p class="text-sm-left text-secondary"><c:out value="${date}"/></p>
        <p><spring:message code="showNews.createdBy"/><c:out value=" "/><b><c:out value="${user.username != null ? user.username : user.email}"/></b></p>
        <p class="text-secondary">&nbsp; - Periodista de Telam (Buenos aires)</p>
        <p class="text-sm-left text-secondary">20 min read</p>
        <p class="text-sm-left">Califica el articulo: </p>
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-hand-thumbs-up-fill" viewBox="0 0 20 20">
            <path d="M6.956 1.745C7.021.81 7.908.087 8.864.325l.261.066c.463.116.874.456 1.012.965.22.816.533 2.511.062 4.51a9.84 9.84 0 0 1 .443-.051c.713-.065 1.669-.072 2.516.21.518.173.994.681 1.2 1.273.184.532.16 1.162-.234 1.733.058.119.103.242.138.363.077.27.113.567.113.856 0 .289-.036.586-.113.856-.039.135-.09.273-.16.404.169.387.107.819-.003 1.148a3.163 3.163 0 0 1-.488.901c.054.152.076.312.076.465 0 .305-.089.625-.253.912C13.1 15.522 12.437 16 11.5 16H8c-.605 0-1.07-.081-1.466-.218a4.82 4.82 0 0 1-.97-.484l-.048-.03c-.504-.307-.999-.609-2.068-.722C2.682 14.464 2 13.846 2 13V9c0-.85.685-1.432 1.357-1.615.849-.232 1.574-.787 2.132-1.41.56-.627.914-1.28 1.039-1.639.199-.575.356-1.539.428-2.59z"/>
        </svg>
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-hand-thumbs-down-fill" viewBox="0 0 20 20">
            <path d="M6.956 14.534c.065.936.952 1.659 1.908 1.42l.261-.065a1.378 1.378 0 0 0 1.012-.965c.22-.816.533-2.512.062-4.51.136.02.285.037.443.051.713.065 1.669.071 2.516-.211.518-.173.994-.68 1.2-1.272a1.896 1.896 0 0 0-.234-1.734c.058-.118.103-.242.138-.362.077-.27.113-.568.113-.856 0-.29-.036-.586-.113-.857a2.094 2.094 0 0 0-.16-.403c.169-.387.107-.82-.003-1.149a3.162 3.162 0 0 0-.488-.9c.054-.153.076-.313.076-.465a1.86 1.86 0 0 0-.253-.912C13.1.757 12.437.28 11.5.28H8c-.605 0-1.07.08-1.466.217a4.823 4.823 0 0 0-.97.485l-.048.029c-.504.308-.999.61-2.068.723C2.682 1.815 2 2.434 2 3.279v4c0 .851.685 1.433 1.357 1.616.849.232 1.574.787 2.132 1.41.56.626.914 1.28 1.039 1.638.199.575.356 1.54.428 2.591z"/>
        </svg>

        <%--<div style="margin-top: 20px" class="w-50 d-flex flex-wrap">--%>
        <div style="margin-top: 25px">
            <c:forEach var="category" items="${categories}">
                <p>
                    <span class="badge badge-pill badge-info"><spring:message code="${category.interCode}"/></span>
                </p>
            </c:forEach>
        </div>

        <div class="w-50 d-flex flex-wrap align-items-center gap-1">
            <div class="text-sm-left font-weight-bold">
                Tags:
            </div>
            <span class="badge badge-pill badge-primary">Messi</span>
        </div>

<%--        <p class="article-body"><c:out value="${news.body}"/></p>--%>
        <div class="d-flex w-100 justify-content-center align-items-center">
            <div class="article-subtitle">
                <c:out value="${news.subtitle}" escapeXml="false"/>
            </div>
        </div>
        <br/>
        <div class="d-flex w-100 justify-content-center align-items-center">
            <div class="article-body">
                <c:out value="${news.body}" escapeXml="false"/>
            </div>

        </div>

        <form action=""  method="get">
            <label for="comment" class="form-label"><spring:message code="showNews.comments"/></label><br>
            <input type="text" class="form-control" id="comment" value="Comentario"><br>
            <button type="button" class="btn btn-info">
                <spring:message code="showNews.comment.submit"/>
            </button></a>
            <input type="submit" class="btn btn-info" value="Comentar">
        </form>


<%--        <c:if test="${news.hasImage()}">--%>
<%--        <img src="<c:url value="/news/${newsId}/image"/>" class="user-section-img"/>--%>
<%--        </c:if>--%>

    </div>
</div>


</body>
</html>