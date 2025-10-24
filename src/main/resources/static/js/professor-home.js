// professor-home.js
document.addEventListener("DOMContentLoaded", () => {

    // === Theme Toggle (safe guard if missing) ===
    const toggle = document.getElementById("theme-toggle");
    if (toggle) {
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
    }

    // === Card reveal animation using IntersectionObserver ===
    const cards = document.querySelectorAll(".card");
    const observer = new IntersectionObserver(entries => {
        for (const entry of entries) {
            if (entry.isIntersecting) {
                entry.target.classList.add("visible");
            }
        }
    }, { threshold: 0.25 });

    for (const card of cards) observer.observe(card);

    // === Welcome toast ===
    const nameSpan = document.querySelector(String.raw`[sec\:authentication='name']`);
    if (nameSpan && nameSpan.textContent.trim() !== "") {
        setTimeout(() => showToast(`Welcome back, ${nameSpan.textContent.trim()}! 👋`), 600);
    }

    // === Example: load notifications (simulate) ===
    // If you have a notifications API, replace this with actual fetch.
    const notifyContainer = document.getElementById("notify");
    if (notifyContainer) {
        // Dummy sample - you can populate based on real WS/SSE
        const sample = [
            { id: 1, text: "New exam appeal submitted", action: "/user_professor/examinations" },
            { id: 2, text: "Course enrollment updated", action: "/api/v1/course/read/professor" }
        ];
        for (const item of sample) {
            const row = document.createElement("div");
            row.className = "notify-row show";
            row.innerHTML = `<div class="notify-text">${item.text}</div>
                <div><a class="btn" href="${item.action}">Open</a></div>`;
            notifyContainer.appendChild(row);
        }
    }
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
