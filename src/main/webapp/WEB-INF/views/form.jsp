<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>forms</title>

    <script type="text/javascript" src="<s:url value="/static/js/lodash.min.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/static/js/gridstack.js"/>"></script>

    <link href="<s:url value="/static/css/gridstack.css"/>" rel="stylesheet"/>
    <link href="<s:url value="/static/css/gridstack-extra.css"/>" rel="stylesheet"/>

    <style type="text/css">

        .form-control-list .list-group-item {
            padding: 5px 10px;
        }

        .grid-stack-item-content {
            background-color: #c4e3f3;
            padding: 3px 3px 0 3px;
        }

        /* Override bootstrap fields to work inside the gridstack */

        .form-group {
            margin: 0;
        }

        .form-group, .form-control-wrapper, .form-control-wrapper textarea {
            height: 95%;
        }

    </style>

</head>
<body>

<div class="row">

    <div class="col-lg-9">

        <form id="grid-stack-frm" method="post">

            <div class="grid-stack">
                <%--<div class="grid-stack-item" data-gs-x="0" data-gs-y="0" data-gs-width="6" data-gs-height="2">--%>
                <%--<div class="grid-stack-item-content">--%>
                <%--<div class="form-group">--%>
                <%--<label for="name">Employee</label>--%>
                <%--&lt;%&ndash;<div class="col-sm-10">&ndash;%&gt;--%>
                <%--<input name="name" id="name" type="text" class="form-control" placeholder="Name">--%>
                <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="grid-stack-item" data-gs-x="4" data-gs-y="2" data-gs-width="4" data-gs-height="3">--%>
                <%--<div class="grid-stack-item-content">--%>
                <%--<div class="form-group">--%>
                <%--<label for="text" class="col-sm-2 control-label">Some Text</label>--%>
                <%--<div class="col-sm-10 form-control-wrapper">--%>
                <%--<textarea class="form-control" name="text" id="text"></textarea>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--</div>--%>
                <%--</div>--%>
            </div>

        </form>

    </div>

    <div class="col-lg-3 form-control-list">
        <h6>Layout Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm" data-ctrl-type="HeaderText" data-edit-tpl="ctrl-header-tpl"><i class="fa fa-bold fa-sm" aria-hidden="true"></i>&nbsp; Header</button>
            <button type="button" class="list-group-item btn-sm" data-edit-tpl="ctrl-text-block-tpl"><i class="fa fa-italic fa-sm" aria-hidden="true"></i>&nbsp; Text Block</button>
        </div>
        <h6>Text Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm" data-edit-tpl="ctrl-text-field-tpl"><i class="fa fa-font fa-sm" aria-hidden="true"></i>&nbsp; Text Field</button>
            <button type="button" class="list-group-item btn-sm" data-edit-tpl="ctrl-textarea-field-tpl"><i class="fa fa-edit fa-sm" aria-hidden="true"></i>&nbsp; Text Area Field</button>
            <button type="button" class="list-group-item btn-sm" data-edit-tpl="ctrl-password-field-tpl"><i class="fa fa-user-secret fa-sm" aria-hidden="true"></i>&nbsp; Password Field</button>
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


<script id="text-tpl" type="text/x-handlebars-template">
    <div class="grid-stack-item" data-gs-x="0" data-gs-y="99" data-gs-width="6" data-gs-height="2">
        <div class="grid-stack-item-content">
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">employee new</label>
                <div class="col-sm-10">
                <input name="name" id="name" type="text" class="form-control" placeholder="Name">
                </div>
            </div>
        </div>
    </div>
</script>

<script id="ctrl-header-tpl" type="text/x-handlebars-template">
</script>

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

    jQuery(document).ready(function () {

        var gridStack = jQuery('.grid-stack');
        gridStack.gridstack({
            cellHeight: 30,
            verticalMargin: 10
        });

        grid = gridStack.data('gridstack');
        ctrlModal = $('#ctrl-modal');
        ctrlModalFrm = ctrlModal.find('#ctrl-modal-frm');

        jQuery('.form-control-list').find('button').on('click', function () {

            let ctrlType = jQuery(this).data('ctrl-type');
            let formEle = getTemplate(ctrlType + '-edit');

            ctrlModalFrm.data('ctrl-type', ctrlType);
            ctrlModalFrm.html(formEle());
            ctrlModal.modal();
        })
    });

    function addCtrl() {
        let ctrlData = $('#ctrl-modal-frm').serializeJSON();
        let ctrlType = ctrlModalFrm.data('ctrl-type');
        let ctrlId = uniqueId();
        let widget = getTemplate('GridStackItem')({
            id: ctrlId,
            x: 0, y: 10, width: 12, height: 2
        });
        grid.addWidget(widget);
        jQuery(toId(ctrlId)).html(getTemplate(ctrlType + '-render')(ctrlData));
        ctrlModal.modal('hide');
    }
    $('#ctrl-modal-add').on('click', addCtrl);

</script>

</body>
</html>