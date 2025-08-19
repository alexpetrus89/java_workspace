document.addEventListener('DOMContentLoaded', function () {
    const select = document.getElementById('courseName');
    select.addEventListener('change', function () {
        const selected = this.options[this.selectedIndex];
        document.getElementById('degreeCourseName').value = selected.getAttribute('data-degree');
    });
});
