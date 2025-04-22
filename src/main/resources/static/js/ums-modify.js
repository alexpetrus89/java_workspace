import { token } from './config.js';

// Funzione per aggiornare i corsi quando la pagina è pronta
// e quando il valore del select cambia.
$(document).ready(function() {
    updateCourses();
});

// Funzione per aggiornare i corsi quando il valore del select cambia.
// chiama la funzione updateCourses per aggiornare i corsi disponibili
$('#degreeCourseOfNewCourse').on('change', function() {
    updateCourses();
});


// Nascondi il campo degreeCourseOfOldCourse
$('#degreeCourseOfOldCourse').hide();


// Funzione per aggiornare i corsi disponibili in base al corso di laurea selezionato
// e per gestire la richiesta AJAX.
function updateCourses() {
    var degreeCourseName = $('#degreeCourseOfNewCourse').val();

    $.ajax({
        type: 'GET',
        url: '/api/v1/degree-course/courses/ajax?name=' + degreeCourseName,
        headers: {
            'Authorization': 'Bearer ' + token,
        },
        dataType: 'json',

        // Funzione eseguita in caso di successo della richiesta AJAX.
        success: function(data) {
            try {
                var jsonData = data;
                console.log(jsonData);
                // Assicurati che jsonData sia un oggetto JSON con una proprietà "degreeCourseName"
                if (!jsonData.degreeCourseName)
                    throw new Error('La risposta non è un oggetto JSON valido.');

                // Svuota la select
                $('#courseToAdd').empty();

                // Aggiungi un'opzione vuota all'inizio
                $('#courseToAdd').append('<option value="">Select a course</option>');
                $.each(jsonData.degreeCourseName, function(index, course) {
                    // Qui puoi accedere alle proprietà del corso, ad esempio:
                    var courseName = course.course.name;
                    // Aggiungi l'opzione al select
                    $('#courseToAdd').append('<option value="' + courseName + '">' + courseName + '</option>');
                });
            } catch (e) {
                console.error('Errore durante il parsing della risposta JSON:', e);
            }
        },
        // Funzione eseguita in caso di errore della richiesta AJAX.
        error: function(xhr, status, error) {
            console.error('Errore nella richiesta AJAX:', error);
        }
    });
    // end of ajax request
}



