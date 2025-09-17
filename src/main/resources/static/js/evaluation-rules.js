function showHideFields(checkbox) {
    const fields = document.getElementById("fields");
    fields.style.display = checkbox.checked ? "block" : "none";
}

function checkGrade() {
    const grade = document.getElementsByName("grade")[0].value;
    if (grade == 30) {
        document.getElementById("withHonors").style.display = "block";
        document.getElementsByName("withHonors")[0].style.display = "block";
    } else {
        document.getElementById("withHonors").style.display = "none";
        document.getElementsByName("withHonors")[0].style.display = "none";
    }
}