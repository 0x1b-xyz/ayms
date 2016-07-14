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
 * have to. It's the responsibility of this method to push the {@code ctrlAttr} values into the
 * rendered form.
 */
function newCtrl() {

    let ctrlId = uniqueId();
    let ctrlType = $(this).data('ctrl-type');
    let ctrlLabel = $(this).html();
    let ctrlAttr = {id: ctrlId};
    let formEle = getTemplate('ctrl-' + ctrlType + '-edit');

    ctrlModal.find('.modal-title').html(ctrlLabel);

    ctrlModalFrm.empty();
    ctrlModalFrm.data('ctrl-id', ctrlId);
    ctrlModalFrm.data('ctrl-type', ctrlType);
    ctrlModalFrm.html(formEle(ctrlAttr));

    ctrlModal.modal();

    if (eval("typeof load_" + ctrlId) != 'undefined') {
        let loadCall = "return load_" + ctrlId + "(ctrlId, ctrlType, ctrlAttr)";
        new Function('ctrlId', 'ctrlType', 'ctrlAttr', loadCall)(ctrlId, ctrlType, ctrlAttr);
    }

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
    for (var key in ctrlAttr) {
        if (!ctrlAttr.hasOwnProperty(key))
            continue;
        var newKey = key.replace(ctrlIdRe, '');
        ctrlAttr[newKey] = ctrlAttr[key];
        delete ctrlAttr[key];
    }

    var added = false;
    if (eval("typeof append_" + ctrlId) != 'undefined') {
        let addCall = 'return append_' + ctrlId + '(ctrlId, ctrlType, ctrlAttr)';
        added = new Function('ctrlId', 'ctrlType', 'ctrlAttr', addCall)(ctrlId, ctrlType, ctrlAttr);
    } else {
        added = appendCtrl(ctrlId, ctrlType, ctrlAttr, appendXdefault,
            appendYdefault, appendWdefault, appendHdefault);
    }

    if (added)
        ctrlModal.modal('hide');

}

/**
 * Appends the output of the {@code ctrl-CTRLTYPE-render} template into the layout grid.
 */
function appendCtrl(ctrlId, ctrlType, ctrlAttr, x, y, width, height) {

    let widget = getTemplate('ctrl-Wrapper')({
        id: ctrlId,
        x: x, y: y, width: width, height: height
    });

    grid.addWidget(widget);
    $(toId(ctrlId)).html(getTemplate('ctrl-' + ctrlType + '-render')(ctrlAttr));

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
