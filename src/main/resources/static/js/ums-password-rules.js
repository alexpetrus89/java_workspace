document.addEventListener('DOMContentLoaded', () => {
    const passwordInput = document.getElementById('password');
    if (!passwordInput) return; // sicurezza

    const rules = {
        length: document.getElementById('rule-length'),
        uppercase: document.getElementById('rule-uppercase'),
        lowercase: document.getElementById('rule-lowercase'),
        number: document.getElementById('rule-number'),
        special: document.getElementById('rule-special')
    };

    const commonPasswords = ["password", "12345678", "qwerty", "letmein", "admin"];

    passwordInput.addEventListener('input', () => {
        const val = passwordInput.value;

        rules.length.classList.toggle('valid', val.length >= 8);
        rules.uppercase.classList.toggle('valid', /[A-Z]/.test(val));
        rules.lowercase.classList.toggle('valid', /[a-z]/.test(val));
        rules.number.classList.toggle('valid', /\d/.test(val));
        rules.special.classList.toggle('valid', /[!@#$%^&*()]/.test(val));

        // opzione: messaggio aggiuntivo per password comune
        if (commonPasswords.includes(val.toLowerCase())) {
            rules.length.textContent = "This password is too common!";
            rules.length.classList.add('valid'); // evidenzia in verde per distinguere
        } else {
            rules.length.textContent = "At least 8 characters";
        }
    });
});

