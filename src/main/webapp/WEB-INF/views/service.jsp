<%@ page import="io.stiefel.ayms.domain.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
<head>
    <title>Services</title>
</head>
<body>

<div class="row">

    <h2>Services</h2>
    <ol class="breadcrumb">
        <li><a href="<s:url value="/"/>">Home</a></li>
        <li><a href="<s:url value="/company/${companyId}"/>">Company</a></li>
        <li><a href="<s:url value="/company/${companyId}/client/${clientId}"/>">Client</a></li>
        <li class="active">Services</li>
    </ol>
    <div class="table-responsive">
        <caption>table caption</caption>
        <table id="service-tbl" class="table table-hover">
            <thead>
            <tr>
                <th>#</th>
                <th>User</th>
                <th>Scheduled</th>
                <th>Arrived</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>

</div>

<div class="row">
    <h2>Create</h2>

    <div class="hidden alert alert-danger" role="alert"><strong>Oh snap!</strong> Change a few things up and try
        submitting again.
    </div>

    <form class="form-horizontal" id="service-frm" method="post">

        <div class="form-group">
            <label for="user" class="col-sm-2 control-label">User</label>
            <div class="col-sm-8">
                <select id="user" class="form-control" name="user">
                    <c:forEach items="${users}" var="user">
                        <option value="${user.id}">${user.lastName}, ${user.firstName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label for="scheduled" class="col-sm-2 control-label">Scheduled</label>
            <div class="col-sm-8">
                <input name="scheduled" type="datetime-local" class="form-control" id="scheduled"/>
            </div>
        </div>

        <div class="form-group">
            <label for="arrived" class="col-sm-2 control-label">Arrived</label>
            <div class="col-sm-8">
                <input name="arrived" type="datetime-local" class="form-control" id="arrived"/>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-success">Save</button>
            </div>
        </div>
    </form>

</div>

<script id="service-tpl" type="text/x-handlebars-template">
    <tr>
        <td><a href="<s:url value="/company/{{company}}/client/{{client}}/service/{{id}}"/>">{{id}}</a></td>
        <td>{{user}}</td>
        <td>{{formatDate scheduled day="numeric" month="long" year="numeric"}} {{formatTime scheduled hour="numeric" minute="numeric"}}</td>
        <td>{{formatDate arrived day="numeric" month="long" year="numeric"}} {{formatTime arrived hour="numeric" minute="numeric"}}</td>
    </tr>
</script>

<script type="text/javascript">
    jQuery(document).ready(function () {

        rows('', '#service-tpl', '#service-tbl');
        form('#service-frm', '#service-tpl', '#service-tbl');
    })
</script>

</body>
</html>
