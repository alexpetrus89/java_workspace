// Update hidden input fields when course is selected
document.addEventListener('DOMContentLoaded', function () {
    // ---------------------------
    // Gestione selezione corso
    // ---------------------------
    const select = document.getElementById('courseName');
    if (select) {
        select.addEventListener('change', function () {
            const selected = this.options[this.selectedIndex];
            document.getElementById('degreeCourseName').value = selected.dataset.degree;
            document.getElementById('courseCfu').value = selected.dataset.cfu;
        });
    }

    // ---------------------------
    // Gestione tema (dark / light)
    // ---------------------------
    const toggle = document.getElementById("theme-toggle");
    const root = document.documentElement;

    if (toggle) {
        // Imposta tema salvato
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
