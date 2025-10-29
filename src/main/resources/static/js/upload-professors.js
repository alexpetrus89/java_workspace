// call the function to retrieve the list of professors
getProfessors()


// function to retrieve the list of professors
function getProfessors() {
    $.ajax({
        type: "GET",
        url: "/api/v1/professor/read/all/ajax",
        dataType: "json",

        // success
        success: function(data) {
            // populates the select element with the list of degree courses
            const select = document.getElementById("professor");
            select.innerHTML = "";
            for (const professor of data) {
                const option = document.createElement("option");
                option.value = professor.professorCode;
                option.text = professor.fullName;
                select.appendChild(option);
            };
        },

        // error
        error: function(xhr, status, error) {
            console.log("Error: " + error + " - Status: " + status + " - Response: " + xhr.responseText);
        }
    });
}

