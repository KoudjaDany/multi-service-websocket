var stompClient = null;

$(document).ready(function () {
    console.log("Index page is ready");
    $("#send-private").click(function () {
        sendPrivateMessage();
    });
    $("#connect").click(function () {
        connect();
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
            console.log('RECEIVED: '+ frame);
            stompClient.subscribe('/topic/messages', function (message) {
                console.log('message: '+ message);
                showMessage(JSON.parse(message.body).content);
            });
            stompClient.subscribe('/user/topic/messages', function (message) {
                console.log('message: '+ message);
                showPrivateMessage(JSON.parse(message.body).content);
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
    console.log("Sending private message '" + message + "' to user " + userId);
    let body = JSON.stringify({'messageContent': message});
    if (!userId || userId === '') {
        stompClient.send(
            "/app/message",
            {'authKey': apiKey},
            body
        );
    } else {
        stompClient.send(
            "/app/message/" + userId,
            {'authKey': apiKey},
            body
        );
    }

}

function showMessage(message) {
    $("#messages").append("<tr><td style='font-size: 8px'>" + message + "</td></tr>");
}

function showPrivateMessage(message) {
    $("#messages").append("<tr class='alert alert-warning'><td style='font-size: 8px'>" + message + "</td></tr>");
}

function showInfo(message) {
    $("#connected").append("<tr class='alert alert-success'><td>" + message + "</td></tr>");
}

function showError(message) {
    $("#connected").append("<tr class='alert alert-danger'><td>" + message + "</td></tr>");
}
