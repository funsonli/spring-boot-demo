$(function () {
    $("#locales").change(function () {
        var lang = $("#locales").val();
        if (lang != "") {
            window.location.replace("/i18n?lang=" + lang);
        }
    });
});