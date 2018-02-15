var data = null;
var processing = false;

setInterval(function() {
   if (processing != true) {
      processing = true;
      $.ajax({
            url: "comms/toInterface.json",
            dataType: "json",
            async: false,
            data: null,
            success: function(json) {
               data = json;
               processing = false;
            }
         }).fail(function() {
            data = null;
            processing = false;
      });
   }
}, 2000);