<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<%@ include file="../../resources/navbar.jsp" %>
<div class="container-lg">
    <c:set var = "activeClasses" scope = "session" value = "bg-secondary active"/>
    <c:set var = "inactiveClasses" scope = "session" value = "text-secondary"/>
    <ul class="my-4 nav bg-primary nav-pills text-light p-2 rounded-lg d-flex ">
        <c:forEach var="order" items="${orders}">
            <li class="nav-item">
                <a class="text-capitalize nav-link rounded-pill <c:out value = "${orderBy == order ? activeClasses : inactiveClasses}"/>" aria-current="page" href="<c:url value = "/${order}">
                    <c:param name = "category" value = "${category}"/>
                    <c:if test="${!empty query}"><c:param name = "query" value = "${param.query}"/></c:if>
                    </c:url>"><c:out value="${order}"/></a>
            </li>
        </c:forEach>
        <li class="nav-item ml-auto">
            <a href="./create_article">
            <button type="button" class="btn btn-info">
                <spring:message code="home.createArticle.button"/>
            </button></a>
        </li>


    </ul>
    <c:if test="${query == ''}">
        <ul class="my-2 nav nav-tabs justify-content-center text-light p-2">
            <li class="nav-item">
                <a class="text-capitalize nav-link <c:out value = "${category.toString() == 'ALL' ? 'active' : ''}"/>" aria-current="page" href="<c:url value = "/${orderBy}">
                    <c:param name = "query" value = "${param.query}"/>
                    </c:url>">ALL</a>
            </li>
            <c:forEach var="cat" items="${categories}">
                <li class="nav-item">
                    <a class="text-capitalize nav-link <c:out value = "${category.toString() != 'ALL' && category == cat ? 'active': ''}"/>" aria-current="page" href="<c:url value = "/${orderBy}">
                    <c:param name = "category" value = "${cat}"/>

                    </c:url>"><c:out value="${cat}"/></a>
                </li>
            </c:forEach>
        </ul>
    </c:if>
    <c:if test="${query != ''}">
        <div class="m-4">
            <a href="<c:url value="/${orderBy}"/>">Cancelar filtro: "${query}"</a>
        </div>
    </c:if>
    <div class="container-fluid">
        <div class="row row-cols-1 row-cols-md-3">
            <c:set var="maxLength" value="${100}"/>
            <c:forEach var="article" items="${news}">
                <div class="col mb-4">
                    <div class="card h-100">
                        <img src="<c:url value="/resources/img_1.jpeg"/>" class="card-img-top" alt="...">
                        <div class="card-body">
                            <a href="<c:url value="/news/${article.newsId}"/>"><h5 class="card-title"><c:out value="${article.title}"/></h5></a>
                            <h6 class="card-subtitle py-1"><c:out value="${article.subtitle}"/></h6>
                            <p class="card-text"><c:out value="${fn:substring(article.body, 0, maxLength)}${fn:length(article.body) > maxLength ? '...' : ''}"/></p>
                        </div>
                    </div>
                </div>
            </c:forEach>

        </div>

    </div>
</div>
<nav class="d-flex justify-content-center align-items-center">
    <ul class="pagination">

            <li class="page-item"><a class="page-link" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "1"/>
            <c:param name = "query" value = "${param.query}"/>
            </c:url>"><spring:message code="home.pagination.first"/></a></li>


        <c:forEach var = "i" begin = "${minPage}" end = "${maxPage}">
            <li class="page-item"><a class="page-link ${i == page ? 'font-weight-bold' : ''}" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "${i}"/>
            <c:param name = "query" value = "${param.query}"/>
            </c:url>"><c:out value="${i}"/></a></li>
        </c:forEach>

            <li class="page-item"><a class="page-link" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "${totalPages}"/>
            <c:param name = "query" value = "${param.query}"/>
            </c:url>"><spring:message code="home.pagination.last"/></a></li>

    </ul>
</nav>
</body>
</html>