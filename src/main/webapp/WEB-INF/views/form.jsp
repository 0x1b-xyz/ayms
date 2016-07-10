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
        .grid-stack-item-content {
            background-color: #c4e3f3;
            padding: 3px 3px 0 3px;
        }

        .form-group {
            margin: 0;
        }

        .form-group, .form-control-wrapper, .form-control-wrapper textarea {
            height: 95%;
        }

        .form-control-list .list-group-item {
            padding: 5px 10px;
        }
    </style>

</head>
<body>

<div class="row">

    <div class="col-lg-9">

        <form id="employee-frm" method="post">

            <div class="grid-stack">
                <div class="grid-stack-item" data-gs-x="0" data-gs-y="0" data-gs-width="6" data-gs-height="2">
                <div class="grid-stack-item-content">
                <div class="form-group">
                <label for="name">Employee</label>
                <%--<div class="col-sm-10">--%>
                <input name="name" id="name" type="text" class="form-control" placeholder="Name">
                <%--</div>--%>
                </div>
                </div>
                </div>
                <div class="grid-stack-item" data-gs-x="4" data-gs-y="2" data-gs-width="4" data-gs-height="3">
                <div class="grid-stack-item-content">
                <div class="form-group">
                <label for="text" class="col-sm-2 control-label">Some Text</label>
                <div class="col-sm-10 form-control-wrapper">
                <textarea class="form-control" name="text" id="text"></textarea>
                </div>
                </div>

                </div>
                </div>
            </div>

        </form>

    </div>

    <div class="col-lg-3 form-control-list">
        <h6>Layout Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm"><i class="fa fa-bold fa-sm" aria-hidden="true"></i>&nbsp; Header</button>
            <button type="button" class="list-group-item btn-sm"><i class="fa fa-italic fa-sm" aria-hidden="true"></i>&nbsp; Text Block</button>
        </div>
        <h6>Text Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm"><i class="fa fa-font fa-sm" aria-hidden="true"></i>&nbsp; Text Field</button>
            <button type="button" class="list-group-item btn-sm"><i class="fa fa-edit fa-sm" aria-hidden="true"></i>&nbsp; Text Area</button>
            <button type="button" class="list-group-item btn-sm"><i class="fa fa-user-secret fa-sm" aria-hidden="true"></i>&nbsp; Password Field</button>
        </div>
        <h6>Typed Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm"><i class="fa fa-volume-control-phone fa-sm" aria-hidden="true"></i>&nbsp; Phone Number</button>
            <button type="button" class="list-group-item btn-sm"><i class="fa fa-envelope fa-sm" aria-hidden="true"></i>&nbsp; Email</button>
            <button type="button" class="list-group-item btn-sm"><i class="fa fa-money fa-sm" aria-hidden="true"></i>&nbsp; Currency</button>
        </div>
        <h6>Date/Time Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm"><i class="fa fa-calendar fa-sm" aria-hidden="true"></i>&nbsp; Date</button>
            <button type="button" class="list-group-item btn-sm"><i class="fa fa-clock-o fa-sm" aria-hidden="true"></i>&nbsp; Time</button>
        </div>
        <h6>Selections Controls</h6>
        <div class="list-group">
            <button type="button" class="list-group-item btn-sm"><i class="fa fa-reorder fa-lg" aria-hidden="true"></i>&nbsp; Dropdown</button>
            <button type="button" class="list-group-item btn-sm"><i class="fa fa-circle-o fa-fw" aria-hidden="true"></i>&nbsp; Radio Buttons</button>
            <button type="button" class="list-group-item btn-sm"><i class="fa fa-check-square-o fa-fw" aria-hidden="true"></i>&nbsp; Checkboxes</button>
            <button type="button" class="list-group-item btn-sm"><i class="fa fa-eraser fa-fw" aria-hidden="true"></i>&nbsp; Yes/No</button>
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

<script type="text/javascript">
    jQuery(document).ready(function () {

        var options = {
            cellHeight: 30,
            verticalMargin: 10
        };

        var gridStack = jQuery('.grid-stack');
        gridStack.gridstack(options);

        var grid = gridStack.data('gridstack');

        jQuery('.widget-btns').find('button').on('click', function () {
//            let tplName = jQuery(this).data('tpl');
//            let tpl = Handlebars.compile(jQuery(toId(tplName)).html());
//            console.log(tpl());
//            grid.addWidget(tpl());

            var obj = new FormTextControl({something: 'alison'});
            obj.alert()
        })
    });
</script>

</body>
</html>