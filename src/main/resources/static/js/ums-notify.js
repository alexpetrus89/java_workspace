/**
 * Il file HTML student-home.html importa la libreria JavaScript StompJS,
 * che verrà utilizzata per comunicare con il nostro server tramite STOMP
 * tramite websocket.
 * Importiamo anche ums-notify.js, che contiene la logica della nostra
 * applicazione client.
 * Il seguente elenco (da src/main/resources/static/js/ums-notify.js)
 * mostra tale file:
 */


const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8081/ws',
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/notify', (outcome) => {
        showGreeting(JSON.parse(outcome.body).content);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
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

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    stompClient.publish({
        destination: "api/v1/examination-outcome/ums/notify",
        body: JSON.stringify({'message': $("#message").val()})
    });
}

function showMessage(message) {
    $("#notifications").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
});


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