// Filtering function for user selection page

document.addEventListener("DOMContentLoaded", () => {
    const input = document.getElementById("userSearch");
    const cards = document.querySelectorAll("#cardsGrid .card");

    input.addEventListener("input", () => {
        const filter = input.value.toLowerCase().trim();

        for (const card of cards) {
            const text = card.textContent.toLowerCase();
            const isVisible = text.includes(filter);
            card.style.display = isVisible ? "" : "none";

            const ps = card.querySelectorAll("p");
            for (const p of ps) {
                const originalText = p.textContent;

                if (filter && isVisible) {
                    const regex = new RegExp(`(${filter})`, "gi");
                    // Wrap match in <mark class="highlight">
                    p.innerHTML = originalText.replace(regex, '<mark class="highlight">$1</mark>');
                } else {
                    p.textContent = originalText;
                }
            }
        }
    });
});

