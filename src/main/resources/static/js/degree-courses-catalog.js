document.addEventListener('DOMContentLoaded', () => {
    const toggle = document.getElementById('theme-toggle');
    const root = document.body;

    // Gestione tema dark/light
    if (toggle) {
        const darkMode = localStorage.getItem('theme') === 'dark';
        root.classList.toggle('dark', darkMode);
        toggle.innerHTML = darkMode ? '🌞' : '🌙';

        toggle.addEventListener('click', () => {
            const isDark = root.classList.toggle('dark');
            localStorage.setItem('theme', isDark ? 'dark' : 'light');
            toggle.innerHTML = isDark ? '🌞' : '🌙';
        });
    }

    // Espansione del blocco (solo segnaposto, DTO non ha courses)
    const viewBtns = document.querySelectorAll('.view-btn');
    for (const btn of viewBtns) {
        btn.addEventListener('click', () => {
            const coursesList = btn.nextElementSibling;
            coursesList.classList.toggle('hidden');
            btn.innerHTML = coursesList.classList.contains('hidden')
                ? '<i class="fas fa-eye"></i> View study plan'
                : '<i class="fas fa-eye-slash"></i> Hide study plan';
        });
    }
});

