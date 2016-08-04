//= require app
//= require validator
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

/**
 * Map of controls that are present in the GRID
 */
var CTRL_INSTANCES = {};

/**
 * Name of topic where ctrl value changes are published
 */
var TOPIC_VALUE_CHANGE = 'ctrlValueChange';

/**
 * These are effectively the control "classes", defining the behaviours of the controls we support
 */
var CTRL_DEFS = {

    'TextField': {
        label: 'Text Field',
        append: function(ctrlId, ctrlType, ctrlAttr) {
            let heights = {'vertical':3,'horizontal':2,'none':2};
            return appendCtrl(ctrlId, ctrlType, ctrlAttr, 0, 50, 20, heights[ctrlAttr.labelAlign], true)
        }
    },

    'TextBlock': {
        label: 'Text Block',
        edit: function(ctrlId, ctrlType, ctrlAttr) {

            // We don't use a validator on this one.
            CTRL_MODAL_FRM.validator('destroy');

            var editor = CTRL_MODAL_FRM.find('#text-editor');
            var editorField = CTRL_MODAL_FRM.find('input[name="text"]');
            editor.wysiwyg();
            editor.bind("DOMSubtreeModified",function(){
                editorField.val(editor.html());
            });
        }
    },

    'TextAreaField': {
        label: 'Text Area Field'
    },

    'PhoneNumberField': {
        label: 'Phone Number Field',
        render: function(ctrlId, ctrlType, ctrlAttr) {
            getCtrlRenderField(ctrlId, ctrlAttr.name).mask('(000) 000-0000')
        }
    },

    'HeaderText': {
        label: 'Header Text',
        edit: function(ctrlId, ctrlType, ctrlAttr) {
            // We could do this using a #cond on the input tag but this seemed like more fun
            var sizeField = CTRL_MODAL_FRM.find('input[name="size"]');
            if (!ctrlAttr.size) {
                sizeField.filter('[value="1"]').prop('checked', true);
            } else {
                sizeField.filter('[value="' + ctrlAttr.size + '"]').prop('checked', true);
            }
        },
        append: function(ctrlId, ctrlType, ctrlAttr) {
            let heights = {'1': 3, '2': 3, '3': 3, '4': 2, '5': 2};
            return appendCtrl(ctrlId, ctrlType, ctrlAttr, 0, 50, 20, heights[ctrlAttr.size], true)
        }
    },

    'EmployeeField': {

        label: 'Employee Field',

        /**
         * Observes {@link #TOPIC_VALUE_CHANGE} for updates to {@code CompanyField}s.
         */
        init: function() {
            $.observer.subscribe(TOPIC_VALUE_CHANGE, getCtrlFunction('EmployeeField', 'valueChange'));
        },

        /**
         * Unsubscribes from {@link #TOPIC_VALUE_CHANGE}.
         */
        destroy: function(ctrlId, ctrlType, ctrlAttr) {
            $.observer.unsubscribe(TOPIC_VALUE_CHANGE, getCtrlFunction('EmployeeField', 'valueChange'))
        },

        /**
         * Loads any existing CompanyField names into the companyField selection
         */
        edit: function(ctrlId, ctrlType, ctrlAttr) {

            let companyField = CTRL_MODAL_FRM.find('select[name="companyField"]');
            $.each(CTRL_INSTANCES, function(ctrlId) {
                let ctrlDef = CTRL_INSTANCES[ctrlId];
                if (ctrlDef.type == 'CompanyField') {
                    $(companyField).append('<option value="' + ctrlDef.id + '">' + ctrlDef.attr.name + '</option>')
                }
            });

        },

        append: function(ctrlId, ctrlType, ctrlAttr) {
            let heights = {'vertical':3,'horizontal':2,'none':2};
            return appendCtrl(ctrlId, ctrlType, ctrlAttr, 0, 50, 20, heights[ctrlAttr.labelAlign], true)
        },

        render: function(ctrlId, ctrlType, ctrlAttr) {
            console.log("Rendering", ctrlType, ctrlAttr);
        },

        /**
         * Called when a value changes on the form. We're listening for company fields here and when the
         * field name matches one we expect we'll load the employees into this field.
         */
        valueChange: function(evtCtrlId, evtCtrlType, evtCtrlAttr) {

            if (evtCtrlType == 'CompanyField') {
                let companyId = getCtrlRenderField(evtCtrlId, evtCtrlAttr.name).find('option:selected').val();
                console.log('got updated company id: ' + companyId);
                if (companyId) {
                    $.each(getCtrlInstances(), function(ctrlId) {
                        let ctrl = CTRL_INSTANCES[ctrlId];
                        if (ctrl.type == 'EmployeeField') {
                            if (ctrl.attr.companyField == evtCtrlId) {
                                getCtrlFunction('EmployeeField', 'updateForCompany')(ctrl.id, ctrl.type, ctrl.attr, companyId);
                            }
                        }
                    })
                }
            }

        },

        updateForCompany: function(ctrlId, ctrlType, ctrlAttr, companyId) {

            console.log('@TODO NEED TO CONSTRAIN BY ID');

            $.ajax({
                url: URL_PREFIX + 'employee',
                data: {companyId: companyId},
                dataType: 'json',
                success: function(response) {
                    let field = getCtrlRenderField(ctrlId, ctrlAttr.name);
                    field.find('option').remove();
                    response.data.forEach(function(employee) {
                        field.append('<option value="' + employee.id + '">' + employee.firstName + ' ' + employee.lastName + '</option>');
                    });
                }
            })

        }
    },

    'EmailField': {
        label: 'Email Field',
        append: function(ctrlId, ctrlType, ctrlAttr) {
            let heights = {'vertical':3,'horizontal':2,'none':2};
            return appendCtrl(ctrlId, ctrlType, ctrlAttr, 0, 50, 20, heights[ctrlAttr.labelAlign], true)
        }
    },

    'CurrencyField': {
        label: 'Currency Field',
        append: function(ctrlId, ctrlType, ctrlAttr) {
            let heights = {'vertical':3,'horizontal':2,'none':2};
            return appendCtrl(ctrlId, ctrlType, ctrlAttr, 0, 50, 20, heights[ctrlAttr.labelAlign], true)
        },
        render: function(ctrlId, ctrlType, ctrlAttr) {
            getCtrlRenderField(ctrlId, ctrlAttr.name).mask("#,##0.00", {reverse: true})
        }
    },

    'CompanyField': {
        label: 'Company Field',
        append: function(ctrlId, ctrlType, ctrlAttr) {
            var heights = {'vertical':3,'horizontal':2,'none':2};
            return appendCtrl(ctrlId, ctrlType, ctrlAttr, 0, 50, 20, heights[ctrlAttr.labelAlign], true)
        },
        render: function(ctrlId, ctrlType, ctrlAttr) {

            // Publish value changes
            getCtrlRenderField(ctrlId, ctrlAttr.name).change(function() {
                let companyId = $(this).find('option:selected').val();
                $.observer.publish(TOPIC_VALUE_CHANGE, ctrlId, ctrlType, ctrlAttr, companyId);
            });

            // Load companies in
            $.ajax({
                url: URL_PREFIX + 'company',
                dataType: 'json',
                success: function(response) {
                    let field = getCtrlRenderField(ctrlId, ctrlAttr.name);
                    response.data.forEach(function(company) {
                        field.append('<option value="' + company.id + '">' + company.name + '</option>');
                    });
                }
            })

        }
    }
};

/**
 * Call 'init' on each control def. Still need to figure out when/where to call ctrlDef#destroy?
 */
$.each(CTRL_DEFS, function(ctrlDefName) {
    try {
        getCtrlFunction(ctrlDefName, 'init')();
    } catch (e) {
        //console.log('No init function found for ' + ctrlDefName, e)
    }
});

/**
 * Finds a function on a CTRL_DEF
 *
 * @param ctrlType Type in {@link #CTRL_DEFS}
 * @param functionName Name of function in the control definition
 */
function getCtrlFunction(ctrlType, functionName) {
    let func = CTRL_DEFS[ctrlType][functionName];
    if (func == undefined)
        throw new Error("Could not find " + ctrlType + "#" + functionName);
    return func
}

/**
 * Calls a function on a CTRL_DEF with the {@code ctrlId, ctrlType and ctrlAttr}
 *
 * @param functionName Name of the function (will be suffixed with {@code _CTRLID})
 * @param ctrlId Identifier of control
 * @param ctrlType Type of control
 * @param ctrlAttr Control attributes
 *
 * @return Result of function call or {@code null} if function does not exist.
 */
function invokeCtrlFunction(functionName, ctrlId, ctrlType, ctrlAttr) {
    try {
        var ctrlFunction = getCtrlFunction(ctrlType, functionName);
        return ctrlFunction(ctrlId, ctrlType, ctrlAttr);
    } catch (e) {
        return null
    }
}

/**
 * Appends the output of the {@code ctrl-CTRLTYPE-render} template into the layout CTRL_GRID and
 * sticks the ctrl definition into {@link #CTRL_INSTANCES}.
 */
function appendCtrl(ctrlId, ctrlType, ctrlAttr, x, y, width, height, editable) {

    CTRL_INSTANCES[ctrlId] = {
        id: ctrlId,
        type: ctrlType,
        attr: ctrlAttr
    };

    let widget = getTemplate('ctrl/wrapper')({
        id: ctrlId,
        x: x, y: y, width: width, height: height,
        editable: editable 
    });
    CTRL_GRID.addWidget(widget);

    let widgetContent = getCtrlContent(ctrlId);
    widgetContent.html(getTemplate('ctrl/' + ctrlType + '/render')(CTRL_INSTANCES[ctrlId]));

    invokeCtrlFunction('render', ctrlId, ctrlType, ctrlAttr);

    return true;

}

/**
 * Loads all controls for the form definition
 * 
 * @param editable When true the grid will not be locked after controls are appended.
 */
function loadCtrls(editable) {

    console.log("Loading controls ...");
    
    jQuery.ajax({
        type: 'get',
        url: window.location.href + '/ctrl',
        contentType: 'application/json',
        beforeSend: function() {
            $.blockUI();
        },
        success: function(response) {
            if (response.data) {
                response.data.forEach(function(ctrl) {
                    appendCtrl(ctrl.id, ctrl.type, ctrl.attr,
                        ctrl.layout.x, ctrl.layout.y, ctrl.layout.width, ctrl.layout.height, editable)
                });
            }
            if (!editable)
                CTRL_GRID.disable();
        },
        complete: function() {
            $.unblockUI();
        }
    });

}

/**
 * Finds the {@code .grid-stack-item-content} for a control
 */
function getCtrlContent(ctrlId) {
    return $('div[data-ctrl-id="' + ctrlId + '"] .grid-stack-item-content');
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

/**
 * Returns all control instances
 */
function getCtrlInstances() {
    return CTRL_INSTANCES;
}

/**
 * Returns the specified ctrl definition from {@link #CTRL_INSTANCES}
 */
function getCtrlInstance(ctrlId) {
    if (!CTRL_INSTANCES.hasOwnProperty(ctrlId))
        throw new Error("Could not find control instance with id: " + ctrlId);
    return CTRL_INSTANCES[ctrlId];
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

});
