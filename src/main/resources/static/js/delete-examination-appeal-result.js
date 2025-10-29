document.addEventListener('DOMContentLoaded', () => {
    const toggle = document.getElementById("theme-toggle");
    const root = document.documentElement;

    if (toggle) {
        const darkMode = localStorage.getItem("theme") === "dark";
        root.classList.toggle("dark", darkMode);
        toggle.innerHTML = darkMode ? '🌞' : '🌙';

        toggle.addEventListener("click", () => {
            const isDark = root.classList.toggle("dark");
            localStorage.setItem("theme", isDark ? "dark" : "light");
            toggle.innerHTML = isDark ? '🌞' : '🌙';
        });
    }

    // Animazione "success"
    const result = document.querySelector("h2");
    if (result) {
        result.classList.add("success-glow");
        setTimeout(() => result.classList.remove("success-glow"), 1500);
    }
});
