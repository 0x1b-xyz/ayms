//= require app
//= require lodash
//= require bootstrap-wysiwyg
//= require jquery.blockUI
//= require jquery.hotkeys
//= require gridstack

var ctrlModal;
var ctrlModalFrm;
var grid;
var ctrlIdRe = /\-[0-9A-F]{8}[0-9A-F]{4}[0-9A-F]{4}[0-9A-F]{4}[0-9A-F]{12}/g;

var appendXdefault = 0;
var appendYdefault = 99;
var appendWdefault = 12;
var appendHdefault = 2;

/**
 * Map of controls that have are in the grid 
 */
var ctrls = {};

/**
 * Calls a "dynamic" function on a control with the {@code ctrlId, ctrlType and ctrlAttr}
 *
 * @param functionName Name of the function (will be suffixed with {@code _CTRLID})
 * @param ctrlId Identifier of control
 * @param ctrlType Type of control
 * @param ctrlAttr Control attributes
 *
 * @return Result of function call or {@code null} if function does not exist.
 */
function ctrlFunction(functionName, ctrlId, ctrlType, ctrlAttr) {

    if (eval("typeof " + functionName + "_" + ctrlId) != 'undefined') {
        let call = 'return ' + functionName + '_' + ctrlId + '(ctrlId, ctrlType, ctrlAttr)';
        return new Function('ctrlId','ctrlType','ctrlAttr', call)(ctrlId, ctrlType, ctrlAttr);
    }
    return null;

}

/**
 * Renders the {@code ctrl-CTRLTYPE-edit} template into the edit modal. Once the initial rendering
 * is completed we look for a method called {@code load_CTRLID} that we'll pass whatever data we
 * have to. It's the responsibility of this method to push the {@code ctrlAttr} values into the
 * rendered form.
 */
function newCtrl() {

    let ctrlId = uniqueId();
    let ctrlType = $(this).data('ctrl-type');
    let ctrlLabel = $(this).html();
    let ctrlAttr = {id: ctrlId};
    let formEle = getTemplate('ctrl/' + ctrlType + '/edit');

    ctrlModal.find('.modal-title').html(ctrlLabel);

    ctrlModalFrm.empty();
    ctrlModalFrm.data('ctrl-id', ctrlId);
    ctrlModalFrm.data('ctrl-type', ctrlType);
    ctrlModalFrm.html(formEle(ctrlAttr));

    ctrlModal.modal();

    ctrlFunction('load', ctrlId, ctrlType, ctrlAttr);

}

/**
 * Pulls the ctrlId, type and data from the edit form. Then we'll look for a function called
 * {@code edit_CTRLID} to call with the {@code ctrlId, ctrlType, ctrlAttr}. That function
 * should make a call back to {@link #appendCtrl} and then return {@code true}. If the function
 * is not found we'll simply call {@link #appendCtrl} with default settings.
 */
function addCtrl() {

    let ctrlId = ctrlModalFrm.data('ctrl-id');
    let ctrlType = ctrlModalFrm.data('ctrl-type');
    let ctrlAttr = $('#ctrl-modal-frm').serializeJSON();

    // Strip the ctrlId off the field names
    // note - no longer appending ctrlId to field names
    // for (var key in ctrlAttr) {
    //     if (!ctrlAttr.hasOwnProperty(key))
    //         continue;
    //     var newKey = key.replace(ctrlIdRe, '');
    //     ctrlAttr[newKey] = ctrlAttr[key];
    //     delete ctrlAttr[key];
    // }
    console.log(ctrlAttr);

    var added = ctrlFunction('append', ctrlId, ctrlType, ctrlAttr);
    if (added == null)
        added = appendCtrl(ctrlId, ctrlType, ctrlAttr, appendXdefault,
            appendYdefault, appendWdefault, appendHdefault);

    if (added)
        ctrlModal.modal('hide');

}

/**
 * Appends the output of the {@code ctrl-CTRLTYPE-render} template into the layout grid and
 * sticks the ctrl definition into {@link #ctrls}.
 */
function appendCtrl(ctrlId, ctrlType, ctrlAttr, x, y, width, height) {

    let widget = getTemplate('ctrl/wrapper')({
        id: ctrlId,
        x: x, y: y, width: width, height: height
    });

    grid.addWidget(widget);
    $(toId(ctrlId)).html(getTemplate('ctrl/' + ctrlType + '/render')(ctrlAttr));

    // Attach the delete handler
    $(toId(ctrlId)).parent().find('.grid-stack-item-delete a').on('click', function() {
        delCtrl(ctrlId);
    });

    ctrls[ctrlId] = {
        id: ctrlId,
        type: ctrlType,
        attr: ctrlAttr
    };

    console.log("Calling render on " + ctrlId);
    ctrlFunction('render', ctrlId, ctrlType, ctrlAttr);

    return true;

}

/**
 * Extracts the layout info from the grid and merges it with the ctrl definition from the
 * {@link #ctrls} map.
 */
function getCtrls() {

    return jQuery.map($('.grid-stack .grid-stack-item:visible'), function(item) {
        item = $(item);
        var node = item.data('_gridstack_node');
        var ctrlId = item.data('ctrl-id');
        return jQuery.extend(ctrls[ctrlId], {
            layout: {
                x: node.x,
                y: node.y,
                width: node.width,
                height: node.height
            }
        });
    });

}

/**
 * Replaces all controls all the form definition with what we get from {@link #getCtrls}
 */
function saveCtrls() {

    let ctrls = getCtrls();
    jQuery.ajax({
        type: 'post',
        url: window.location.href + "/ctrl/replace",
        contentType: 'application/json',
        data: JSON.stringify(ctrls),
        beforeSend: function() {
            $.blockUI()
        },
        success: function() {
            console.log('good to go!')
        },
        complete: function() {
            $.unblockUI();
        }
    })

}

/**
 * Loads all controls for the form definition
 */
function loadCtrls() {

    jQuery.ajax({
        type: 'get',
        url: window.location.href + '/ctrl',
        contentType: 'application/json',
        beforeSend: function() {
            $.blockUI();
        },
        success: function(response) {
            response.data.forEach(function(ctrl) {
                appendCtrl(ctrl.id, ctrl.type, ctrl.attr,
                    ctrl.layout.x, ctrl.layout.y, ctrl.layout.width, ctrl.layout.height)
            });
        },
        complete: function() {
            $.unblockUI();
        }
    });

}

/**
 * Removes a control from the grid
 *
 * @param ctrlId Identifier of control
 */
function delCtrl(ctrlId) {
    let widget = $(toId(ctrlId)).parent();
    grid.removeWidget(widget);
    widget.remove();
    delete ctrls[ctrlId];
}

/**
 * Looks up a field by id within the {@code #ctrl-modal-frm}
 *
 * @param ctrlId Identifier of control
 * @param fieldName Name of field within control edit form
 */
function getCtrlField(ctrlId, fieldName) {
    return $('#ctrl-modal-frm').find(toId(fieldName + '-' + ctrlId));
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

    $('#grid-stack-frm-submit').on('click', saveCtrls);

    loadCtrls();

});
