<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 29/08/2022
  Time: 11:51
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <title>unbiased - Home Page</title>
    <link rel="icon" type="image/png" href="<c:url value="/resources/unbiased-logo-circle.png"/>">
    <link href="<c:url value="/resources/bootstrap.min.css"/>" rel="stylesheet">
    <%--    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">--%>
    <%--<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>--%>
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
</head>

<body>
<nav class="navbar navbar-dark navbar-expand-sm bg-primary text-white">

    <div class="container-fluid">
        <a class="navbar-brand text-info " href="#">
            unbiased
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <!-- Links -->
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="#">Link 1</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Link 2</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Link 3</a>
            </li>
        </ul>
    </div>
</nav>

<%--
<div style="border: solid red; display: flex; justify-content: center; align-items: center; width: 100%; height: 100%; flex-direction: column}">

    <div style="border: solid red; display: flex">

        hola mundo
        &lt;%&ndash;<div class="input-group mb-3" style="display: flex; width: 50%; flex-direction: row">
            <input type="text" class="form-control" placeholder="titulo" aria-label="Indicar titulo">
        </div>

        <div class="input-group mb-3" style="display: flex; width: 50%; flex-direction: row">
            <input type="text" class="form-control" placeholder="titulo" aria-label="Indicar titulo">
        </div>&ndash;%&gt;

    </div>

    <div style="border: solid red; display: flex">
        hola mundo2
    </div>

</div>
--%>

<div style="display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100%">

    <%--preguntar si el tag de form-group es un tema de notacion o si es algo mas, porque estoy
    mezclando form group y form input--%>
    <form style="width: 50%">
        <div class="form-group">
            <div class="input-group mb-3">
                <input type="text" class="form-control" placeholder="Titulo">
            </div>
        </div>

        <div class="form-group">
            <div class="input-group mb-3">
                <input type="text" class="form-control" placeholder="Descripcion">
            </div>
        </div>

        <div class="form-group">
            <div class="input-group">
                <textarea class="form-control" aria-label="With textarea"></textarea>
            </div>
        </div>

        <div class="form-group">
            <label for="exampleInputEmail1">Direccion de Email</label>
            <input type="email" class="form-control" id="exampleInputEmail1">
        </div>

        <div style="width: 100%; display: flex; justify-content: end">
            <button class="btn btn-primary" type="submit">Guardar</button>
        </div>
    </form>

</div>


</body>
</html>
