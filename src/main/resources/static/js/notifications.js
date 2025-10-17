document.addEventListener("DOMContentLoaded", () => {

    const notifyContainer = document.getElementById("notify");

    if (!notifyContainer) {
        console.error("Notification container not found!");
        return;
    }

    // --- Funzione per mostrare toast interni alla card ---
    function createNotificationCard(message, id = null) {
        const notification = document.createElement("div");
        notification.className = "notification-item";
        notification.innerHTML = `
            <p>${message}</p>
            ${id === null ? "" : `<button class="btn-accept" data-id="${id}">Accept</button>`}
        `;

        if (id !== null) {
            const btn = notification.querySelector(".btn-accept");
            btn.addEventListener("click", () => markAsRead(id, notification));
        }

        notifyContainer.prepend(notification); // newest at the top
    }

    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    // --- Funzione per segnare come letto ---
    function markAsRead(id, element) {
        fetch(`/api/v1/notification/${id}/read`, {
            method: "POST",
            headers: { [header]: token }
        })
            .then(res => {
                if (!res.ok) throw new Error("Failed to mark as read");
                element.remove();
            })
            .catch(err => console.error("Error marking notification as read:", err));
    }

    // --- Recupera notifiche preesistenti ---
    fetch('/api/v1/notification/read/all')
        .then(res => res.json())
        .then(notifications => {
            for (const notification of notifications)
                createNotificationCard(notification.message, notification.id);
        })
        .catch(err => console.error("Error fetching notifications:", err));

    // --- WebSocket real-time ---
    const socket = new SockJS('http://localhost:8081/ws');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, frame => {
        console.log('Connected: ' + frame);

        // Sottoscrizione al topic personale
        stompClient.subscribe('/user/topic/exam-outcome', notification => {
            const data = JSON.parse(notification.body);
            createNotificationCard(data.message, data.id);
            showToast("New notification received!");
        });
    });
});

