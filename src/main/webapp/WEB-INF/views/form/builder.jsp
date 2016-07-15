<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>forms</title>

    <script type="text/javascript" src="<s:url value="/assets/form-builder.js"/>"></script>
    <link href="<s:url value="/assets/formDefinition.css"/>" rel="stylesheet"/>

</head>
<body>

<div class="row">

    <h2>${formDefinition.name} <small>${formDefinition.description}</small></h2>
    <ol class="breadcrumb">
        <li><a href="<s:url value="/"/>">Home</a></li>
        <li><a href="<s:url value="/formDefinition"/>">Form Definitions</a></li>
        <li class="active">${formDefinition.name}</li>
    </ol>

    <div class="col-lg-10">

        <form id="grid-stack-frm" method="post" class="form-horizontal">
            <hr>
            <div class="grid-stack"></div>
            <hr>
            <a href="<s:url value="/formDefinition"/>" type="button" role="button" class="btn btn-default">Cancel</a>
            <button id="grid-stack-frm-submit" type="button" class="btn btn-primary">Save</button>
        </form>

    </div>

    <div class="col-lg-2 form-control-list">
        <h6>Layout Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm" data-ctrl-type="HeaderText"><i class="fa fa-bold fa-sm" aria-hidden="true"></i>&nbsp; Header</button>
            <button type="button" class="list-group-item btn-sm" data-ctrl-type="TextBlock"><i class="fa fa-italic fa-sm" aria-hidden="true"></i>&nbsp; Text Block</button>
        </div>
        <h6>Text Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm" data-ctrl-type="TextField"><i class="fa fa-font fa-sm" aria-hidden="true"></i>&nbsp; Text Field</button>
            <button type="button" class="list-group-item btn-sm" data-ctrl-type="TextAreaField"><i class="fa fa-edit fa-sm" aria-hidden="true"></i>&nbsp; Text Area</button>
        </div>
        <h6>Typed Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm" data-ctrl-type="PhoneNumberField"><i class="fa fa-volume-control-phone fa-sm" aria-hidden="true"></i>&nbsp; Phone Number</button>
            <button type="button" class="list-group-item btn-sm" data-ctrl-type="EmailField"><i class="fa fa-envelope fa-sm" aria-hidden="true"></i>&nbsp; Email</button>
            <button type="button" class="list-group-item btn-sm" data-ctrl-type="CurrencyField"><i class="fa fa-money fa-sm" aria-hidden="true"></i>&nbsp; Currency</button>
        </div>
        <h6>Selections Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm" data-edit-tpl="ctrl-dropdown-field-tpl"><i class="fa fa-reorder fa-lg" aria-hidden="true"></i>&nbsp; Dropdown</button>
            <button type="button" class="list-group-item btn-sm" data-edit-tpl="ctrl-radio-field-tpl"><i class="fa fa-circle-o fa-fw" aria-hidden="true"></i>&nbsp; Radio Buttons</button>
            <button type="button" class="list-group-item btn-sm" data-edit-tpl="ctrl-checkbox-field-tpl"><i class="fa fa-check-square-o fa-fw" aria-hidden="true"></i>&nbsp; Checkboxes</button>
            <button type="button" class="list-group-item btn-sm" data-edit-tpl="ctrl-yesno-field-tpl"><i class="fa fa-eraser fa-fw" aria-hidden="true"></i>&nbsp; Yes/No</button>
        </div>
    </div>

</div>

<div id="ctrl-modal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Add a Ctrl</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="ctrl-modal-frm"></form>
            </div>
            <div class="modal-footer">
                <button type="button" role="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <button type="button" role="button" class="btn btn-primary" id="ctrl-modal-add">Add</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>