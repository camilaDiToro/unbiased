<%--
  Created by IntelliJ IDEA.
  User: khcatino
  Date: 29/09/2022
  Time: 13:11
  To change this template use File | Settings | File Templates.
--%>
<link href="<c:url value="/resources/css/custom.css"/>" rel="stylesheet">
<div class=" w-25 d-flex flex-column border-info border-right mt-4 m-3 ">

  <h3 class="text-secondary"><spring:message code="moderation.panel"/></h3>
  <ul class="nav flex-column vertical-menu ">
    <li class="nav-item">
      <div class="d-flex flex-row">
        <img class="moderation-img" src="<c:url value="/resources/images/warning-svgrepo-com.svg"/>" alt="...">
        <a class="nav-link selected pl-0" href="<c:url value="/admin/reported_news"/>"><spring:message code="moderation.reportedArticles"/></a>
      </div>
    </li>
    <li>
      <div class="d-flex flex-row">
        <img class="moderation-img" src="<c:url value="/resources/images/warning-svgrepo-com.svg"/>" alt="...">
        <a class="nav-link selected pl-0" href="<c:url value="/admin/add_admin_page"/>">
          Add admin
        </a>
      </div>
    </li>

  </ul>


</div>
