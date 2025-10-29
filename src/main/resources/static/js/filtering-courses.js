function filterCourses() {
    const searchInput = document.getElementById("searchInput");
    const table = document.getElementById("coursesTable");
    const rows = table.querySelectorAll("tbody tr"); // solo tbody

    searchInput.addEventListener("keyup", function () {
        const filter = this.value.toLowerCase();
        for (const row of rows) {
            const rowText = row.innerText.toLowerCase();
            row.style.display = rowText.includes(filter) ? "" : "none";
        }
    });
}

document.addEventListener("DOMContentLoaded", filterCourses);
