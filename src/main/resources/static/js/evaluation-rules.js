// Mostra/nasconde campi grade e withHonors
function showHideFields() {
    const gradeInput = document.querySelector('input[name="grade"]');
    const honorsBlock = document.getElementById('withHonorsBlock');

    if (!gradeInput || !honorsBlock) return;

    const value = Number.parseInt(gradeInput.value);

    if (Number.isNaN(value)) {
        document.getElementById('fields').classList.add('hidden');
        honorsBlock.classList.add('hidden');
    } else {
        // Mostra il campo grade
        document.getElementById('fields').classList.remove('hidden');

        // Mostra withHonors solo se il voto è 30
        if (value === 30) {
            honorsBlock.classList.remove('hidden');
            honorsBlock.classList.add('fade-in');
        } else {
            honorsBlock.classList.add('hidden');
        }
    }
}

// Blocca invio con Enter
function handleKeyPress(event) {
    if (event.key === "Enter") {
        event.preventDefault();
    }
}

// Controllo del voto (0-30) e gestione withHonors
function checkGrade() {
    const gradeInput = document.querySelector('input[name="grade"]');
    if (!gradeInput) return;

    const value = Number.parseInt(gradeInput.value);
    if (Number.isNaN(value) || value < 0 || value > 30) {
        gradeInput.style.borderColor = "red";
    } else {
        gradeInput.style.borderColor = "";
    }

    // Aggiorna visibilità withHonors
    showHideFields();
}

// Collega eventi
document.addEventListener('DOMContentLoaded', () => {
    const gradeInput = document.querySelector('input[name="grade"]');
    if (gradeInput) {
        gradeInput.addEventListener('input', checkGrade);
        gradeInput.addEventListener('keypress', handleKeyPress);
    }
});
