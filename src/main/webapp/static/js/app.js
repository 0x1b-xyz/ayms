/**
 * @param url Endpoint url, expects a Result<T> outcome
 * @param tpl Template used to append rows
 * @param tbl Table we'll append rows into
 * @param clear Whether we should clear existing rows
 */
function rows(url, tpl, tbl, clear) {

    if (clear == undefined)
        clear = true;
    var template = Handlebars.compile(jQuery(tpl).html());
    var tbody = jQuery(tbl + ' > tbody');

    jQuery.ajax(url, {
        accepts: {
            json: 'application/json'
        },
        dataType: 'json',
        success: function(result) {

            if (clear) {
                tbody.empty()
            }

            result.data.forEach(function(row) {
                tbody.append(template(row))
            })
        }
    })

}

/**
 * Hooks into the submit on the specified form, serializing the data to send to the form action.
 * Handles the Result when returned, activating the alert when there are errors and attempts to
 * highlight the fields.
 *
 * Assuming the submit was successful will reload data using {@link rows}
 */
function form(frm, tpl, tbl) {

    var form = jQuery(frm);

    form.submit(function (e) {

        var data = form.serializeJSON();
        var action = form.attr('action');

        jQuery.ajax({
            url: action,
            type: 'POST',
            data: data,
            success: function (result) {
                if (!result.success) {
                    form.siblings('.alert').removeClass('hidden');
                    result.fields.forEach(function (field) {
                        form.find(toId(field.name)).parents('.form-group').addClass('has-error')
                    });
                    return;
                }

                form.siblings('.alert').addClass('hidden');
                form.find('.form-group').removeClass('has-error');
                form[0].reset();
                rows(action, tpl, tbl, true)
            }
        });

        e.preventDefault();

    })

}

/**
 * Escapes an element id with a period or colon in it and prepends
 * an '#'
 * @param id
 * @returns {string}
 */
function toId(id) {
    return "#" + id.replace( /(:|\.|\[|\]|,)/g, "\\$1" );
}



var FormTextControl = function(options) {

    var self = this;
    var vars = {
        something: 'jason'
    };

    this.construct = function(options) {
        $.extend(vars, options)
    };

    this.alert = function() {
        alert(vars.something);
    };

    this.construct(options);

};