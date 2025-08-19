// funzione per recuperare la lista dei corsi di laurea
function getDegreeCourses() {
    $.ajax({
        type: "GET",
        url: "/api/v1/degree-course/ajax",
        dataType: "json",

        // success
        success: function(data) {
            // popola l'elemento select con la lista dei corsi di laurea
            const select = document.getElementById("degreeCourse");
            select.innerHTML = "";
            data.forEach(function(degreeCourse) {
                const option = document.createElement("option");
                option.value = degreeCourse.name;
                option.text = degreeCourse.name;
                select.appendChild(option);
            });
        },

        // error
        error: function(xhr, status, error) {
            console.log("Error: " + error + " - Status: " + status + " - Response: " + xhr.responseText);
        }
    });
}

// chiama la funzione per recuperare la lista dei corsi di laurea
getDegreeCourses();
