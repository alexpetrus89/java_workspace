/**
 * Il file HTML student-home.html importa la libreria JavaScript StompJS,
 * che verrà utilizzata per comunicare con il server tramite protocollo di
 * messaggistica STOMP e tramite protocollo WebSocket.
 * Importiamo anche ums-notify.js, che contiene la logica della nostra
 * applicazione client.
 * Il seguente elenco (da src/main/resources/static/js/ums-notify.js)
 * mostra tale file:
 */


// Creazione del client StompJs
// Crea un nuovo client StompJs e lo configura per
// connettersi al broker WebSocket all'indirizzo
// ws://localhost:8081/ums-ws.
const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8081/ums-ws',
});

// Funzione di connessione WebSocket
function connect() {
    stompClient.activate();
}


stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

stompClient.debug = function (msg) {
    console.log('STOMP DEBUG: ' + msg);
};

// Funzione di connessione al server Stomp
stompClient.onConnect = function(frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/notify', function(message) {
        console.log('Received notification: ' + message.body);
        // Visualizza la notifica nella sezione "Notifications"
        let notificationList = document.getElementById('notification-list');
        let notification = document.createElement('div');
        notification.textContent = message.body;
        notificationList.appendChild(notification);
    });
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#notifications").html("");
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    if (stompClient.connected) {
        stompClient.publish({
            destination: "api/v1/examination-outcome/ums/notify",
            body: JSON.stringify({'message': $("#message").val()})
        });
    } else {
        console.log("Non connesso al server");
    }
}

function showMessage(message) {
    $("#notifications-list").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    connect();
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendMessage());
});


// Funzioni di errore WebSocket
stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.debug = function (msg) {
    console.log('STOMP DEBUG: ' + msg);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};


/**
 * Le parti principali di questo file JavaScript da comprendere sono le
 * funzioni stompClient.onConnect e sendName.
 *
 * stompClient viene inizializzato con brokerURL che fa riferimento al
 * percorso /ws, che è dove il nostro server websocket attende le connessioni.
 * Una volta stabilita la connessione, il client si iscrive alla destinazione
 * /topic/notify, dove il server pubblicherà i messaggi di notifica.
 *
 * Quando viene ricevuto un messaggio di notifica su quella destinazione,
 * aggiungerà un elemento paragrafo al DOM per visualizzarlo.
 *
 * La funzione sendMessage() recupera il messaggio inserito dall'utente e
 * utilizza il client STOMP per inviarlo alla destinazione /ums/notify
 * (dove ExaminationOutcomeController#notifyOutcome lo riceverà).
 *
 * La funzione showMessage() viene chiamata quando il client STOMP riceve
 * un messaggio di notifica, e aggiunge il messaggio al DOM.
 */