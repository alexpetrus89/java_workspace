// courses.js
document.addEventListener("DOMContentLoaded", () => {

    // === Theme Toggle ===
    const toggle = document.getElementById("theme-toggle");
    const root = document.documentElement;

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

    // === Row animation with IntersectionObserver ===
    const rows = document.querySelectorAll(".table-row");
    const observer = new IntersectionObserver(entries => {
        for (const entry of entries) {
            if (entry.isIntersecting) {
                entry.target.classList.add("visible");
                observer.unobserve(entry.target);
            }
        }
    }, { threshold: 0.2 });

    for (const row of rows) observer.observe(row);

    // === Welcome toast ===
    setTimeout(() => showToast("📚 Courses loaded successfully"), 700);
});

/* Toast helper */
function showToast(msg) {
    const container = document.getElementById("toast-container");
    if (!container) return;

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
