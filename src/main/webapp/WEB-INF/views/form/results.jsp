<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
<head>
    <title>${definition.description} Results</title>
    <script type="text/javascript" src="<s:url value="/assets/form/form.js"/>"></script>
</head>
<body>

<div class="row">

    <h2>${definition.description} Results</h2>
    <ol class="breadcrumb">
        <li><a href="<s:url value="/"/>">Home</a></li>
        <li><a href="<s:url value="/form"/>">Form Definitions</a></li>
        <li class="active">${definition.name}</li>
    </ol>
    <div class="table-responsive">
        <%--<caption>table caption</caption>--%>
        <table id="result-tbl" class="table table-hover">
            <thead>
            <tr>
                <th>Definition</th>
                <th>Form</th>
                <th>Created</th>
                <th>Updated</th>
                <%--<th></th>--%>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
        <a href="<c:url value="/form/${definition.id}/result/create"/>" class="btn btn-success btn-sm" role="button">Create</a>
    </div>

</div>

<div class="row">

    <h2>Search</h2>

    <form class="form-horizontal" id="client-frm" method="post">

        <div class="form-group">
            <label for="search-field-sel" class="col-sm-2 control-label">Terms</label>
            <div class="col-sm-8">
            <div class="form-group row">
                <div class="col-md-6">
                    <select id="search-field-sel" class="form-control"></select>
                </div>
                <div class="col-md-6">
                    <input id="search-field-txt" type="text" class="form-control"/>
                </div>
            </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button id="search-btn" type="button" role="button" class="btn btn-success">Search</button>
            </div>
        </div>


    </form>

</div>

<script type="text/javascript">

    function search() {

        let field = $('#search-field-sel').val();
        let term = $('#search-field-txt').val();

        let terms = {};
        terms[field] = term;

        jQuery.ajax('/form/1/search', {
            type: 'post',
            contentType: 'application/json',
            data: JSON.stringify(terms),
            success: function(response) {
                console.log(response);
            }
        })

    }

    jQuery(document).ready(function () {

        rows('', 'form/result-row', '#result-tbl', function() {});

        jQuery.ajax('/form/1/fields', {
            type: 'get',
            contentType: 'application/json',
            success: function(response) {
                let fieldSel = $('#search-field-sel');
                $.each(response.data, function(idx, field) {
                    fieldSel.append('<option>' + field + '</option>')
                })
            }
        })

        $('#search-btn').on('click', search)

    })
</script>

</body>
</html>
