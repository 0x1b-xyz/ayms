//= require jquery
//= require jquery-ui
//= require jquery.serializejson
//= require jquery.mask
//= require bootstrap
//= require handlebars
//= require handlebars-intl

//= require_tree hbs

/**
 * Base url for templates or just urls. You should rewrite them using the <s:url/> tag so the context
 * switches correctly
 */
var URL_PREFIX = '/';
var TPL_PREFIX = URL_PREFIX + 'static/hbs/';

/**
 * @param url Endpoint url, expects a Result<T> outcome
 * @param tpl Template used to append rows
 * @param tbl Table we'll append rows into
 * @param clear Whether we should clear existing rows
 */
function rows(url, tpl, tbl, clear) {

    if (clear == undefined)
        clear = true;
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
                tbody.append(getTemplate(tpl)(row));
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

/**
 * Not totally guaranteed but not a bad unique id generator. Good 'enuff at least.
 * @returns {string}
 */
function uniqueId() {
    var result, i, j;
    result = '';
    for(j=0; j<32; j++) {
        i = Math.floor(Math.random()*16).toString(16).toUpperCase();
        result = result + i;
    }
    return result;
}

/**
 * Adds formatting functions to handlebars
 */
HandlebarsIntl.registerWith(Handlebars);

/**
 * Adds a useful comparison helper to handlebars.
 *
 * http://doginthehat.com.au/2012/02/comparison-block-helper-for-handlebars-templates/#comment-44
 */
Handlebars.registerHelper('compare', function (lvalue, operator, rvalue, options) {

    var operators, result;

    if (arguments.length < 3) {
        throw new Error("Handlerbars Helper 'compare' needs 2 parameters");
    }

    if (options === undefined) {
        options = rvalue;
        rvalue = operator;
        operator = "===";
    }

    operators = {
        '==': function (l, r) { return l == r; },
        '===': function (l, r) { return l === r; },
        '!=': function (l, r) { return l != r; },
        '!==': function (l, r) { return l !== r; },
        '<': function (l, r) { return l < r; },
        '>': function (l, r) { return l > r; },
        '<=': function (l, r) { return l <= r; },
        '>=': function (l, r) { return l >= r; },
        'typeof': function (l, r) { return typeof l == r; }
    };

    if (!operators[operator]) {
        throw new Error("Handlerbars Helper 'compare' doesn't know the operator " + operator);
    }

    result = operators[operator](lvalue, rvalue);

    if (result) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }

});

/**
 * Prepends the {@link #URL_PREFIX} onto the path given. Will strip a leading slash if
 * it finds one.
 */
Handlebars.registerHelper('url', function(options) {
    var url = options.fn(this);
    console.log(url);
    if (url.startsWith('/'))
        url = url.substring(1, url.length);
    return URL_PREFIX + url;
});

/**
 * Lazily loads and compiles the '.hbs' template from the {@link TPL_PREFIX}, returning
 * an executable template.
 *
 * @param path
 */
function getTemplate(path) {
    if (!Handlebars.templates.hasOwnProperty(path))
        throw new Error("Could not load template from path: " + path);
    return Handlebars.templates[path];
}