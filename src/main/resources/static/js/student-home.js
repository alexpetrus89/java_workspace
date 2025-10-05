document.addEventListener("DOMContentLoaded", () => {
    const cards = document.querySelectorAll(".card");
    let audioUnlocked = false;

    // Sblocca l'audio al primo click o tocco
    const unlockAudio = () => {
        const silentAudio = new Audio("/static/sounds/mixkit-fast-rocket-whoosh-1714.wav");
        silentAudio.volume = 0.0001; // praticamente muto
        silentAudio.play().catch(() => {});
        audioUnlocked = true;
        document.removeEventListener("click", unlockAudio);
        document.removeEventListener("touchstart", unlockAudio);
    };

    document.addEventListener("click", unlockAudio);
    document.addEventListener("touchstart", unlockAudio);

    cards.forEach(card => {
        const hoverSound = new Audio(card.dataset.sound);
        hoverSound.volume = 0.2;

        hoverSound.addEventListener("canplaythrough", () => console.log("Audio ready:", hoverSound.src));
        hoverSound.addEventListener("error", (e) => console.error("Audio error:", e));

        card.addEventListener("mouseenter", () => {
            if (!audioUnlocked) return; // blocca finché non sbloccato
            hoverSound.currentTime = 0;
            hoverSound.play().catch(() => {});
        });

        card.addEventListener("click", (e) => {
            if (audioUnlocked) {
                hoverSound.currentTime = 0;
                hoverSound.play().catch(() => {});
            }

            // Effetto ripple
            const rect = card.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;
            card.style.setProperty("--ripple-x", `${x}px`);
            card.style.setProperty("--ripple-y", `${y}px`);
            card.classList.add("ripple");

            // Vibrate su mobile
            if (navigator.vibrate) navigator.vibrate(30);

            // Mostra toast
            const toastMsg = card.dataset.toast || "Opening...";
            showToast(toastMsg, "success");

            setTimeout(() => card.classList.remove("ripple"), 600);
        });
    });
});

/* --- TOAST FUNCTION --- */
function showToast(message, type = "success") {
    const container = document.getElementById("toast-container");
    const toast = document.createElement("div");
    toast.classList.add("toast", type);
    toast.textContent = message;

    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = "0";
        toast.style.transform = "translateY(20px)";
        setTimeout(() => toast.remove(), 500);
    }, 2500);
}




