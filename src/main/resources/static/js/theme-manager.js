/* ==========================================================
    🌗 THEME MANAGER MODULE
    Handles dark/light theme switching and toast feedback.
    Can be imported or included globally.
========================================================== */

export function initThemeToggle(toggleId = "theme-toggle") {
    const toggle = document.getElementById(toggleId);
    const root = document.documentElement;

    // --- Apply saved theme from localStorage ---
    const darkMode = localStorage.getItem("theme") === "dark";
    root.classList.toggle("dark", darkMode);
    if (toggle) toggle.innerHTML = darkMode ? '<i class="fas fa-sun"></i>' : '<i class="fas fa-moon"></i>';

    // --- Handle toggle click ---
    if (toggle) {
        toggle.addEventListener("click", () => {
            const isDark = root.classList.toggle("dark");
            localStorage.setItem("theme", isDark ? "dark" : "light");
            toggle.innerHTML = isDark ? '<i class="fas fa-sun"></i>' : '<i class="fas fa-moon"></i>';
            showToast(isDark ? "🌙 Dark mode enabled" : "☀️ Light mode enabled");
        });
    }

    ensureToastContainer();
}

/* ---------- Toast utilities ---------- */
export function showToast(message, duration = 2500) {
    const container = document.getElementById("toast-container") || ensureToastContainer();
    const toast = document.createElement("div");
    toast.className = "toast";
    toast.textContent = message;
    container.appendChild(toast);

    requestAnimationFrame(() => toast.classList.add("show"));
    setTimeout(() => toast.classList.remove("show"), duration - 500);
    setTimeout(() => toast.remove(), duration);
}

/* ---------- Helper ---------- */
function ensureToastContainer() {
    let container = document.getElementById("toast-container");
    if (!container) {
        container = document.createElement("div");
        container.id = "toast-container";
        document.body.appendChild(container);
    }
    return container;
}
