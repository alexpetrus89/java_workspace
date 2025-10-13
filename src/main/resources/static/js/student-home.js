/* ==========================================================
    STUDENT HOME SCRIPT
    Handles theme toggle, card animation, and toast feedback
   ========================================================== */

document.addEventListener("DOMContentLoaded", () => {

    // === Theme Toggle ===
    const toggle = document.getElementById("theme-toggle");
    const root = document.documentElement;

    // Load stored theme
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

    // === Card Scroll Animation ===
    const cards = document.querySelectorAll(".card");
    const observer = new IntersectionObserver(entries => {
        entries.forEach(e => {
            if (e.isIntersecting) e.target.classList.add("visible");
        });
    }, { threshold: 0.3 });
    cards.forEach(card => observer.observe(card));

    // === Welcome Toast ===
    const nameSpan = document.querySelector("[sec\\:authentication='name']");
    if (nameSpan && nameSpan.textContent.trim() !== "") {
        setTimeout(() => showToast(`Welcome back, ${nameSpan.textContent.trim()}! 👋`), 700);
    }
});

/* ==========================================================
    Toast helper
   ========================================================== */
function showToast(msg) {
    const container = document.getElementById("toast-container");
    if(!container) return;

    const toast = document.createElement("div");
    toast.className = "toast";
    toast.textContent = msg;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = "0";
        toast.style.transform = "translateY(20px)";
        setTimeout(() => toast.remove(), 400);
    }, 3300);
}




