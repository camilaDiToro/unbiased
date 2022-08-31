<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<html>
<%@include file="../../resources/navbar.jsp" %>
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
            <a href="./createArticle">
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
                            <h5 class="card-title"><c:out value="${article.title}"/></h5>
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
</body>
</html>