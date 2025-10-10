/**
 * ======================================================
 * 🧠 Booked Calendar - Page Script
 * Handles theme toggle, toast, and card interactions
 * ======================================================
 */
document.addEventListener("DOMContentLoaded", () => {

    // 🌗 THEME TOGGLE
    const toggle = document.getElementById("theme-toggle");
    const root = document.documentElement;

    // Funzione per applicare il tema e aggiornare l'icona
    const applyTheme = (darkMode) => {
        if (darkMode) {
            root.classList.add("dark");
            if (toggle) toggle.innerHTML = '<i class="fas fa-sun"></i>';
        } else {
            root.classList.remove("dark");
            if (toggle) toggle.innerHTML = '<i class="fas fa-moon"></i>';
        }
        localStorage.setItem("theme", darkMode ? "dark" : "light");
    };

    // Inizializza tema da localStorage
    applyTheme(localStorage.getItem("theme") === "dark");

    // Toggle al click
    if (toggle) {
        toggle.addEventListener("click", () => {
            const dark = !root.classList.contains("dark");
            applyTheme(dark);
            showToast(dark ? "🌙 Dark mode enabled" : "☀️ Light mode enabled");
        });
    }

    // 📌 Toast for booked appeal buttons
    const bookButtons = document.querySelectorAll(".book-button");
    bookButtons.forEach(button => {
        button.addEventListener("click", () => {
            // Mostra il toast senza impedire il submit del form
            showToast("✅ Booking your appeal...");
        });
    });

});

/* --- TOAST FUNCTION --- */
function showToast(message, type = "success", duration = 2200) {
    const container = document.getElementById("toast-container") || createContainer();
    const toast = document.createElement("div");
    toast.className = `toast ${type}`;
    toast.textContent = message;
    container.appendChild(toast);

    // Animazione fade out
    setTimeout(() => {
        toast.style.opacity = "0";
        toast.style.transform = "translateY(10px)";
        setTimeout(() => toast.remove(), 400);
    }, duration);
}

function createContainer() {
    const c = document.createElement("div");
    c.id = "toast-container";
    document.body.appendChild(c);
    return c;
}