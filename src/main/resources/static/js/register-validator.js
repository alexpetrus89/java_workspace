document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("registerForm");
    const input = document.getElementById("register");
    const error = document.getElementById("registerError");

    form.addEventListener("submit", function (event) {
        const value = input.value.trim();
        const regex = /^\d{6}$/;

        if (!regex.test(value)) {
            event.preventDefault();
            error.textContent = "The register must contain at least 6 digits.";
        } else {
            error.textContent = "";
        }
    });
});