<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@ include file="../../resources/jsp/head.jsp" %>
<body>
<div class="d-flex h-100 flex-column">
  <%@ include file="../../resources/jsp/navbar.jsp" %>
  <div class="d-flex flex-column h-100">
    <div class="flex-grow-1 d-flex flex-row">

      <%--LEFT SIDE--%>
      <%@ include file="../../resources/jsp/moderation-left-side.jsp" %>

      <%--RIGHT SIDE--%>
      <div class="d-flex w-75 flex-column">
        <div class="card mb-4">
          <div class="card-body text-center">
            <div class="img-container-navbar">
              <c:if test="${loggedUser.hasImage()}">
                <img src="<c:url value="/profile/${loggedUser.imageId}/image"/>" alt=""
                     class="rounded-circle img-fluid object-fit-cover mr-1" style="width: 150px;">
              </c:if>

              <c:if test="${!loggedUser.hasImage()}">
                <img src="<c:url value="/resources/images/profile-image.png"/>" alt=""
                     class="rounded-circle img-fluid object-fit-cover mr-1" style="width: 150px;">
              </c:if>
            </div>

            <h5 class="my-3 text-white"><c:out value="${loggedUser}"/></h5>
            <p class="mb-1">You havent posted anything that is affecting yout account status</p>
            <br/>
            <div class="d-flex justify-content-center mb-2">
              <button type="button" class="btn btn-secondary">Create new post</button>
            </div>
          </div>
        </div>
      </div>
</body>
</html>
