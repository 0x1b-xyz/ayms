<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
<head>
    <title>Form Definitions</title>
</head>
<body>

<div class="row">

    <h2>Form Definitions</h2>
    <ol class="breadcrumb">
        <li><a href="<s:url value="/"/>">Home</a></li>
        <li class="active">Form Definitions</li>
    </ol>
    <div class="table-responsive">
        <caption>table caption</caption>
        <table id="formDefinition-tbl" class="table table-hover">
            <thead>
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th></th>
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

    <form class="form-horizontal" id="formDefinition-frm" method="post">
        <div class="form-group">
            <label for="name" class="col-sm-2 control-label">Name</label>
            <div class="col-sm-8">
                <input name="name" id="name" type="text" class="form-control" placeholder="Name">
            </div>
        </div>
        <div class="form-group">
            <label for="description" class="col-sm-2 control-label">Description</label>
            <div class="col-sm-8">
                <input name="description" type="text" class="form-control" id="description" placeholder="Description">
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-success">Create</button>
            </div>
        </div>
    </form>

</div>

<script type="text/javascript">
    jQuery(document).ready(function () {

        rows('', 'form/definition-row', '#formDefinition-tbl');
        form('#formDefinition-frm', 'form/definition-row', '#formDefinition-tbl')

    })
</script>

</body>
</html>
