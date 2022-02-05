var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    // var socket = new SockJS('/gs-guide-websocket');
    var socket = new SockJS('/myBroker');
    stompClient = Stomp.over(socket);
    stompClient.connect({
        name: '123'
    }, function (frame) {
        setConnected(true);
        console.log('123Connected: ' + frame);
        // 订阅后实现广播
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
            console.log('123Subscribe: ' + greeting);
        });
        // 订阅后实现点对点
        // stompClient.subscribe('/user/1/greetings', function (greeting) {
        stompClient.subscribe('/user/queue/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
            console.log('123Subscribe: ' + greeting);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("123Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()})); // 对应@MessageMapping，广播
    // stompClient.subscribe("/app/hello", res => { // 对应@SubscribeMapping，点对点
    //     console.info("client subscribe");
    //     console.info("client receive response: " + res);
    // });
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});