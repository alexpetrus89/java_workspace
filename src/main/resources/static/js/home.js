/**
 * ======================================================
 * 🧠 Uni Journey - Home Page Script
 * Handles theme toggle, greeting toast, and card animations.
 * Linked sections: .theme-toggle, #toast-container, .card
 * ======================================================
 */
import { initThemeToggle, showToast } from './theme-manager.js';

document.addEventListener("DOMContentLoaded", () => {
    initThemeToggle();

    // 👋 Welcome toast for logged-in user
    const user = document.querySelector(String.raw`[sec\:authentication]`);
    if (user && user.textContent.trim() !== '') {
        setTimeout(() => showToast(`Welcome back, ${user.textContent.trim()}! 👋`), 800);
    }

    // 🃏 Animate cards on scroll
    const cards = document.querySelectorAll('.card');
    const observer = new IntersectionObserver((entries) => {
        for (const entry of entries) {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
                entry.target.style.transitionDelay = `${Math.random() * 0.3}s`;
                observer.unobserve(entry.target);
            }
        }
    }, { threshold: 0.3 });
    for (const card of cards) observer.observe(card);
});






