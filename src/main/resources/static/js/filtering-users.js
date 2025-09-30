function filterUsers() {
    const input = document.getElementById("userSearch").value.toLowerCase();

    // Filter desktop table
    const table = document.getElementById("userTable");
    const tr = table.getElementsByTagName("tr");
    for (let i = 1; i < tr.length; i++) { // skip header
        let td = tr[i].getElementsByTagName("td");
        let match = false;
        for (const element of td) {
            if (element.textContent.toLowerCase().includes(input)) {
                match = true;
                break;
            }
        }
        tr[i].style.display = match ? "" : "none";
    }

    // Filter mobile cards
    const cards = document.getElementById("mobileCards").getElementsByClassName("card");
    for (let card of cards) {
        let text = card.textContent.toLowerCase();
        card.style.display = text.includes(input) ? "" : "none";
    }
}