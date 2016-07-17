<%@ taglib prefix="dec" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!doctype html>
<html class="no-js" lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ayms - <dec:title default="default"/></title>

    <link href="<c:url value="/assets/app.css"/>" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <!--<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>-->
    <!--<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>-->
    <%--<![endif]-->--%>

    <script src="<c:url value="/assets/app.js"/>"></script>

    <script type="text/javascript">
        URL_PREFIX = '<s:url value="/"/>';
    </script>

    <dec:head/>
</head>
<body>

<div id="wrapper">

    <div id="sidebar-wrapper">
        <ul class="sidebar-nav">
            <li class="sidebar-brand"><a href="<s:url value="/"/>">ayms</a></li>
            <li><a href="<s:url value="/company"/>">Companies</a></li>
            <li><a href="<s:url value="/employee"/>">Employees</a></li>
            <li><a href="<s:url value="/client"/>">Clients</a></li>
            <li><a href="<s:url value="/form/definition"/>">Forms</a></li>
        </ul>
    </div>

    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row">
                <div class="col-lg-12">
                    <div class="container-fluid">
                        <dec:body/>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<script>

    jQuery(document).ready(function() {

        $("#menu-toggle").click(function(e) {
            e.preventDefault();
            $("#wrapper").toggleClass("toggled");
        });

    });
</script>

</body>