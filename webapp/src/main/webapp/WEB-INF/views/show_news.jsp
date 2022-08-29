<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<body>
<h2><c:out value="${news.title}"/></h2>
<h4><c:out value="${news.subtitle}"/></h4>
<p><c:out value="${news.body}"/></p>
<p>Fecha: <c:out value="${news.creationDate}"/></p>
<p>Creador <c:out value="${news.creator.email}"/></p>

</body>
</html>