document.addEventListener("DOMContentLoaded", () => {
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
    });
});