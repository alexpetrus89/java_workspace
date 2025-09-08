// call the function to retrieve the list of degree courses
getDegreeCourses();


// function to retrieve the list of degree courses
function getDegreeCourses() {
    $.ajax({
        type: "GET",
        url: "/api/v1/read/degree-course/ajax",
        dataType: "json",

        // success
        success: function(data) {
            // populates the select element with the list of degree courses
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
