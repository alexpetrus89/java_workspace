document.addEventListener('DOMContentLoaded', () => {
    const toggle = document.getElementById("theme-toggle");
    const root = document.documentElement;

    if (toggle) {
        // Applica tema salvato
        const darkMode = localStorage.getItem("theme") === "dark";
        root.classList.toggle("dark", darkMode);
        toggle.innerHTML = darkMode ? '🌞' : '🌙';

        // Toggle manuale
        toggle.addEventListener("click", () => {
            const isDark = root.classList.toggle("dark");
            localStorage.setItem("theme", isDark ? "dark" : "light");
            toggle.innerHTML = isDark ? '🌞' : '🌙';
        });
    }
});
