function normalizeText(s) {
    return (s || "")
        .toString()
        .trim()
        .toLowerCase()
        // rimuove accenti per rendere la ricerca più permissiva
        .normalize('NFD').replace(/[\u0300-\u036f]/g, '');
}

function filterCourses() {
    const searchInput = document.getElementById("searchInput");
    const table = document.getElementById("coursesTable");

    if (!searchInput) {
        console.warn("filterCourses: #searchInput not found");
        return;
    }
    if (!table) {
        console.warn("filterCourses: #coursesTable not found");
        return;
    }

    const tbody = table.querySelector("tbody");
    if (!tbody) {
        console.warn("filterCourses: tbody not found into table");
        return;
    }

    const rows = Array.from(tbody.rows); // solo le righe del corpo (esclude <thead>)

    const performFilter = () => {
        const q = normalizeText(searchInput.value);
        let visibleCount = 0;

        rows.forEach(row => {
            const rowText = normalizeText(row.innerText);
            const visible = q === "" || rowText.includes(q);
            row.style.display = visible ? "" : "none";
            if (visible) visibleCount++;
        });

        // opzionale: mostra un messaggio "no results" se visibleCount === 0
        // (implementazione lasciata a piacere)
    };

    // Inizializza il filtro (in caso ci sia un valore già presente)
    performFilter();

    // Usa 'input' per catturare anche incolla e input mobile
    searchInput.addEventListener("input", performFilter);
}

// Esegui solo dopo che il DOM è pronto
document.addEventListener("DOMContentLoaded", filterCourses);