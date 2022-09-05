<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: khcatino
  Date: 29/08/2022
  Time: 16:49
  To change this template use File | Settings | File Templates.
--%>
<head>
<title>
<c:out value="unbiased - Homepage"/></title>
    <link rel="icon" type="image/png" href="<c:url value="/resources/unbiased-logo-circle.png"/>">
    <link href="<c:url value="/resources/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/custom.css"/>" rel="stylesheet">

<%--    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">--%>
    <%--<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>--%>
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
</head>

<body><nav style="display: flex" class="navbar navbar-dark navbar-expand-sm bg-primary text-white">

    <div style="display: flex" class="container-fluid" style="border: solid red">
        <a class="navbar-brand  " href="/webapp_war_exploded">
            <img src="<c:url value="/resources/unbiased_navbar.png"/>" height="35" alt="" class="d-inline-block align-middle mr-2">
            <span class="text-info">unbiased</span>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <!-- Links -->
        <%--<ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="#">Link 1</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Link 2</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Link 3</a>
            </li>
        </ul>--%>

        <div class="d-flex">
            <form class="form-inline my-2 my-lg-0" method="GET" action="<c:url value="/TOP"/>">
                <spring:message code="navbar.search"  var="searchPlaceholder" />
                <input style="height: 30px; padding-left: 35px; background-image: url('<c:url value="/resources/images/lupa.png"/>'); background-repeat: no-repeat; background-position: left center; background-size: 10%;" class="form-control mr-sm-2 btn-outline-info text-white bg-primary" type="search" placeholder="${searchPlaceholder}" id="query" name="query"/>
            </form>
        </div>

    </div>


</nav>
