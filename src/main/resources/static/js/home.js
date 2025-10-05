/**
 * ======================================================
 * 🧠 Uni Journey - Home Page Script
 * Handles theme toggle, greeting toast, and card animations.
 * Linked sections: .theme-toggle, #toast-container, .card
 * ======================================================
 */
document.addEventListener('DOMContentLoaded', () => {
    const toggle = document.getElementById('theme-toggle');
    const root = document.documentElement;

    // 🌗 Apply stored theme on load
    if (localStorage.getItem('theme') === 'dark') {
        root.classList.add('dark');
        toggle.innerHTML = '<i class="fas fa-sun"></i>';
    } else {
        root.classList.remove('dark');
        toggle.innerHTML = '<i class="fas fa-moon"></i>';
    }

    // 🌞 Toggle theme on click
    toggle.addEventListener('click', () => {
        root.classList.toggle('dark');
        const dark = root.classList.contains('dark');
        toggle.innerHTML = dark ? '<i class="fas fa-sun"></i>' : '<i class="fas fa-moon"></i>';
        localStorage.setItem('theme', dark ? 'dark' : 'light');
        showToast(dark ? '🌙 Dark mode on' : '☀️ Light mode on');
    });

     // 👋 Welcome toast for logged-in user
    const user = document.querySelector('[sec\\:authentication]');
    if (user && user.textContent.trim() !== '')
        setTimeout(() => showToast(`Welcome back, ${user.textContent.trim()}! 👋`), 800);

    // 🃏 Animate cards on scroll
    const cards = document.querySelectorAll('.card');
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(e => {
            if (e.isIntersecting) {
                e.target.classList.add('visible');
                e.target.style.transitionDelay = `${Math.random()*0.3}s`;
            }
        });
    }, { threshold: 0.3 });
    cards.forEach(card => observer.observe(card));
});



function showToast(msg, duration = 2200) {
    const container = document.getElementById('toast-container') || createContainer();
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.textContent = msg;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateY(10px)';
        setTimeout(() => toast.remove(), 400);
    }, duration);
}

function createContainer() {
    const c = document.createElement('div');
    c.id = 'toast-container';
    document.body.appendChild(c);
    return c;
}


// ===== FOOTER ANIMATION =====
document.addEventListener('DOMContentLoaded', () => {
    const footer = document.querySelector('.animated-footer');
    if (!footer) return;

    const observer = new IntersectionObserver(entries => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                footer.classList.add('visible');
                observer.disconnect(); // L'animazione avviene solo una volta
            }
        });
    }, { threshold: 0.2 });

    observer.observe(footer);
});





