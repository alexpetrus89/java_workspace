document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("uniqueCodeForm");
    const input = document.getElementById("uniqueCode");
    const error = document.getElementById("uniqueCodeError");

    form.addEventListener("submit", function (event) {
        const value = input.value.trim();
        const regex = /^[A-Za-z0-9]{8,}$/;

        if (!regex.test(value)) {
            event.preventDefault();
            error.textContent = "The unique code must contain at least 8 alphanumeric characters.";
        } else {
            error.textContent = "";
        }
    });
});
