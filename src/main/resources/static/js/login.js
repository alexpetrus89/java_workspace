/**
 * ======================================================
 * 🧠 Uni Journey - Login Page Script
 * Handles dynamic greeting, dark/light theme toggle, and toast notifications.
 * Linked sections: #greeting, .theme-toggle, #toast-container
 * ======================================================
 */

function createContainer() {
    const c = document.createElement('div');
    c.id = 'toast-container';
    document.body.appendChild(c);
    return c;
}

/*  ==========================
    💬 TOAST NOTIFICATIONS
    ========================== */
function showToast(msg, duration = 2200) {
    const container = document.getElementById('toast-container') || createContainer();
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.textContent = msg;
    container.appendChild(toast);

    // Animate out after duration
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateY(10px)';
        setTimeout(() => toast.remove(), 400);
    }, duration);
}

function initTheme(root) {
    /*  ==========================
        🌗 THEME TOGGLE
        ========================== */
    const toggle = document.getElementById('theme-toggle');
    if (!toggle) return;

    // Apply stored theme on load
    if (localStorage.getItem('theme') === 'dark') {
        root.classList.add('dark');
        toggle.innerHTML = '<i class="fas fa-sun"></i>';
    } else {
        root.classList.remove('dark');
        toggle.innerHTML = '<i class="fas fa-moon"></i>';
    }

    // Toggle theme on click
    toggle.addEventListener('click', () => {
        root.classList.toggle('dark');
        const dark = root.classList.contains('dark');
        toggle.innerHTML = dark ? '<i class="fas fa-sun"></i>' : '<i class="fas fa-moon"></i>';
        localStorage.setItem('theme', dark ? 'dark' : 'light');
    });
}

function initGreeting() {
    /*  ==========================
        🌞 DYNAMIC GREETING
        ========================== */
    const greetingEl = document.getElementById('greeting');
    if (!greetingEl) return;

    const now = new Date();
    const hour = now.getHours();
    let greeting;

    if (hour >= 6 && hour < 12) greeting = "Good morning";
    else if (hour >= 12 && hour < 18) greeting = "Good afternoon";
    else greeting = "Good evening";

    greetingEl.textContent = `${greeting}! Login to your account.`;
    greetingEl.classList.add('visible');
}

function initToastsFromDOM() {
    /*  ==========================
        💬 DISPLAY LOGIN MESSAGES AS TOASTS
        ========================== */
    // Logout message
    const logoutMsg = document.querySelector('.alert.success');
    if (logoutMsg && logoutMsg.textContent.trim() !== '') {
        showToast(logoutMsg.textContent.trim(), 3000);
    }

    // Error messages
    const errorMsgs = document.querySelectorAll('.alert.error');
    for (const msgEl of errorMsgs) {
        if (msgEl.textContent.trim() !== '') showToast(msgEl.textContent.trim(), 3000);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const root = document.documentElement;
    initTheme(root);
    initGreeting();
    initToastsFromDOM();
});

