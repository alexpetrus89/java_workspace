let index = 0; // initialize index for animation delay
document.addEventListener('DOMContentLoaded', () => {
    const cards = document.querySelectorAll('.card');
    for (const card of cards) {
        card.style.animation = `fadeSlideUp 0.6s forwards`;
        card.style.animationDelay = `${index * 100}ms`; // effetto a cascata
        index++; // increment the index
    }
});
