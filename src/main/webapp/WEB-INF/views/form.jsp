<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>forms</title>

    <script type="text/javascript" src="<s:url value="/static/js/lodash.min.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/static/js/gridstack.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/static/js/bootstrap-wysiwyg.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/static/js/jquery.hotkeys.js"/>"></script>

    <link href="<s:url value="/static/css/gridstack.css"/>" rel="stylesheet"/>
    <link href="<s:url value="/static/css/gridstack-extra.css"/>" rel="stylesheet"/>

    <style type="text/css">

        .container-fluid {
            padding: 0;
        }

        .form-control-list .list-group-item {
            padding: 5px 10px;
        }

        .grid-stack-item-content {
            background-color: whitesmoke;
            padding: 3px 3px 0 3px;
        }

        /* Override bootstrap fields to work inside the gridstack */

        .grid-stack-frm .form-group {
            margin: 0;
        }

        .form-group, .form-control-wrapper, .form-control-wrapper textarea {
            height: 95%;
        }

        .text-editor {
            max-height: 250px;
            height: 250px;
            background-color: white;
            border-collapse: separate;
            border: 1px solid rgb(204, 204, 204);
            padding: 4px;
            box-sizing: content-box;
            -webkit-box-shadow: rgba(0, 0, 0, 0.0745098) 0px 1px 1px 0px inset;
            box-shadow: rgba(0, 0, 0, 0.0745098) 0px 1px 1px 0px inset;
            border-top-right-radius: 3px; border-bottom-right-radius: 3px;
            border-bottom-left-radius: 3px; border-top-left-radius: 3px;
            overflow: scroll;
            outline: none;
        }

    </style>

</head>
<body>

<div class="row">

    <div class="col-lg-9">

        <hr>
        <form id="grid-stack-frm" method="post" class="form-horizontal">
            <div class="grid-stack"></div>
        </form>
        <hr>

    </div>

    <div class="col-lg-3 form-control-list">
        <h6>Layout Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm" data-ctrl-type="HeaderText"><i class="fa fa-bold fa-sm" aria-hidden="true"></i>&nbsp; Header</button>
            <button type="button" class="list-group-item btn-sm" data-ctrl-type="TextBlock"><i class="fa fa-italic fa-sm" aria-hidden="true"></i>&nbsp; Text Block</button>
        </div>
        <h6>Text Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm" data-ctrl-type="TextField"><i class="fa fa-font fa-sm" aria-hidden="true"></i>&nbsp; Text Field</button>
            <button type="button" class="list-group-item btn-sm" data-ctrl-type="TextAreaField"><i class="fa fa-edit fa-sm" aria-hidden="true"></i>&nbsp; Text Area Field</button>
            <button type="button" class="list-group-item btn-sm" data-ctrl-type="PasswordField"><i class="fa fa-user-secret fa-sm" aria-hidden="true"></i>&nbsp; Password Field</button>
        </div>
        <h6>Typed Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm" data-edit-tpl="ctrl-phone-field-tpl"><i class="fa fa-volume-control-phone fa-sm" aria-hidden="true"></i>&nbsp; Phone Number Field</button>
            <button type="button" class="list-group-item btn-sm" data-edit-tpl="ctrl-email-field-tpl"><i class="fa fa-envelope fa-sm" aria-hidden="true"></i>&nbsp; Email Field</button>
            <button type="button" class="list-group-item btn-sm" data-edit-tpl="ctrl-currency-field-tpl"><i class="fa fa-money fa-sm" aria-hidden="true"></i>&nbsp; Currency Field</button>
        </div>
        <h6>Date/Time Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm" data-edit-tpl="ctrl-date-field-tpl"><i class="fa fa-calendar fa-sm" aria-hidden="true"></i>&nbsp; Date Field</button>
            <button type="button" class="list-group-item btn-sm" data-edit-tpl="ctrl-time-field-tpl"><i class="fa fa-clock-o fa-sm" aria-hidden="true"></i>&nbsp; Time Field</button>
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
                <h4 class="modal-title">Add a Header</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="ctrl-modal-frm"></form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" id="ctrl-modal-add">Add</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    var ctrlModal;
    var ctrlModalFrm;
    var grid;
    var ctrlIdRe = /\-[0-9A-F]{8}[0-9A-F]{4}[0-9A-F]{4}[0-9A-F]{4}[0-9A-F]{12}/g;

    function newCtrl() {

        let ctrlId = uniqueId();
        let ctrlType = $(this).data('ctrl-type');
        let ctrlLabel = $(this).html();
        let formEle = getTemplate('ctrl-' + ctrlType + '-edit');

        ctrlModal.find('.modal-title').html(ctrlLabel);

        ctrlModalFrm.data('ctrl-id', ctrlId);
        ctrlModalFrm.data('ctrl-type', ctrlType);
        ctrlModalFrm.html(formEle({
            id: ctrlId
        }));

        ctrlModal.modal();

    }

    function addCtrl() {

        let ctrlId = ctrlModalFrm.data('ctrl-id');
        let ctrlType = ctrlModalFrm.data('ctrl-type');
        let ctrlData = $('#ctrl-modal-frm').serializeJSON();

        // Strip the ctrlId off the field names
        for (var key in ctrlData) {
            if (!ctrlData.hasOwnProperty(key))
                continue;
            var newKey = key.replace(ctrlIdRe, '');
            ctrlData[newKey] = ctrlData[key];
            delete ctrlData[key];
        }

        let widget = getTemplate('ctrl-Wrapper')({
            id: ctrlId,
            x: 0, y: 10, width: 12, height: 2
        });

        grid.addWidget(widget);
        $(toId(ctrlId)).html(getTemplate('ctrl-' + ctrlType + '-render')(ctrlData));

        ctrlModal.modal('hide');

    }

    $(document).ready(function () {

        var gridStack = jQuery('.grid-stack');
        gridStack.gridstack({
            cellHeight: 15,
            verticalMargin: 10
        });

        grid = gridStack.data('gridstack');
        ctrlModal = $('#ctrl-modal');
        ctrlModalFrm = ctrlModal.find('#ctrl-modal-frm');

        $('.form-control-list').find('button').on('click', newCtrl);
        $('#ctrl-modal-add').on('click', addCtrl);

    });


</script>

</body>
</html>