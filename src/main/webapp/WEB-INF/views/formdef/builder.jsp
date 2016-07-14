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

        /*.container-fluid {*/
            /*padding: 0;*/
        /*}*/

        .form-control-list .list-group-item {
            padding: 5px 10px;
        }

        .grid-stack-item-content {
            background-color: whitesmoke;
            padding: 3px 3px 0 3px;
        }

        .grid-stack-item-delete {
            top: 40%;
            left: 100%;
            margin-left: -25px;
            margin-right: 12px;
        }

        /* Override bootstrap fields to work inside the gridstack */

        .grid-stack-item-content .form-group {
            margin: 0;
        }

        .form-group, .form-control-wrapper, .form-control-wrapper textarea {
            height: 95%;
        }

        .form-group.inner-form-group {
            margin-bottom: 0;
        }

        /*These classes make it possible to embed a vertical form field in a */
        /*horizontal form.*/

        .form-vertical .form-horizontal .form-group > label {
            text-align: left;
        }
        .form-horizontal .form-vertical .form-group > label {
            float: none;
            padding-top: 0;
            text-align: left;
            width: 100%
        }
        .form-horizontal .form-vertical .form-control {
            margin-left: 0;
        }
        .form-horizontal .form-vertical.form-actions,
        .form-horizontal .form-vertical .form-actions {
            padding-left: 20px;
        }
        .form-group .form-group {
            margin-bottom: 0;
        }

        /*end vertical form classes*/

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

    <h2>${formdef.name} <small>${formdef.description}</small></h2>
    <ol class="breadcrumb">
        <li><a href="<s:url value="/"/>">Home</a></li>
        <li><a href="<s:url value="/formdef"/>">Form Definitions</a></li>
        <li class="active">${formdef.name}</li>
    </ol>

    <div class="col-lg-10">

        <form id="grid-stack-frm" method="post" class="form-horizontal">
            <hr>
            <div class="grid-stack"></div>
            <hr>
            <a href="<s:url value="/formdef"/>" type="button" role="button" class="btn btn-default">Cancel</a>
            <button type="submit" class="btn btn-primary">Save</button>
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

<script type="text/javascript">

    var ctrlModal;
    var ctrlModalFrm;
    var grid;
    var ctrlIdRe = /\-[0-9A-F]{8}[0-9A-F]{4}[0-9A-F]{4}[0-9A-F]{4}[0-9A-F]{12}/g;

    var appendXdefault = 0;
    var appendYdefault = 99;
    var appendWdefault = 12;
    var appendHdefault = 2;

    /**
     * Renders the {@code ctrl-CTRLTYPE-edit} template into the edit modal. Once the initial rendering
     * is completed we look for a method called {@code load_CTRLID} that we'll pass whatever data we
     * have to. It's the responsibility of this method to push the {@code ctrlData} values into the
     * rendered form.
     */
    function newCtrl() {

        let ctrlId = uniqueId();
        let ctrlType = $(this).data('ctrl-type');
        let ctrlLabel = $(this).html();
        let ctrlData = {id: ctrlId};
        let formEle = getTemplate('ctrl-' + ctrlType + '-edit');

        ctrlModal.find('.modal-title').html(ctrlLabel);

        ctrlModalFrm.empty();
        ctrlModalFrm.data('ctrl-id', ctrlId);
        ctrlModalFrm.data('ctrl-type', ctrlType);
        ctrlModalFrm.html(formEle(ctrlData));

        ctrlModal.modal();

        if (eval("typeof load_" + ctrlId) != 'undefined') {
            let loadCall = "return load_" + ctrlId + "(ctrlId, ctrlType, ctrlData)";
            new Function('ctrlId', 'ctrlType', 'ctrlData', loadCall)(ctrlId, ctrlType, ctrlData);
        }

    }

    /**
     * Pulls the ctrlId, type and data from the edit form. Then we'll look for a function called
     * {@code edit_CTRLID} to call with the {@code ctrlId, ctrlType, ctrlData}. That function
     * should make a call back to {@link #appendCtrl} and then return {@code true}. If the function
     * is not found we'll simply call {@link #appendCtrl} with default settings.
     */
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

        var added = false;
        if (eval("typeof append_" + ctrlId) != 'undefined') {
            let addCall = 'return append_' + ctrlId + '(ctrlId, ctrlType, ctrlData)';
            added = new Function('ctrlId', 'ctrlType', 'ctrlData', addCall)(ctrlId, ctrlType, ctrlData);
        } else {
            added = appendCtrl(ctrlId, ctrlType, ctrlData, appendXdefault,
                                    appendYdefault, appendWdefault, appendHdefault);
        }

        if (added)
            ctrlModal.modal('hide');

    }

    /**
     * Appends the output of the {@code ctrl-CTRLTYPE-render} template into the layout grid.
     */
    function appendCtrl(ctrlId, ctrlType, ctrlData, x, y, width, height) {

        let widget = getTemplate('ctrl-Wrapper')({
            id: ctrlId,
            x: x, y: y, width: width, height: height
        });

        grid.addWidget(widget);
        $(toId(ctrlId)).html(getTemplate('ctrl-' + ctrlType + '-render')(ctrlData));

        return true;

    }

    /**
     * Removes a control
     */
    function delCtrl(ctrlId) {
        let widget = $(toId(ctrlId)).parent();
        grid.removeWidget(widget);
        widget.remove();
    }

    /**
     * Looks up a field by name within the {@code #ctrl-modal-frm}
     */
    function getCtrlField(ctrlId, fieldName) {
        return $('#ctrl-modal-frm').find("input[name='" + fieldName + '-' + ctrlId + "']");
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

        // Bind the {@link #newCtrl} method to all our form control buttons
        $('.form-control-list').find('button').on('click', newCtrl);

        // Call {@link #addCtrl} when the modal "Add" button is clicked
        $('#ctrl-modal-add').on('click', addCtrl);

    });


</script>

</body>
</html>