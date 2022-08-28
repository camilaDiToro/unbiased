<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<body>
<h2>Hi <c:out value="${user.email}"/>!</h2>
<h4>The user's id is <c:out value="${user.id}"/>!</h4>
</body>
</html>