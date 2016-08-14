//= require form/common

var GRID_FRM;

/**
 * Collects the results of the form and saves them
 */
function saveResult() {

    let result = $(GRID_FRM).serializeJSON();
    console.log(result);

    let ctrls = {};
    for (var field in result) {
        if (!result.hasOwnProperty(field))
            continue;
        let matcher = field.match(CTRL_ID_RE);
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
        success: function() {
            console.log('Saved results.')
        },
        complete: function() {
            loadResult();
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

            response.data.data.forEach(function (ctrlData) {
                try {
                    let ctrl = getCtrlInstance(ctrlData.ctrl);
                    console.log('binding into', ctrl);
                    try {
                        getCtrlFunction(ctrl.type, 'bind')(ctrl, ctrlData.fields)
                    } catch (e) {
                        console.log(e, "Could not bind data into ctrl", ctrlData, ctrl)
                    }
                } catch (e) {
                    console.log(e, "Could not find ctrl", ctrlData)
                }

            });

            console.log('Loaded results.')
        },
        complete: function() {
            if (typeof callback == 'function')
                callback(this);
            $.unblockUI();
        }
    })

}

$(document).ready(function () {

    GRID_FRM = $('#grid-stack-frm');

    $('#grid-stack-frm-submit').on('click', saveResult);

    loadCtrls(false, function() {
        loadResult()
    });

});