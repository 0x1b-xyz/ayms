//= require app
//= require bootstrap-wysiwyg
//= require jquery.blockUI
//= require jquery.hotkeys
//= require jquery.observer
//= require lodash
//= require gridstack

var CTRL_MODAL;
var CTRL_MODAL_FRM;
var CTRL_GRID;
var CTRL_ID_RE = /\-[0-9A-F]{8}[0-9A-F]{4}[0-9A-F]{4}[0-9A-F]{4}[0-9A-F]{12}/g;

var APPEND_X_DEFAULT = 0;
var APPEND_Y_DEFAULT = 99;
var APPEND_W_DEFAULT = 12;
var APPEND_H_DEFAULT = 2;

/**
 * Map of controls that are present in the GRID 
 */
var CTRLS = {};

/**
 * Name of topic where ctrl value changes are published  
 */
var TOPIC_VALUE_CHANGE = 'ctrlValueChange';

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

    CTRL_MODAL.find('.modal-title').html(ctrlLabel);

    CTRL_MODAL_FRM.empty();
    CTRL_MODAL_FRM.data('ctrl-id', ctrlId);
    CTRL_MODAL_FRM.data('ctrl-type', ctrlType);
    CTRL_MODAL_FRM.html(formEle(ctrlAttr));

    CTRL_MODAL.modal();

    ctrlFunction('load', ctrlId, ctrlType, ctrlAttr);

}

/**
 * Pulls the ctrlId, type and data from the edit form. Then we'll look for a function called
 * {@code edit_CTRLID} to call with the {@code ctrlId, ctrlType, ctrlAttr}. That function
 * should make a call back to {@link #appendCtrl} and then return {@code true}. If the function
 * is not found we'll simply call {@link #appendCtrl} with default settings.
 */
function addCtrl() {

    let ctrlId = CTRL_MODAL_FRM.data('ctrl-id');
    let ctrlType = CTRL_MODAL_FRM.data('ctrl-type');
    let ctrlAttr = $('#ctrl-modal-frm').serializeJSON();

    // Strip the ctrlId off the field names
    // note - no longer appending ctrlId to field names
    // for (var key in ctrlAttr) {
    //     if (!ctrlAttr.hasOwnProperty(key))
    //         continue;
    //     var newKey = key.replace(CTRL_ID_RE, '');
    //     ctrlAttr[newKey] = ctrlAttr[key];
    //     delete ctrlAttr[key];
    // }

    var added = ctrlFunction('append', ctrlId, ctrlType, ctrlAttr);
    if (added == null)
        added = appendCtrl(ctrlId, ctrlType, ctrlAttr, APPEND_X_DEFAULT,
            APPEND_Y_DEFAULT, APPEND_W_DEFAULT, APPEND_H_DEFAULT);

    if (added)
        CTRL_MODAL.modal('hide');

}

/**
 * Appends the output of the {@code ctrl-CTRLTYPE-render} template into the layout CTRL_GRID and
 * sticks the ctrl definition into {@link #CTRLS}.
 */
function appendCtrl(ctrlId, ctrlType, ctrlAttr, x, y, width, height) {

    let widget = getTemplate('ctrl/wrapper')({
        id: ctrlId,
        x: x, y: y, width: width, height: height
    });

    CTRL_GRID.addWidget(widget);
    $(toId(ctrlId)).html(getTemplate('ctrl/' + ctrlType + '/render')(
        $.extend(ctrlAttr, {
            id: ctrlId
        })
    ));

    // Attach the delete handler
    $(toId(ctrlId)).parent().find('.grid-stack-item-delete a').on('click', function() {
        removeCtrl(ctrlId);
    });

    CTRLS[ctrlId] = {
        id: ctrlId,
        type: ctrlType,
        attr: ctrlAttr
    };

    ctrlFunction('render', ctrlId, ctrlType, ctrlAttr);

    return true;

}

/**
 * Extracts the layout info from the CTRL_GRID and merges it with the ctrl definition from the
 * {@link #CTRLS} map.
 */
function getCtrls() {

    return jQuery.map($('.grid-stack .grid-stack-item:visible'), function(item) {
        item = $(item);
        var node = item.data('_gridstack_node');
        var ctrlId = item.data('ctrl-id');
        return jQuery.extend(CTRLS[ctrlId], {
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
 * Removes a control from the GRID_CTRL
 *
 * @param ctrlId Identifier of control
 */
function removeCtrl(ctrlId) {

    let ctrl = CTRLS[ctrlId];
    let widget = $(toId(ctrlId)).parent();

    CTRL_GRID.removeWidget(widget);
    widget.remove();

    ctrlFunction('remove', ctrl.id, ctrl.type, ctrl.attr);

    delete CTRLS[ctrlId];
}

/**
 * Looks up a field by id within the {@code #ctrl-modal-frm}
 *
 * @param ctrlId Identifier of control
 * @param fieldName Name of field within control edit form
 */
function getCtrlEditField(ctrlId, fieldName) {
    return $('#ctrl-modal-frm').find(toId(fieldName + '-' + ctrlId));
}

/**
 * Looks up a field by id within the {@code #grid-stack-frm}
 *
 * @param ctrlId Identifier of control
 * @param fieldName Name of rendered field in form
 */
function getCtrlRenderField(ctrlId, fieldName) {
    return $('#grid-stack-frm').find(toId(fieldName + '-' + ctrlId));
}

$(document).ready(function () {

    var gridStack = jQuery('.grid-stack');
    gridStack.gridstack({
        cellHeight: 15,
        verticalMargin: 10
    });

    CTRL_GRID = gridStack.data('gridstack');
    CTRL_MODAL = $('#ctrl-modal');
    CTRL_MODAL_FRM = CTRL_MODAL.find('#ctrl-modal-frm');

    // Bind the {@link #newCtrl} method to all our form control buttons
    $('.form-control-list').find('button').on('click', newCtrl);

    // Call {@link #addCtrl} when the modal "Add" button is clicked
    $('#ctrl-modal-add').on('click', addCtrl);

    $('#grid-stack-frm-submit').on('click', saveCtrls);

    loadCtrls();

    // function fn1() { console.log('fn1 called', arguments); }
    // function fn2() { console.log('fn2 called', arguments); }
    // function fn3() { console.log('fn3 called', arguments); }
    //
    // function publish() {
    //     var args = (1 <= arguments.length) ? Array.prototype.slice.call(arguments, 0) : [];
    //
    //     console.group('publish ' + args[0]);
    //     $.observer.publish.apply($.observer, args);
    //     console.groupEnd();
    // }
    //
    // $.observer.subscribe('test1', fn1);
    // $.observer.subscribe('test1', fn2);
    // $.observer.subscribe('test2', fn3);
    //
    // publish('test1', 'strArg1', 'strArg2');
    // publish('test2', 1, 2, '3');
    //
    // $.observer.unsubscribe('test1', fn2);
    //
    // publish('test1', {key1: 'val1', key2: 'val2'});
    // publish('test2');
    //
    // $.observer.unsubscribe('test1', fn1);
    //
    // publish('test1');
    // publish('test2');

});
