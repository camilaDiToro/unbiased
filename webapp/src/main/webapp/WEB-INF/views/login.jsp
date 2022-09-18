<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<c:url value="/login" var="loginUrl" />
<form action="${loginUrl}" method="post"> <! -- enctype="application/x-www-form-urlencoded" -->
    <div>
        <label for="username">Email: </label>
        <input id="username" name="username" type="text"/>
    </div>
    <div>
        <label for="password">Password: </label>
        <input id="password" name="password" type="password"/>
    </div>
    <div>
        <label>
            <input name="rememberme" type="checkbox"/>
            Remember me:
        </label>
    </div>
    <div>
        <input type="submit" value="Login!"/>
    </div>
    <c:if test="${param.error}">
        No se pudo iniciar sesion. El mail ingresado o la contraseña no son correctas, o bien la cuenta no fue verificada
    </c:if>
</form>
</body>
</html>