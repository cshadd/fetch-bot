var data = null;
var data2 = null;
var processing = false;
var processing2 = false;
var refresh = null;

setInterval(function () {
    refresh = new Date().getTime();
    if (processing != true) {
        processing = true;
        $.ajax({
            url: "comms/toInterface.json?refresh=" + refresh,
            dataType: "json",
            async: false,
            data: null,
            success: function (json) {
                data = json;
                processing = false;
            }
        })
        .fail(function () {
            data = null;
            processing = false;
        });
    }
    if (processing2 != true) {
        processing2 = true;
        $.ajax({
            url: "comms/toRobot.json?refresh=" + refresh,
            dataType: "json",
            async: false,
            data: null,
            success: function (json) {
                data2 = json;
                processing2 = false;
            }
        })
        .fail(function () {
            data2 = null;
            processing2 = false;
        });
    }
}, 2000);
