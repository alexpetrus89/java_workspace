document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("registerForm");
    const input = document.getElementById("register");
    const error = document.getElementById("registerError");

    if (!form || !input || !error) return; // security

    form.addEventListener("submit", function(event) {
        const value = input.value.trim();
        const regex = /^\d{6}$/;

        if (!regex.test(value)) {
            event.preventDefault();
            error.textContent = "The register must contain at least 6 digits.";
            error.classList.add("show");
        } else {
            error.textContent = "";
            error.classList.remove("show");
        }
    });

    // Rimuove l'errore mentre l'utente digita
    input.addEventListener("input", function() {
        if (error.classList.contains("show")) {
            error.classList.remove("show");
        }
    });

});