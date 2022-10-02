<head>
  <title>
    unbiased&nbsp;-&nbsp;
    <c:if test="${textType.toString() == 'LITERAL'}">
      <c:out value="${pageTitle}"/>
    </c:if>
    <c:if test="${textType.toString() == 'INTERCODE'}">
      <spring:message code="${pageTitle}" arguments="${stringParams}"/>
    </c:if>
  </title>
  <link rel="icon" type="image/png" href="<c:url value="/resources/images/unbiased-logo-circle.png"/>">
  <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet">
  <c:if test="${signInOrCreate}">
    <link href="<c:url value="/resources/css/signin.css"/>" rel="stylesheet">
    <script src="<c:url value="/resources/js/show_pwd.js"/>"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
  </c:if>
  <link href="<c:url value="/resources/css/custom.css"/>" rel="stylesheet">

  <%--    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">--%>
  <%--<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>--%>
  <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
  <script src="<c:url value="/resources/js/script.js"/>"></script>
</head>
