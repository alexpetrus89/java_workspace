function filterCourses() {

    const searchInput = document.getElementById("searchInput");

    // Filter desktop table
    const table = document.getElementById("coursesTable");
    const rows = table.getElementsByTagName("tr");

    searchInput.addEventListener("keyup", function () {
        const filter = this.value.toLowerCase();

        for (let i = 1; i < rows.length; i++) {
            const rowText = rows[i].innerText.toLowerCase();
            rows[i].style.display = rowText.includes(filter) ? "" : "none";
        }
    });
}