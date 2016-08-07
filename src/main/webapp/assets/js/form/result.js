//= require form/common

var GRID_FRM;

/**
 * Collects the results of the form and saves them
 */
function saveResult() {



}

$(document).ready(function () {

    GRID_FRM = $('#grid-stack-frm');

    $('#grid-stack-frm-submit').on('click', saveResult);

    loadCtrls(false);

});