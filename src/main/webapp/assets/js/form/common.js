//= require app
//= require validator
//= require bootstrap-wysiwyg
//= require jquery.blockUI
//= require jquery.hotkeys
//= require jquery.observer
//= require lodash
//= require gridstack

//= require_tree form/ctrl

var CTRL_MODAL;
var CTRL_MODAL_FRM;
var CTRL_GRID;
var CTRL_FIELD_NAME_RE = /([A-Za-z0-9]*)-([A-Za-z0-9\.]*)/;

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

    /**
     * This is just evil - but this represents the base "class" for ctrl defs. {@link #getCtrlFunction}
     * will look here if no method is found in the target def.
     */
    'Common': {

        /**
         * Generic "bind" function will quietly attempt to map a structure into the fields of a ctrl. Once
         * values are bound we attempt to call a {@code bound} function on the control.
         */
        bind: function (ctrl, data) {
            try {
                
                $.each(data, function(field, value) {

                    // If there's no .FIELD suffix on the field name then we look for 'value'
                    if (field.indexOf('.') == -1)
                        field = 'value';

                    var ctrlField = getCtrlField(ctrl, field);
                    if (!ctrlField.length) {
                        console.log("Could not find ctrl field", ctrl, field);
                        return;
                    }
                    ctrlField.val(value);
                });

                try {
                    getCtrlFunction(ctrl.type, 'bound')(ctrl);
                } catch (e) {
                    // We'll eat this
                }

            } catch (e) {
                console.log("Failed to bind data into ctrl", e, ctrl, data)
            }
        }

    },

    'TextField': {
        label: 'Text Field',
        append: function (ctrl) {
            let heights = {'vertical': 3, 'horizontal': 2, 'none': 2};
            return appendCtrl(ctrl, 0, 50, 20, heights[ctrl.attr.labelAlign], true)
        }
    },

    'TextBlock': {
        label: 'Text Block',
        edit: function (ctrl) {

            // We don't use a validator on this one.
            CTRL_MODAL_FRM.validator('destroy');

            var editor = CTRL_MODAL_FRM.find('#text-editor');
            var editorField = CTRL_MODAL_FRM.find('input[name="text"]');
            editor.wysiwyg();
            editor.bind("DOMSubtreeModified", function () {
                editorField.val(editor.html());
            });
        }
    },

    'TextAreaField': {
        label: 'Text Area Field',
        append: function (ctrl) {
            let heights = {'vertical': 4, 'horizontal': 3, 'none': 2};
            return appendCtrl(ctrl, 0, 50, 20, heights[ctrl.attr.labelAlign], true)
        }
    },

    'PhoneNumberField': {
        label: 'Phone Number Field',
        render: function (ctrl) {
            getCtrlField(ctrl.id, ctrl.name).mask('(000) 000-0000')
        }
    },

    'HeaderText': {
        label: 'Header Text',
        edit: function (ctrl) {
            // We could do this using a #cond on the input tag but this seemed like more fun
            var sizeField = CTRL_MODAL_FRM.find('input[name="size"]');
            if (!ctrl.attr.size) {
                sizeField.filter('[value="1"]').prop('checked', true);
            } else {
                sizeField.filter('[value="' + ctrl.attr.size + '"]').prop('checked', true);
            }
        },
        append: function (ctrl) {
            let heights = {'1': 3, '2': 3, '3': 3, '4': 2, '5': 2};
            return appendCtrl(ctrl, 0, 50, 20, heights[ctrl.attr.size], true)
        }
    },

    'EmployeeField': {

        label: 'Employee Field',

        /**
         * Observes {@link #TOPIC_VALUE_CHANGE} for updates to {@code CompanyField}s.
         */
        init: function () {
            $.observer.subscribe(TOPIC_VALUE_CHANGE, getCtrlFunction('EmployeeField', 'valueChange'));
        },

        /**
         * Unsubscribes from {@link #TOPIC_VALUE_CHANGE}.
         */
        destroy: function (ctrl) {
            $.observer.unsubscribe(TOPIC_VALUE_CHANGE, getCtrlFunction('EmployeeField', 'valueChange'))
        },

        /**
         * Loads any existing CompanyField names into the companyField selection
         */
        edit: function (ctrl) {

            let companyField = CTRL_MODAL_FRM.find('select[name="companyField"]');
            $.each(CTRL_INSTANCES, function (name, ctrl) {
                if (ctrl.type == 'CompanyField') {
                    $(companyField).append('<option value="' + ctrl.id + '">' + ctrl.name + '</option>')
                }
            });

        },

        append: function (ctrl) {
            let heights = {'vertical': 3, 'horizontal': 2, 'none': 2};
            return appendCtrl(ctrl, 0, 50, 20, heights[ctrl.attr.labelAlign], true)
        },

        render: function (ctrl) {

            // Publish value changes
            let employeeField = getCtrlField(ctrl.name, ctrl.name);

            employeeField.autocomplete({
                hint: true,
                highlight: true,
                minLength: 1,
                source: function (request, cb) {
                    $.get('/employee/search',
                        $.extend(request, {
                            field: 'lastName'
                        }),
                        function (response) {
                            cb($.map(response.data, function (employee) {
                                return $.extend(employee, {
                                    label: employee.lastName + ', ' + employee.firstName,
                                    value: employee.id
                                })
                            }))
                        }
                    )
                },
                select: function (event, ui) {
                    employeeField.val(ui.item.lastName + ', ' + ui.item.firstName);
                    return false
                }
            });

            // employeeField.change(function() {
            //     let companyId = $(this).find('option:selected').val();
            //     $.observer.publish(TOPIC_VALUE_CHANGE, ctrl, companyId);
            // });

        },

        /**
         * Called when a value changes on the form. We're listening for company fields here and when the
         * field name matches one we expect we'll load the employees into this field.
         */
        valueChange: function (evtCtrl) {

            if (evtCtrl.type == 'CompanyField') {
                let companyId = getCtrlField(evtCtrl.name, evtCtrl.name).find('option:selected').val();
                console.log('got updated company id: ' + companyId);
                if (companyId) {
                    $.each(CTRL_INSTANCES, function (name, ctrl) {
                        if (ctrl.type == 'EmployeeField') {
                            if (ctrl.attr.companyField == evtCtrl.name) {
                                getCtrlFunction('EmployeeField', 'updateForCompany')(ctrl, companyId);
                            }
                        }
                    })
                }
            }

        },

        /**
         * Retrieves the employees for the specified company and loads them into the field
         */
        updateForCompany: function (ctrl, companyId) {

            $.ajax({
                url: URL_PREFIX + 'employee',
                data: {companyId: companyId},
                dataType: 'json',
                success: function (response) {
                    let field = getCtrlField(ctrl.name, ctrl.name);
                    field.find('option').remove();
                    response.data.forEach(function (employee) {
                        field.append('<option value="' + employee.id + '">' + employee.firstName + ' ' + employee.lastName + '</option>');
                    });
                }
            })

        }
    },

    'EmailField': {
        label: 'Email Field',
        append: function (ctrl) {
            let heights = {'vertical': 3, 'horizontal': 2, 'none': 2};
            return appendCtrl(ctrl, 0, 50, 20, heights[ctrl.attr.labelAlign], true)
        }
    },

    'CurrencyField': {
        label: 'Currency Field',
        append: function (ctrl) {
            let heights = {'vertical': 3, 'horizontal': 2, 'none': 2};
            return appendCtrl(ctrl, 0, 50, 20, heights[ctrl.attr.labelAlign], true)
        },
        render: function (ctrl) {
            getCtrlField(ctrl.name, ctrl.name).mask("#,##0.00", {reverse: true})
        }
    },

    'CompanyField': {

        label: 'Company Field',

        append: function (ctrl) {
            var heights = {'vertical': 3, 'horizontal': 2, 'none': 2};
            return appendCtrl(ctrl, 0, 50, 20, heights[ctrl.attr.labelAlign], true)
        },

        render: function (ctrl) {

            // Publish value changes
            let companyField = getCtrlField(ctrl.name, ctrl.name);

            // companyField.change(function() {
            //     let companyId = $(this).find('option:selected').val();
            //     $.observer.publish(TOPIC_VALUE_CHANGE, ctrl, companyId);
            // });

            companyField.autocomplete({
                hint: true,
                highlight: true,
                minLength: 1,
                source: function (request, cb) {
                    $.get('/company/search',
                        $.extend(request, {
                            field: 'name'
                        }),
                        function (response) {
                            cb($.map(response.data, function (company) {
                                return $.extend(company, {
                                    label: company.name,
                                    value: company.id
                                })
                            }))
                        }
                    )
                },
                select: function (event, ui) {
                    companyField.val(ui.item.name);
                    return false
                }
            });

        }
    },

    'ClientField': {
        label: "Client Field",
        append: function (ctrl) {
            return appendCtrl(ctrl, 0, 99, 12, 9, true)
        },

        /**
         * Sets up the auto-completes to load client data into the client form fields
         */
        render: function (ctrl) {

            let fields = ['lastName', 'firstName', 'ssn'];

            fields.forEach(function (field) {

                let targetField = getCtrlField(ctrl.name, field);

                $(targetField).autocomplete({
                    hint: false,
                    highlight: true,
                    minLength: 1,
                    source: function (request, cb) {
                        $.get('/client/search',
                            $.extend(request, {
                                field: field
                            }),
                            function (response) {
                                let options = $.map(response.data, function (client) {
                                    return $.extend(client, {
                                        label: '(' + client[field] + '): ' + client.lastName + ', ' + client.firstName,
                                        value: client.id
                                    });
                                });
                                cb(options);
                            })
                    },
                    select: function (event, ui) {
                        let client = $.extend(ui.item, {
                            'address.line1': ui.item.address.line1,
                            'address.line2': ui.item.address.line2,
                            'address.city': ui.item.address.city,
                            'address.state': ui.item.address.state,
                            'address.zipcode': ui.item.address.zipcode
                        });

                        // We don't bind these fields into the form so this saves on error messages
                        $.each(['address','company','label','value'], function(i,key) {
                            delete client[key];
                        });

                        getCtrlFunction('ClientField', 'bind')(ctrl, client);
                        return false
                    }
                })


            });

        },

        /**
         * Called after data is bound. We use this opportunity to lock the fields for read-only
         * when a client-id is selected.
         */
        bound: function(ctrl) {

            let clientId = getCtrlField(ctrl.name, 'id');



        }

    }
};

/**
 * Call 'init' on each control def. Still need to figure out when/where to call ctrlDef#destroy?
 */
$.each(CTRL_DEFS, function (ctrlDefName) {
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
    if (func == undefined) {
        func = CTRL_DEFS['Common'][functionName];
        if (func == undefined)
            throw new Error("Could not find " + ctrlType + "#" + functionName);
    }
    return func
}

/**
 * Calls a function on a CTRL_DEF with the {@code ctrl}
 *
 * @param functionName Name of the function (will be suffixed with {@code _CTRLID})
 * @param ctrl Control to run the function on
 *
 * @return Result of function call or {@code null} if function does not exist.
 */
function invokeCtrlFunction(functionName, ctrl) {
    try {
        var ctrlFunction = getCtrlFunction(ctrl.type, functionName);
        return ctrlFunction(ctrl);
    } catch (e) {
        return null
    }
}

/**
 * Appends the output of the {@code ctrl-CTRLTYPE-render} template into the layout CTRL_GRID and
 * sticks the ctrl definition into {@link #CTRL_INSTANCES}.
 */
function appendCtrl(ctrl, x, y, width, height, editable) {

    CTRL_INSTANCES[ctrl.name] = ctrl;

    let widget = getTemplate('form/ctrl/wrapper')({
        name: ctrl.name,
        x: x, y: y, width: width, height: height,
        editable: editable
    });
    CTRL_GRID.addWidget(widget);

    if (editable) {
        getCtrlContent(ctrl).siblings('.grid-stack-item-edit').find('a.edit').on('click', function() {
            editCtrl(ctrl.name)
        })
    }

    let widgetContent = getCtrlContent(ctrl);
    widgetContent.html(getTemplate('form/ctrl/' + ctrl.type + '/render')(ctrl));

    invokeCtrlFunction('render', ctrl);

    return true;

}

/**
 * Clears all control data from the grid by calling {@link #removeCtrl}
 */
function clearCtrls() {

    console.log("Clearing all controls ...");
    $.each(CTRL_INSTANCES, function(name, ctrl) {
        removeCtrl(ctrl)
    });

}

/**
 * Removes a control from the GRID_CTRL and {@link #CTRL_INSTANCES}
 *
 * @param ctrl Instance to remove
 */
function removeCtrl(ctrl) {

    let widget = getCtrlContent(ctrl).parent();

    CTRL_GRID.removeWidget(widget);
    widget.remove();

    invokeCtrlFunction('remove', ctrl);

    delete CTRL_INSTANCES[ctrl.name];
}

/**
 * Loads all controls for the form definition
 *
 * @param editable When true the grid will not be locked after controls are appended.
 * @param callback Called before the UI is unblocked in the success hook
 */
function loadCtrls(editable, callback) {

    console.log("Loading controls ...");

    jQuery.ajax({
        type: 'get',
        url: window.location.href,
        contentType: 'application/json',
        beforeSend: function () {
            $.blockUI();
        },
        success: function (response) {
            if (response.data) {
                renderDefinition(response.data, editable)
            }
            if (!editable)
                CTRL_GRID.disable();
        },
        complete: function () {
            if (typeof callback == 'function')
                callback(this);
            $.unblockUI();
        }
    });

}

/**
 * Performs the actual append of controls into the grid. Clears any existing controls
 *
 * @param definition The form definition structure
 * @param editable Whether the controls are editable or not
 */
function renderDefinition(definition, editable) {
    clearCtrls();
    console.log("Rendering definition into the grid ...", definition);
    definition.ctrls.forEach(function (ctrl) {
        appendCtrl(ctrl,
            ctrl.layout.x, ctrl.layout.y, ctrl.layout.width, ctrl.layout.height, editable)
    })
}

/**
 * Binds data into a ctrl using a list of {@link FormData} structures. This is a list of
 * ctrl, name and value. This list will be transformed into a map of name/value pairs
 * that will be passed to the {@code bind} function on the ctrl type.
 *
 * @param data An array of FormData structures
 */
function bindData(data) {

    let byCtrl = {};

    $.each(data, function (index, tuple) {
        if (!byCtrl.hasOwnProperty(tuple.ctrl))
            byCtrl[tuple.ctrl] = {};
        byCtrl[tuple.ctrl][tuple.name] = tuple.value
    });

    $.each(byCtrl, function (name, fields) {

        let ctrl = null;
        try {
            let ctrl = getCtrlInstance(name);
            getCtrlFunction(ctrl.type, 'bind')(ctrl, fields)
        } catch (e) {
            console.log(e, "Could not find ctrl", ctrl, fields)
        }


    });

}

/**
 * Finds the {@code .grid-stack-item-content} for a control by searching {@code data-ctrl-name]
 *
 * @param ctrl Ctrl instance or name of control
 */
function getCtrlContent(ctrl) {
    let name = (ctrl.hasOwnProperty('name')) ? ctrl.name : ctrl;
    return $('div[data-ctrl-name="' + name + '"] .grid-stack-item-content');
}

/**
 * Looks up a field by id within the {@code #grid-stack-frm}
 *
 * @param ctrl Ctrl instance
 * @param fieldName Name of rendered field in form. If not specified returns all fields
 */
function getCtrlField(ctrl, fieldName) {
    if (fieldName != undefined)
        return $('#grid-stack-frm').find(toId(ctrl.name + '-' + fieldName));
    return $('#grid-stack-frm').find('[id^=' + ctrl.name + '-]');
}

/**
 * Returns the specified ctrl definition from {@link #CTRL_INSTANCES}
 * 
 * @param name Name of control to find
 */
function getCtrlInstance(name) {
    if (!CTRL_INSTANCES.hasOwnProperty(name))
        throw new Error("Could not find control: " + name);
    return CTRL_INSTANCES[name];
}

$(document).ready(function () {

    Handlebars.registerHelper('ctrl-hbs', function (type, hbs) {
        return getTemplate('form/ctrl/' + type + '/' + hbs)(this)
    });

    var gridStack = jQuery('.grid-stack');
    gridStack.gridstack({
        cellHeight: 15,
        verticalMargin: 10
    });

    CTRL_GRID = gridStack.data('gridstack');
    CTRL_MODAL = $('#ctrl-modal');
    CTRL_MODAL_FRM = CTRL_MODAL.find('#ctrl-modal-frm');

});
