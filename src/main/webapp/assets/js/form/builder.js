//= require form/common

var APPEND_X_DEFAULT = 0;
var APPEND_Y_DEFAULT = 99;
var APPEND_W_DEFAULT = 12;
var APPEND_H_DEFAULT = 2;

/**
 * Renders the {@code ctrl-CTRLTYPE-edit} template into the edit modal. Once the initial rendering
 * is completed we look for a method called {@code load_CTRLID} that we'll pass whatever data we
 * have to. It's the responsibility of this method to push the {@code ctrl} values into the
 * rendered form.
 */
function newCtrl() {

    let ctrlId = uniqueId();
    let ctrlType = $(this).data('ctrl-type');
    let ctrl = {
        id: ctrlId,
        type: ctrlType,
        attr: {}
    };

    editModal(ctrl, false);

}

/**
 * Edits a control by pulling up the edit form
 */
function editCtrl(ctrlId) {

    let ctrl = getCtrlInstance(ctrlId);
    editModal(ctrl, true);

}

/**
 * Shows the edit modal with the ctrl editor. Works for adding a new ctrl.
 *
 * @param ctrl Instance to edit
 * @param editing Indicates we're editing not adding.
 */
function editModal(ctrl, editing) {

    CTRL_MODAL.find('.modal-title').html(CTRL_DEFS[ctrl.type].label);

    CTRL_MODAL_FRM.data('ctrl-id', ctrl.id);
    CTRL_MODAL_FRM.data('ctrl-type', ctrl.type);
    CTRL_MODAL_FRM.find('.modal-body').html(getTemplate('ctrl/' + ctrl.type + '/edit')(ctrl));

    CTRL_MODAL_FRM.validator('destroy');
    CTRL_MODAL_FRM.validator().off('submit');
    CTRL_MODAL_FRM.validator().on('submit', function(e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            if (!editing) {
                addCtrl();
            } else {
                updateCtrl();
            }
        }
    });

    invokeCtrlFunction('edit', ctrl.id, ctrl.type, ctrl.attr);

    let deleteBtn = CTRL_MODAL_FRM.find('#ctrl-modal-del');
    let addBtn = CTRL_MODAL_FRM.find('#ctrl-modal-add');

    if (editing) {

        deleteBtn.off('click');
        deleteBtn.on('click', function() {
            removeCtrl(ctrl.id);
        });
        deleteBtn.removeClass('hidden');

        addBtn.html('Update');

    } else {

        deleteBtn.addClass('hidden');
        addBtn.html('Add');

    }

    CTRL_MODAL.modal();

}

/**
 * Pulls the ctrlId, type and data from the edit form. Calls the 'append' function on the ctrl
 * definition when it exists. Append should call {@link #appendCtrl} or return null.
 */
function addCtrl() {

    let ctrlId = CTRL_MODAL_FRM.data('ctrl-id');
    let ctrlType = CTRL_MODAL_FRM.data('ctrl-type');
    let ctrlAttr = $('#ctrl-modal-frm').serializeJSON();

    var added = invokeCtrlFunction('append', ctrlId, ctrlType, ctrlAttr);
    if (added == null)
        added = appendCtrl(ctrlId, ctrlType, ctrlAttr, APPEND_X_DEFAULT,
            APPEND_Y_DEFAULT, APPEND_W_DEFAULT, APPEND_H_DEFAULT);

    if (added)
        CTRL_MODAL.modal('hide');

}

/**
 * Updates the ctrl by re-rendering it into an existing widget wrapper.
 */
function updateCtrl() {

    let ctrlId = CTRL_MODAL_FRM.data('ctrl-id');
    let ctrlType = CTRL_MODAL_FRM.data('ctrl-type');
    let ctrlAttr = $('#ctrl-modal-frm').serializeJSON();

    CTRL_INSTANCES[ctrlId] = {
        id: ctrlId,
        type: ctrlType,
        attr: ctrlAttr
    };

    let widgetContent = getCtrlContent(ctrlId);
    widgetContent.html(getTemplate('ctrl/' + ctrlType + '/render')(CTRL_INSTANCES[ctrlId]));

    invokeCtrlFunction('render', ctrlId, ctrlType, ctrlAttr);

    CTRL_MODAL.modal('hide');

}

/**
 * Extracts the layout info from the CTRL_GRID and merges it with the ctrl definition from the
 * {@link #CTRL_INSTANCES} map.
 */
function getCtrls() {

    return jQuery.map($('.grid-stack .grid-stack-item:visible'), function(item) {
        item = $(item);
        var node = item.data('_gridstack_node');
        var ctrlId = item.data('ctrl-id');
        return jQuery.extend(CTRL_INSTANCES[ctrlId], {
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
        url: window.location.href + "/ctrl",
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
 * Removes a control from the GRID_CTRL
 *
 * @param ctrlId Identifier of control
 */
function removeCtrl(ctrlId) {

    let ctrl = CTRL_INSTANCES[ctrlId];
    let widget = getCtrlContent(ctrlId).parent();

    CTRL_GRID.removeWidget(widget);
    widget.remove();

    invokeCtrlFunction('remove', ctrl.id, ctrl.type, ctrl.attr);

    delete CTRL_INSTANCES[ctrlId];
}

$(document).ready(function () {

    Handlebars.registerHelper('default-edit-fields', function() {
        return getTemplate('ctrl/default-edit-fields')(this)
    });

    // Bind the {@link #newCtrl} method to all our form control buttons
    $('.form-control-list').find('button').on('click', newCtrl);

    $('#grid-stack-frm-submit').on('click', saveCtrls);

});
