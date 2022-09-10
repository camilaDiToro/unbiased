


<nav style="display: flex" class="navbar navbar-dark navbar-expand-sm bg-primary text-white">

    <div style="display: flex" class="container-fluid" style="border: solid red">
        <a class="navbar-brand  " href="<c:url value="/TOP"/>">
            <img src="<c:url value="/resources/unbiased_navbar.png"/>" height="35" alt="" class="d-inline-block align-middle mr-2">
            <span class="text-info">unbiased</span>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="d-flex">
            <form class="form-inline my-2 my-lg-0" method="GET" action="<c:url value="/TOP"/>">
                <spring:message code="navbar.search"  var="searchPlaceholder" />
                <input style="height: 30px; padding-left: 35px; background-image: url('<c:url value="/resources/images/lupa.png"/>'); background-repeat: no-repeat; background-position: left center; background-size: 10%;" class="form-control mr-sm-2 btn-outline-info text-white bg-primary" type="search" placeholder="${searchPlaceholder}" id="query" name="query"/>
            </form>
        </div>
    </div>
</nav>
