var data = null;
var data2 = null;
var processing = false;

setInterval(function () {
    if (processing != true) {
        processing = true;
        $.ajax({
            url: "comms/toInterface.json?refresh=" + new Date().getTime(),
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
        $.ajax({
            url: "comms/toRobot.json?refresh=" + new Date().getTime(),
            dataType: "json",
            async: false,
            data: null,
            success: function (json) {
                data2 = json;
                processing = false;
            }
        })
        .fail(function () {
            data2 = null;
            processing = false;
        });
    }
}, 2000);
