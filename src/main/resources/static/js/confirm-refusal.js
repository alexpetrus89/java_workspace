document.addEventListener("DOMContentLoaded", () => {
    const toggle = document.getElementById("theme-toggle");
    const root = document.documentElement;

    // Applica il tema salvato in localStorage
    const darkMode = localStorage.getItem("theme") === "dark";
    root.classList.toggle("dark", darkMode);
    if (toggle) toggle.innerHTML = darkMode ? '🌞' : '🌙';

    // Toggle al click
    if (toggle) {
        toggle.addEventListener("click", () => {
            const isDark = root.classList.toggle("dark");
            localStorage.setItem("theme", isDark ? "dark" : "light");
            toggle.innerHTML = isDark ? '🌞' : '🌙';
        });
    }
});
