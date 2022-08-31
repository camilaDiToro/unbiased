<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 29/08/2022
  Time: 11:51
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<%@include file="../../resources/navbar.jsp" %>
<body>
<div style="display: flex; flex-direction: column; align-items: center; justify-content: center;" class="h-auto p-5">

    <%--preguntar si el tag de form-group es un tema de notacion o si es algo mas, porque estoy
    mezclando form group y form input--%>
    <form class="h-auto w-50">
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

        <div class="form-group">
            <label for="FormControlFile">Imagen de la noticia</label>
            <input type="file" class="form-control-file" id="FormControlFile">
        </div>

        <div style="width: 100%; display: flex; justify-content: end">
            <button class="btn btn-primary" type="submit">Guardar</button>
        </div>
    </form>

</div>


</body>
</html>
