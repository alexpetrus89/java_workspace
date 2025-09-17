document.getElementById("fiscalCodeForm").addEventListener("submit", function (event) {
    const input = document.getElementById("fiscalCode");
    const error = document.getElementById("fiscalCodeError");
    const value = input.value.trim();

    const regex = /^[A-Z0-9]{16}$/i;
    if (!regex.test(value)) {
        event.preventDefault();
        error.textContent = "The fiscal code must contain exactly 16 alphanumeric characters.";
    } else {
        error.textContent = "";
    }
});