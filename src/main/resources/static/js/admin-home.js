document.addEventListener('DOMContentLoaded', () => {
    const cards = document.querySelectorAll('.card');
    cards.forEach((card, index) => {
        card.style.animation = `fadeSlideUp 0.6s forwards`;
        card.style.animationDelay = `${index * 100}ms`; // effetto a cascata
    });
});
