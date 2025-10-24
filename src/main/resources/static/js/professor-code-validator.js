document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("professorCodeForm");
    const input = document.getElementById("professorCode");
    const error = document.getElementById("professorCodeError");

    form.addEventListener("submit", function (event) {
        const value = input.value.trim();
        const regex = /^[A-Za-z0-9]{8,}$/;

        if (regex.test(value) === false) {
            event.preventDefault();
            error.textContent = "The professor code must contain at least 8 alphanumeric characters.";
        } else {
            error.textContent = "";
        }
    });
});
