const socket = new SockJS('http://localhost:8081/ws');
const stompClient = Stomp.over(socket);


// web socket connection
stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);

    // Sottoscrizione al topic personale
    stompClient.subscribe('/user/topic/exam-outcome', function (notification) {
        let message = notification.body;

        // Append in tabella notifiche
        let row = "<tr><td>" + message + "</td></tr>";
        document.getElementById("notify").insertAdjacentHTML("beforeend", row);
    });
});


// function to render notification
function renderNotification(id, message) {
    let row = `
        <tr data-id="${id}">
            <td>${message}</td>
            <td>
                <button onclick="markAsRead(${id})">Accetta</button>
            </td>
        </tr>`;
    document.getElementById("notify").insertAdjacentHTML("beforeend", row);
}


// retrieve notifications
fetch('/api/v1/outcome-notifications')
    .then(res => res.json())
    .then(notifications => {
        notifications.forEach(n => {
            let row = "<tr><td>" + n.message + "</td></tr>";
            document.getElementById("notify").insertAdjacentHTML("beforeend", row);
        });
    });


// function to mark notification as read
function markAsRead(id) {
    fetch(`/api/v1/notifications/${id}/read`, { method: "POST" })
        .then(() => {
            let row = document.querySelector(`tr[data-id='${id}']`);
            if (row) row.remove();
        });
}
