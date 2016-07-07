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

    <link href="<c:url value="/static/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/static/css/app.css"/>" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <!--<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>-->
    <!--<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>-->
    <%--<![endif]-->--%>

    <script src="<c:url value="/static/js/jquery-2.2.4.js"/>"></script>
    <script src="<c:url value="/static/js/jquery.serializejson.js"/>"></script>
    <script src="<c:url value="/static/js/jquery.mask.js"/>"></script>
    <script src="<c:url value="/static/js/handlebars-v4.0.5.js"/>"></script>
    <script src="<c:url value="/static/js/handlebars-intl.js"/>"></script>
    <script src="<c:url value="/static/js/bootstrap.js"/>"></script>
    <script src="<c:url value="/static/js/app.js"/>"></script>

    <dec:head/>
</head>
<body>

<div id="wrapper">

    <div id="sidebar-wrapper">
        <ul class="sidebar-nav">
            <li class="sidebar-brand">
                <a href="<s:url value="/"/>">ayms</a>
            </li>
            <li>
                <a href="<s:url value="/company"/>">Companies</a>
            </li>
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

    HandlebarsIntl.registerWith(Handlebars);

    jQuery(document).ready(function() {

        $("#menu-toggle").click(function(e) {
            e.preventDefault();
            $("#wrapper").toggleClass("toggled");
        });

    });
</script>

</body>