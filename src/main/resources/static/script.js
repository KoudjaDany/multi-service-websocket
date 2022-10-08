var stompClient = null;
var notificationCount = 0;
var user = null;

$(document).ready(function () {
    console.log("Index page is ready");
    fetchUserInfo();
    connect();
    fetchMessages();
    $("#connect").hide();
    $("#hidden").hide();
    $("#key-id").hide();
    $("#ws-id").hide();
    $("#send-private").click(function () {
        sendPrivateMessage();
    });
    $("#private-message").submit(function () {
        sendPrivateMessage();
    });


    $("#notifications").click(function () {
        resetNotificationCount();
    });
});

function connect() {
    let apiKey = document.getElementById("key-id").value;
    let wsId = document.getElementById("ws-id").value;

    // Try to set up WebSocket connection with the handshake
    var socket = new SockJS('/ws-register');
    // Create a new StompClient object with the WebSocket endpoint
    stompClient = Stomp.over(socket);

    // Start the STOMP communications, provide a callback for when the CONNECT frame arrives.
    stompClient.connect(
        {'authKey': apiKey, 'ws-id': wsId},
        function (frame) {
            showInfo(frame);
            console.log('RECEIVED: ' + frame);
            stompClient.subscribe('/topic/messages', function (message) {
                console.log('message: ' + message);
                //showMessage(JSON.parse(message.body).text);
                showMessageResponse(JSON.parse(message.body));
            });
            stompClient.subscribe('/user/topic/private-messages', function (message) {
                console.log('message: ' + message);
                //showPrivateMessage(JSON.parse(message.body).text);
                showMessageResponse(JSON.parse(message.body));
            });
            stompClient.subscribe('/topic/global-notifications', function () {
                notificationCount++;
                updateNotificationDisplay();
            });
            stompClient.subscribe('/user/topic/private-notifications', function () {
                notificationCount++;
                updateNotificationDisplay();
            });
        },
        function (err) {
            showError(err);
        }
    );
}

function sendPrivateMessage() {
    let message = document.getElementById("private-message").value;
    let userId = document.getElementById("user-id").value;
    let apiKey = document.getElementById("key-id").value;
    let body = JSON.stringify({'text': message, 'to': userId});
    if (!userId || userId === '') {
        stompClient.send(
            "/app/message",
            {'authKey': apiKey},
            body
        );
    } else {
        stompClient.send(
            "/app/private-message",
            {'authKey': apiKey},
            body
        );
    }
    $("#private-message").val("");
}

function showMessage(message) {
    $("#messages").append("<tr><td style='font-size: 8px'>" + message + "</td></tr>");
}

function showMessageResponse(message) {
    console.log("responseToAppend:", message);
    console.log(user);
    console.log(message.username === user);
    if (message.username === user) {
        showSentMessage(message);
    } else {
        showReceivedMessage(message);
    }
    //$("#messages").append("<tr><td style='font-size: 8px'> <span class='ui-icon-person ui-icon'></span>" + message.username + "</td> : <td style='font-size: 8px'>" + message.text + "</td></tr>");
}

function showPrivateMessage(message) {
    $("#messages").append(`
    <tr class='alert alert-warning'>
        <td style='font-size: 8px'> ` + message + ` </td>
        <td><i class="bi-me fa-message-xmark" ></i></td>
    </tr>`);
}

function showReceivedMessage(message) {
    $("#messages").append(`
                    <div class="row">
                           <div class="col-md-1">
                                <div class="row">
                                    <div class="col-12 align-content-center text-lg-center">
                                        <i class="bi bi-person"></i>
                                    </div>
                                </div>                                 
                                <div class="row">
                                     <div class="col-12 align-content-center text-center small text-muted">
                                     ` + message.username + `
                                     </div>
                                </div>                            
                            </div>
                            <div class="col-md-10 align-self-start">
                                <div class="alert alert-secondary" style="margin-bottom: 0">
                                ` + message.text + `
                                </div>
                            </div>
                            
                        </div>
                        <div class="row">
                            <div class="offset-1 align-self-start">
                                <span class="small text-muted">` + getFormattedDateTime(message.date) + `</span>
                            </div>
                        </div>
    `);
}

function showSentMessage(message) {
    $("#messages").append(`
                        <div class="row">
                            <div class="offset-1 col-md-10 align-self-end">
                                <div class="alert alert-primary" style="margin-bottom: 0">
                                ` + message.text + `
                                </div>
                            </div>
                            <div class="col-md-1">
                                <div class="row">
                                    <div class="col-12 align-content-center text-lg-center">
                                        <i class="bi bi-person-circle"></i>
                                    </div>
                                </div>                                 
                                <div class="row">
                                     <div class="col-12 align-content-center text-center small text-muted">
                                     ` + message.username + `
                                     </div>
                                </div>                            
                            </div>
                        </div>
                        <div class="row">
                            <div class="offset-9 align-self-end">
                                <span class="small text-muted">` + getFormattedDateTime(message.date) + `</span>
                            </div>
                        </div>
    `);
}

function showInfo(message) {
    $("#connected").append("<tr class='alert alert-success'><td>" + message + "</td></tr>");
}

function showError(message) {
    $("#connected").append("<tr class='alert alert-danger'><td>" + message + "</td></tr>");
}


function updateNotificationDisplay() {
    if (notificationCount === 0) {
        $('#notifications').hide();
    } else {
        $('#notifications').show().text(notificationCount);
    }
}

function resetNotificationCount() {
    notificationCount = 0;
    updateNotificationDisplay();
}


function fetchMessages() {
    $.get("/api/v1/messages/all-my-messages", function (data) {
        console.log(data);
        for (let datum of data) {
            console.log(datum);
            showMessageResponse(datum);
        }
    });
}

function fetchUserInfo() {
    $.get("/api/v1/users/current-username", function (data) {
        console.log(data);
        user = data;
    });
}


function getFormattedDateTime(serverDate){
    const date = new Date(serverDate);
    let day = date.toLocaleDateString();
    const time = date.toLocaleTimeString();
    if(date instanceof Date){
        let now = new Date(Date.now());
        if(date.getDate() === now.getDate() && date.getMonth() === now.getMonth() && date.getFullYear() === now.getFullYear() ){
            day = "Today";
        } else if(date.getDate() === now.getDate() -1 && date.getMonth() === now.getMonth() && date.getFullYear() === now.getFullYear()){
            day = "Yesterday"
        }
    }
    return day + ", " + time
}
