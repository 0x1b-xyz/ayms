/**
 * @param url Endpoint url, expects a Result<T> outcome
 * @param tpl Template used to append rows
 * @param tbl Table we'll append rows into
 */
function rows(url, tpl, tbl) {
    var template = Handlebars.compile(jQuery(tpl).html())
    var tbody = jQuery(tbl + ' > tbody');
    jQuery.ajax('', {
        accepts: {
            json: 'application/json'
        },
        dataType: 'json',
        success: function(result) {
            result.data.forEach(function(row) {
                tbody.append(template(row))
            })
        }
    })

}