// funzione per recuperare la lista degli appelli di esame
function getExaminationAppeals() {
    $.ajax({
        type: "GET",
        url: "/api/v1/examination-appeal/view/professor",
        dataType: "json",

        // success
        success: function(data) {
            // popola l'elemento select con la lista degli appelli di esame
            const select = document.getElementById("examAppeals");
            select.innerHTML = "";
            data.forEach(function(examAppeals) {
                const option = document.createElement("option");
                option.value = examAppeals.course.name;
                option.text = examAppeals.course.name;
                option.text = examAppeals.date;
                select.appendChild(option);
            });
        },

        // error
        error: function(xhr, status, error) {
            console.log("Errore: " + error);
        }
    });
}

// chiama la funzione per recuperare la lista dei corsi di laurea
getExaminationAppeals();