<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<html>
<%@ include file="../../resources/navbar.jsp" %>
<div class="d-flex align-items-center justify-content-center h-100">
    <div class="text-center">
        <h1 class="display-1 fw-bold">404</h1>
        <p class="fs-1"> <span class="text-danger">Oops!</span> Page not found.</p>
        <p class="lead">
            The news article you're looking for doesn't exist.
        </p>
        <a href="<c:url value="/"/>" class="btn btn-primary">Go Home</a>
    </div>
</div>
</body>
</html>