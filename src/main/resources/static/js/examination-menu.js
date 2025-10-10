/* ==========================================================
    EXAMINATION MENU SCRIPT
    Handles theme toggle and feedback toast
========================================================== */

document.addEventListener("DOMContentLoaded", () => {
    const toggle = document.getElementById("theme-toggle");
    const root = document.documentElement;

    // Carica tema salvato
    if (localStorage.getItem("theme") === "dark") {
        root.classList.add("dark");
        toggle.innerHTML = '<i class="fas fa-sun"></i>';
    }

    toggle.addEventListener("click", () => {
        root.classList.toggle("dark");
        const dark = root.classList.contains("dark");
        toggle.innerHTML = dark ? '<i class="fas fa-sun"></i>' : '<i class="fas fa-moon"></i>';
        localStorage.setItem("theme", dark ? "dark" : "light");
        showToast(dark ? "🌙 Dark mode enabled" : "☀️ Light mode enabled");
    });
});

/* ---------- Toast utility ---------- */
function showToast(msg) {
    const container = document.getElementById("toast-container");
    const toast = document.createElement("div");
    toast.className = "toast";
    toast.textContent = msg;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = "0";
        toast.style.transform = "translateY(20px)";
        setTimeout(() => toast.remove(), 400);
    }, 2800);
}
