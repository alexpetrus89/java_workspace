document.addEventListener("DOMContentLoaded", () => {
    const messageDiv = document.querySelector(".result-message");
    if (!messageDiv) return;

    const messageText = messageDiv.textContent.trim().toLowerCase();

    if (messageText.includes("successfully") || messageText.includes("moved")) {
        messageDiv.classList.add("success");
    } else if (messageText.includes("error") || messageText.includes("unable")) {
        messageDiv.classList.add("error");
    }
});

