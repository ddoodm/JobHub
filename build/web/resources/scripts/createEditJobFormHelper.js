/*
 * Adds interactive functionality to Create and Edit job forms
 */

$(document).ready(function ()
{
    $(".datepicker").datepicker({
        format: "d MM yyyy",
        startDate: "today",
        todayBtn: "linked",
        orientation: "bottom auto",
        todayHighlight: true
    });
});

// Auto-update file-picker path
$("#createEditJobForm").on('change', '#uploadFile', function ()
{
    var filePath = $("#uploadFile").val();
    $("#uploadFileFeedback").val(filePath);
});