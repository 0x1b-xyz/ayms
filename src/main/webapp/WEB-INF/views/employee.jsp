<%@ page import="io.stiefel.ayms.domain.Employee" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
<head>
    <title>Employees</title>
</head>
<body>

<div class="row">

    <h2>Employees</h2>
    <ol class="breadcrumb">
        <li><a href="<s:url value="/"/>">Home</a></li>
        <li><a href="<s:url value="/company/${companyId}"/>">Company</a>
        </li>
        <li class="active">Employees</li>
    </ol>
    <div class="table-responsive">
        <caption>table caption</caption>
        <table id="employee-tbl" class="table table-hover">
            <thead>
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Role</th>
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

    <form class="form-horizontal" id="employee-frm" method="post">
        <div class="form-group">
            <label for="name" class="col-sm-2 control-label">Employee</label>
            <div class="col-sm-8">
                <input name="name" id="name" type="text" class="form-control" placeholder="Name">
            </div>
        </div>
        <div class="form-group">
            <label for="firstName" class="col-sm-2 control-label">Name</label>
            <div class="col-sm-8">
                <div class="form-group row">
                    <div class="col-md-6">
                        <input name="firstName" type="text" class="form-control" id="firstName" placeholder="First">
                    </div>
                    <div class="col-md-6">
                        <input name="lastName" type="text" class="form-control" id="lastName" placeholder="Last">
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label for="role" class="col-sm-2 control-label">Role</label>
            <div class="col-sm-8">
                <select name="role" id="role" class="form-control">
                    <c:set var="roles" value="<%=Employee.Role.values()%>"/>
                    <c:forEach items="${roles}" var="role" varStatus="i">
                        <option value="${role}">${role}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-success">Save</button>
            </div>
        </div>
    </form>

</div>

<script id="employee-tpl" type="text/x-handlebars-template">
    <tr>
        <td><a href="<s:url value="/company/{{company}}/employee/{{id}}"/>">{{id}}</a></td>
        <td>{{name}}</td>
        <td>{{firstName}}</td>
        <td>{{lastName}}</td>
        <td>{{role}}</td>
    </tr>
</script>

<script type="text/javascript">
    jQuery(document).ready(function () {
        rows('', '#employee-tpl', '#employee-tbl');
        form('#employee-frm', '#employee-tpl', '#employee-tbl');
    })
</script>

</body>
</html>