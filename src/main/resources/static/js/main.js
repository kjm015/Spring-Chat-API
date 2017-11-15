var messagePos = 1;

var id = null;
var run= false;

id = localStorage.getItem("id");

$(window).on('load', function() {
    if(id) {
        run = true;
        $('.register').hide();
        $('.messaging').show();
        getMessages();
    }
});

$(function() {
    $("#register").click(function () {
        var name = $("#Name").val();
        var url = "user/register";

        $.ajax({
            url: url,
            type: 'PUT',
            timeout: 1000,
            data: "name=" + name,
            success: function(data) {
                console.log("registered user name: " + data.name +  "id:" + data.id);
                id = data.id;
                localStorage.setItem("id", id);
                run = true;
                $('.register').hide();
                $('.messaging').show();
                getMessages();
            },
            error: function() {
              alert("No Server Connection!");
            }
        });
    });
});

    function getMessages() {
            function update() {
                var url = "message/getMessages";
                $.ajax({
                    url: url,
                    timeout: 1000,
                    type: 'GET',
                    data: "id=" + messagePos,
                    success: function(json) {
                        if (json.length !==0 && (messagePos <= json[json.length-1].id || messagePos===1)) {
                            for (let i = 0; i < json.length; i++) {
                                $("#msg").append(json[i].user.name + ": " + json[i].message + "<br / >");
                                messagePos = json[i].id;
                            }
                            messagePos++;
                        }
                    },
                    error: function() {
                        console.log("error");
                    }
                 });
            }
        var refreshId = setInterval(update, 1000);
        update();
    }
$(function() {
    $("input").keypress(function(event) {
        if (event.which == 13 && run) {
            event.preventDefault();
            sendMessage();
        }
    });
    $("#send").click(function () {
       sendMessage();
    });
});

function sendMessage() {
    var message = $("#message").val();
    var url = "message/send";
    $.ajax({
        url: url,
        type: 'PUT',
        timeout: 1000,
        data: "text=" + message + "&id=" + id,
        success: function (json) {
            console.log("Message id: " + json.id + " content: " + json.message + " sent.");
            $("#message").val('');
        },
        error: function() {
            console.log("error");
        }
    });
}