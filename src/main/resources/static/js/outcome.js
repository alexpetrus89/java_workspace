document.addEventListener("DOMContentLoaded", () => {
    const toggle = document.getElementById("theme-toggle");
    const root = document.documentElement;

    // Tema salvato in localStorage
    const darkMode = localStorage.getItem("theme") === "dark";
    root.classList.toggle("dark", darkMode);
    if (toggle) toggle.innerHTML = darkMode ? '🌞' : '🌙';

    // Toggle tema
    if (toggle) {
        toggle.addEventListener("click", () => {
            const isDark = root.classList.toggle("dark");
            localStorage.setItem("theme", isDark ? "dark" : "light");
            toggle.innerHTML = isDark ? '🌞' : '🌙';
        });
    }

    // Filtro dinamico
    const cards = document.querySelectorAll(".appeal-card");
    const filterInput = document.getElementById("appealFilter");
    filterInput.addEventListener("input", () => {
        const query = filterInput.value.toLowerCase();
        cards.forEach(card => {
            const text = card.dataset.search.toLowerCase();
            const match = text.includes(query);
            card.style.display = match ? "flex" : "none";
            card.style.opacity = match ? "1" : "0.4";
            card.style.transition = "opacity 0.3s ease";
        });
    });
});



