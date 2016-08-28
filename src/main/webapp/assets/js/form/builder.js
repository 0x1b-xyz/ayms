//= require ../app
//= require common
//= require_tree ctrl

var APPEND_X_DEFAULT = 0;
var APPEND_Y_DEFAULT = 99;
var APPEND_W_DEFAULT = 12;
var APPEND_H_DEFAULT = 2;

/**
 * Builds a new ctrl and passes it to {@link #editModal}
 */
function newCtrl() {

    let ctrlType = $(this).data('ctrl-type');
    let ctrlIdx = $.map(CTRL_INSTANCES, function(name, ctrl) {
        if (ctrl.type != ctrlType)
            return null;
        return ctrl.name
    }).length;

    let ctrl = {
        type: ctrlType,
        name: ctrlType.toLowerCase() + ctrlIdx,
        attr: {}
    };

    editModal(ctrl, false);

}

/**
 * Edits a control by pulling up the edit form
 * 
 * @param name Name of control to edit
 */
function editCtrl(name) {
    editModal(getCtrlInstance(name), true);
}

/**
 * Shows the edit modal with the ctrl editor. Works for adding a new ctrl.
 *
 * @param ctrl Instance to edit
 * @param editing Indicates we're editing not adding.
 */
function editModal(ctrl, editing) {

    CTRL_MODAL.find('.modal-title').html(CTRL_DEFS[ctrl.type].label);

    CTRL_MODAL_FRM.data('ctrl-type', ctrl.type);
    CTRL_MODAL_FRM.find('.modal-body').html(getTemplate('form/ctrl/' + ctrl.type + '/edit')(ctrl));

    CTRL_MODAL_FRM.find("input[type='text']").on("click", function () {
        $(this).select();
    });

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

    invokeCtrlFunction('edit', ctrl);

    let deleteBtn = CTRL_MODAL_FRM.find('#ctrl-modal-del');
    let addBtn = CTRL_MODAL_FRM.find('#ctrl-modal-add');

    if (editing) {

        deleteBtn.off('click');
        deleteBtn.on('click', function() {
            removeCtrl(ctrl);
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
 * Collects the type, name and attributes of a control from the {@link #CTRL_MODAL_FRM}
 */
function getCtrlFromEdit() {

    let attr = $('#ctrl-modal-frm').serializeJSON();
    let name = attr.name;
    if (!name)
        throw new Error("Name is required to add a ctrl", attr);
    delete attr.name;
    let type = CTRL_MODAL_FRM.data('ctrl-type');
    if (!type)
        throw new Error("Type is required to add a ctrl", attr);

    return {
        type: type,
        name: name,
        attr: attr
    };


}

/**
 * Pulls the ctrl, type and data from the edit form. Calls the 'append' function on the ctrl
 * definition when it exists. Append should call {@link #appendCtrl} or return null.
 */
function addCtrl() {

    let ctrl = getCtrlFromEdit();

    var added = invokeCtrlFunction('append', ctrl);
    if (added == null)
        added = appendCtrl(ctrl, APPEND_X_DEFAULT,
            APPEND_Y_DEFAULT, APPEND_W_DEFAULT, APPEND_H_DEFAULT, true);

    if (added)
        CTRL_MODAL.modal('hide');

}

/**
 * Updates the ctrl by re-rendering it into an existing widget wrapper.
 */
function updateCtrl() {

    let ctrl = getCtrlFromEdit();

    CTRL_INSTANCES[ctrl.name] = ctrl;

    let widgetContent = getCtrlContent(ctrl);
    widgetContent.html(getTemplate('form/ctrl/' + ctrl.type + '/render')(ctrl));

    invokeCtrlFunction('render', ctrl);

    CTRL_MODAL.modal('hide');

}

/**
 * Merges the ctrl instances with their current layout information and posts them back to the
 * server for create or update.
 */
function saveCtrls() {

    let ctrls = jQuery.map($('.grid-stack .grid-stack-item:visible'), function(item) {
        item = $(item);
        let node = item.data('_gridstack_node');
        let ctrlName = item.data('ctrl-name');
        return jQuery.extend(CTRL_INSTANCES[ctrlName], {
            layout: {
                x: node.x,
                y: node.y,
                width: node.width,
                height: node.height
            }
        });
    });

    jQuery.ajax({
        type: 'post',
        url: window.location.href + "/ctrl",
        contentType: 'application/json',
        data: JSON.stringify(ctrls),
        beforeSend: function() {
            $.blockUI()
        },
        success: function(response) {
            console.log('Saved definition, reloading now ...');
            renderDefinition(response.data, true);
        },
        complete: function() {
            $.unblockUI();
        }
    })

}

$(document).ready(function () {

    Handlebars.registerHelper('default-edit-fields', function(options) {
        return getTemplate('form/ctrl/default-edit-fields')(this)
    });
    
    // Bind the {@link #newCtrl} method to all our form control buttons
    $('.form-control-list').find('button').on('click', newCtrl);

    $('#grid-stack-frm-submit').on('click', saveCtrls);

    loadCtrls(true);
    
});
