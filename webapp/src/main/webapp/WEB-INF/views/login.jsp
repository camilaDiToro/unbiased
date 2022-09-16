<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<c:set var="pageTitle" scope="request" value="Login"/>
<c:set var="signInOrCreate" scope="request" value="${true}"/>
<%@include file="../../resources/head.jsp" %>
<body class="text-center">
<c:url value="/login" var="loginUrl" />

<form class="form-signin" action="${loginUrl}" method="post">
    <h1 class="logo mb-4 text-info">unbiased</h1>
    <h1 class="h3 mb-3 font-weight-normal text-light">Please sign in</h1>

        <div>
            <label for="username" class="sr-only">Email address</label>
            <input type="text" id="username" name="username" class="form-control" placeholder="Email address" required="" autofocus="">

        </div>
        <div>
            <label for="password" class="sr-only">Password</label>
            <input name="password" type="password" id="password" class="form-control" placeholder="Password">

        </div>
   <div class="checkbox mb-3">
        <label class="text-light">
            <input type="checkbox" name="rememberme"> Remember me
        </label>

    </div>
    <button class="btn btn-lg btn-info btn-block" type="submit">Sign in</button>
    <p class="mt-5 mb-3 text-muted">© 2022-2022</p>
    </form>

</body>
</html>