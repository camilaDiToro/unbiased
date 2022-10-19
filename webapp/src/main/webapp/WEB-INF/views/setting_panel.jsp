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
                <ul class="list-group">
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Cambio de rol
                        <span>
                          <label class="switch">
                            <input type="checkbox" checked>
                            <span class="slider round"></span>
                          </label>
                        </span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Nuevos seguidores
                        <span>
                          <label class="switch">
                            <input type="checkbox" checked>
                            <span class="slider round"></span>
                          </label>
                        </span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Comentarios
                        <span>
                          <label class="switch">
                            <input type="checkbox" checked>
                            <span class="slider round"></span>
                          </label>
                        </span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Creador del dia
                        <span>
                          <label class="switch">
                            <input type="checkbox" checked>
                            <span class="slider round"></span>
                          </label>
                        </span>
                    </li>
                </ul>
            </div>
        </div>
    </div>

</body>
</html>
