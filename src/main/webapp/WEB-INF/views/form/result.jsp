<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>forms</title>

    <script type="text/javascript" src="<s:url value="/assets/form/result.js"/>"></script>
    <link href="<s:url value="/assets/form.css"/>" rel="stylesheet"/>

</head>
<body>

<div class="row">

    <div id="result-dtl"></div>

    <form id="grid-stack-frm" method="post" class="form-horizontal">
        <div class="grid-stack"></div>
        <hr>
        <button id="result-del-btn" role="button" class="btn btn-danger">Delete</button>
        <a href="<s:url value="/form/${definitionId}/result"/>" type="button" role="button" class="btn btn-default">Cancel</a>
        <button id="result-save-btn" type="button" class="btn btn-primary">Save</button>
    </form>

</body>
</html>