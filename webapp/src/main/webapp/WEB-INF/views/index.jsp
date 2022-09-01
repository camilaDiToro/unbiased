<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<html>
<%@ include file="../../resources/navbar.jsp" %>
<div class="container-lg">
    <c:set var = "activeClasses" scope = "session" value = "bg-secondary active"/>
    <c:set var = "inactiveClasses" scope = "session" value = "text-secondary"/>
    <ul class="my-4 nav bg-primary nav-pills text-light p-2 rounded-lg d-flex ">

            <li class="nav-item">
                <a class="nav-link rounded-pill <c:out value = "${orderBy == 'Top' ? activeClasses : inactiveClasses}"/>" aria-current="page" href="Top">Top</a>
            </li>
            <li class="nav-item" >
                <a class="nav-link rounded-pill <c:out value = "${orderBy == 'New' ? activeClasses : inactiveClasses}"/>" href="New">New</a>
            </li>
            <li class="nav-item">
                <a class="nav-link rounded-pill <c:out value = "${orderBy == 'For me' ? activeClasses : inactiveClasses}"/>" href="For me">For me</a>
            </li>
        <li class="nav-item ml-auto">
            <a href="./create_article">
            <button type="button" class="btn btn-info">
                Create article
            </button></a>
        </li>


    </ul>
    <ul class="my-2 nav justify-content-center text-light p-2">
        <li class="nav-item">
            <a class="nav-link active" aria-current="page" href="#">Active</a>
        </li>
        <li class="nav-item" >
            <a class="nav-link " href="#">Link</a>
        </li>
        <li class="nav-item">
            <a class="nav-link " href="#">Link</a>
        </li>
    </ul>
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
<h2>Hi <c:out value="${user.email}"/>!</h2>
<h4>The user's id is <c:out value="${user.id}"/>!</h4>
<nav class="d-flex justify-content-center align-items-center">
    <ul class="pagination">
        <c:if test = "${page > 1}">
            <li class="page-item"><a class="page-link" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "1"/>
            </c:url>">First</a></li>
        </c:if>

        <c:forEach var = "i" begin = "${page - 1 >= 1 ? page - 1 : 1}" end = "${page + 1 <= totalPages ? page+1 : totalPages}">
            <li class="page-item"><a class="page-link ${i == page ? 'font-weight-bold' : ''}" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "${i}"/>
            </c:url>"><c:out value="${i}"/></a></li>
        </c:forEach>
        <c:if test = "${page < totalPages}">
            <li class="page-item"><a class="page-link" href="<c:url value = "/${orderBy}">
            <c:param name = "page" value = "${totalPages}"/>
            </c:url>">Last</a></li>
        </c:if>
    </ul>
</nav>
</body>
</html>