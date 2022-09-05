<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 29/08/2022
  Time: 11:51
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<%@include file="../../resources/navbar.jsp" %>
<body>

<div style="position: absolute ; margin-left: 4%; margin-top: 2%">
    <a href="./TOP">
    <input type="image" src="<c:url value="/resources/images/back_to_prev.png"/>" alt="..." style="max-width: 7%; max-height: 7%">
    </a>
</div>
<div style="display: flex; flex-direction: column; align-items: center; justify-content: center" class="h-auto p-5">

    <c:url value="/create_article" var="postUrl"/>
    <form:form modelAttribute="createNewsForm" enctype="multipart/form-data" action="${postUrl}" method="post" cssClass="h-auto w-50">

        <div>
            <form:errors path="title" element="p" cssStyle="color: red"/>
            <form:label path="title">Titulo</form:label>
            <div class="form-group">
                <div class="input-group mb-3">
                    <form:input placeholder="Este es un ejemplo del titulo" cssClass="form-control"  type="text" path="title"/>
                </div>
            </div>
        </div>


        <div>
            <form:errors path="subtitle" element="p" cssStyle="color: red"/>
            <form:label path="subtitle">Descripcion</form:label>
            <div class="form-group">
                <div class="input-group mb-3">
                    <form:input type="text" path="subtitle" cssClass="form-control" placeholder="Esto es un ejemplo de la descripcion de una notia"/>
                </div>
            </div>
        </div>


        <div>
            <form:errors path="body" element="p" cssStyle="color: red"/>
            <form:label path="body">Cuerpo de la noticia</form:label>
            <div class="form-group">
                <div class="input-group">
                    <form:textarea type="text" path="body" cssClass="form-control"/>
                </div>
            </div>
        </div>

        <div>
            <form:errors path="creatorEmail" element="p" cssStyle="color: red"/>
            <form:label path="creatorEmail">Direccion de Email:</form:label>
            <div class="form-group">
                <form:input placeholder="ejemplo@ejemplo.com" type="email" cssClass="form-control" path="creatorEmail"/>
            </div>
        </div>



        <%--<div class="form-group">
            <label for="FormControlFile">Imagen de la noticia</label>
            <input type="file" class="form-control-file" id="FormControlFile">
        </div>--%>

        <div class="dropdown">
            <button class="btn btn-primary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Elegir categoria
            </button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">

                <div class="form-check">
                        <%--<input class="form-check-input" type="checkbox" value="" id="checkTourism">--%>
                    <form:checkbox path="categories" value="TURISMO" id="checkTourism"/>
                    <label class="form-check-label" for="checkTourism">
                        Turismo
                    </label>
                </div>

                <div class="form-check">
                    <form:checkbox path="categories" value="ESPECTÁCULO" id="checkEntertainment"/>
                    <label class="form-check-label" for="checkEntertainment">
                        Espectaculo
                    </label>
                </div>

                <div class="form-check">
                    <form:checkbox path="categories" value="POLÍTICA" id="checkPolitics"/>
                    <label class="form-check-label" for="checkPolitics">
                        Politica
                    </label>
                </div>

                <div class="form-check">
                    <form:checkbox path="categories" value="ECONOMÍA" id="checkEconomy"/>
                    <label class="form-check-label" for="checkEconomy">
                        Economia
                    </label>
                </div>

                <div class="form-check">
                    <form:checkbox path="categories" value="DEPORTES" id="checkSports"/>
                    <label class="form-check-label" for="checkSports">
                        Deportes
                    </label>
                </div>

                <div class="form-check">
                    <form:checkbox path="categories" value="TECNOLOGÍA" id="checkTecnology"/>
                    <label class="form-check-label" for="checkTecnology">
                        Tecnologia
                    </label>
                </div>
            </div>
        </div>

        <div style="width: 100%; display: flex; justify-content: end">
            <button class="btn btn-primary" type="submit">Guardar</button>
        </div>
    </form:form>

</div>


</body>
</html>
