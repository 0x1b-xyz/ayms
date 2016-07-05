<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
<head>
    <title>Companies</title>
</head>
<body>

<div class="row">
    <h2>Companies</h2>
    <ol class="breadcrumb">
        <li><a href="<s:url value="/"/>">Home</a></li>
        <li class="active">Companies</li>
    </ol>
    <div class="table-responsive">
        <caption>table caption</caption>
        <table id="company-tbl" class="table table-hover">
            <thead>
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Address</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
</div>

<script id="company-tpl" type="text/x-handlebars-template">
    <tr>
        <td><a href="<s:url value="/company/{{id}}"/>">{{id}}</a></td>
        <td>{{name}}</td>
        <td>{{#if address.line1}}{{address.line1}} {{address.city}}, {{address.state}} {{address.zipCode}}{{/if}}</td>
        <td>
            <a href="<s:url value="/company/{{id}}/user"/>" class="btn btn-info" role="button">Users</a>
            <a href="<s:url value="/company/{{id}}/client"/>" class="btn btn-default" role="button">Clients</a>
        </td>
    </tr>
</script>

<script type="text/javascript">
    jQuery(document).ready(function() {
        rows('', '#company-tpl', '#company-tbl')
    })
</script>

</body>
</html>
