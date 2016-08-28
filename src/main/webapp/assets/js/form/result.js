//= require ../app
//= require common
//= require_tree ctrl
//= require result-dtl

var AYM_FORM;

function deleteResult() {

    jQuery.ajax({
        type: 'DELETE',
        url: window.location.href,
        contentType: 'application/json',
        beforeSend: function() {
            $.blockUI();
        },
        success: function(response) {
            console.log("Removed result ...");
            console.log(window.location.href)
        },
        complete: function () {
            $.unblockUI();
        }
    })

}

/**
 * Collects the results of the form and saves them. Each field in the form must be named {@code ctrl-name}
 * as they will be pushed into a map of structs keyed by ctrl
 */
function saveResult() {

    let result = $(AYM_FORM).serializeJSON();

    let ctrls = {};
    for (var field in result) {
        if (!result.hasOwnProperty(field))
            continue;
        let matcher = field.match(CTRL_FIELD_NAME_RE);
        let ctrlId = matcher[1];
        let fieldName = matcher[2];
        let fieldData = []; fieldData[fieldName] = result[field];
        ctrls[ctrlId] = $.extend(ctrls[ctrlId], fieldData)
    }

    jQuery.ajax({
        type: 'post',
        url: window.location.href,
        contentType: 'application/json',
        data: JSON.stringify(ctrls),
        beforeSend: function() {
            $.blockUI()
        },
        success: function(response) {
            console.log('Saved results, rebinding data ...');
            renderDefinition(response.data.definition);
            bindData(response.data.data)
        },
        complete: function() {
            $.unblockUI()
        }
    })


}

/**
 * Loads the current form results and passes them to the {@code bind} function of each control
 * instance.
 */
function loadResult(callback) {

    jQuery.ajax({
        type: 'get',
        url: window.location.href,
        beforeSend: function() {
            $.blockUI()
        },
        success: function(response) {

            $('#result-dtl').html(getTemplate('form/result-dtl')(response.data));

            renderDefinition(response.data.definition, false);

            // May not have results yet
            if (response.data.data)
                bindData(response.data.data);

            console.log('Loaded results.');


        },
        complete: function() {
            if (typeof callback == 'function')
                callback(this);
            $.unblockUI();
        }
    })

}

$(document).ready(function () {

    AYM_FORM = $('#grid-stack-frm');

    $('#result-save-btn').on('click', saveResult);
    $('#result-del-btn').on('click', deleteResult);

    loadResult();

});