import { COURSES_TOKEN } from './config.js';

// When the "page ready" event occurs
// calls the course list update function
$(document).ready(function() {
    updateCourses();
});


// When the "degree course selection" event occurs
// calls the course list update function
$('#degreeCourseOfNewCourse').on('change', function() {
    updateCourses();
});


// When the "select degree course" event occurs, this function
// updates the available courses based on the selected degree program
// retrieves them via AJAX request.
function updateCourses() {
    const degreeCourseName = $('#degreeCourseOfNewCourse').val();

    $.ajax({
        type: 'GET',
        url: '/api/v1/degree-course/read/courses/ajax?name=' + degreeCourseName,
        headers: {
            'Authorization': 'Bearer ' + COURSES_TOKEN,
        },
        dataType: 'json',

        // Function executed in case of success of the AJAX request.
        // response is the data returned by the server
        success: function(data) {
            try {
                const jsonData = data;
                console.log(jsonData);

                // Make sure jsonData is a JSON object with a "degreeCourseName" property
                if (!jsonData.degreeCourseName)
                    throw new Error('Response is not a valid JSON object.');

                // Remove the existing options from the select element
                $('#courseToAdd').empty();

                // Add an empty option at the beginning
                $('#courseToAdd').append('<option value="">Select a course</option>');

                // Populate the select element with the list of courses
                $.each(jsonData.degreeCourseName, function(index, course) {
                    const courseName = course.course.name;
                    // Add the course name as an option to the select element
                    $('#courseToAdd').append('<option value="' + courseName + '">' + courseName + '</option>');
                });
            } catch (e) {
                console.error('Error parsing JSON response:', e);
            }
        },
        // Function executed in case of error of the AJAX request
        // xhr is the XMLHttpRequest object that contains the error details
        // status and error are the details of the error
        error: function(xhr, status, error) {
            console.log("Error: " + error + " - Status: " + status + " - Response: " + xhr.responseText);
        }
    });
    // end of ajax request
}


// Hide the degreeCourseOfOldCourse field
$('#degreeCourseOfOldCourse').hide();



