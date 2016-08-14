<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
<head>
    <title>${definition.description} Results</title>
</head>
<body>

<div class="row">

    <h2>${definition.description} Results</h2>
    <ol class="breadcrumb">
        <li><a href="<s:url value="/"/>">Home</a></li>
        <li><a href="<s:url value="/formDef"/>">Form Definitions</a></li>
        <li class="active">${definition.name}</li>
    </ol>
    <div class="table-responsive">
        <caption>table caption</caption>
        <table id="formResult-tbl" class="table table-hover">
            <thead>
            <tr>
                <th>#</th>
                <th>Created</th>
                <th>Updated</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
        <a href="<c:url value="/form/${definition.id}/result/create"/>" class="btn btn-success btn-sm" role="button">Create</a>
    </div>

</div>

<script type="text/javascript">
    jQuery(document).ready(function () {

        rows('', 'form/result-row', '#formResult-tbl', function() {

        });

    })
</script>

</body>
</html>
