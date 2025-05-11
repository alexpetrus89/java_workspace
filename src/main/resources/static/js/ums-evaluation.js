function showHideFields(checkbox) {
    const fields = document.getElementById("fields");
    if (checkbox.checked) {
        fields.style.display = "block";
    } else {
        fields.style.display = "none";
    }
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