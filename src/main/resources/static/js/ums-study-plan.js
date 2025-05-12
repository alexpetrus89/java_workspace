import { COURSES_TOKEN } from './config.js';

// Quando avviene l'evento "pagina pronta"
// chiama la funzione di aggiornamento della lista di corsi
$(document).ready(function() {
    updateCourses();
});

// Quando avviene l'evento "selezione degree course"
// chiama la funzione di aggiornamento della lista di corsi
$('#degreeCourseOfNewCourse').on('change', function() {
    updateCourses();
});


// Nascondi il campo degreeCourseOfOldCourse
$('#degreeCourseOfOldCourse').hide();


// Funzione per aggiornare i corsi disponibili in base al corso di laurea selezionato
// e per gestire la richiesta AJAX.
function updateCourses() {
    const degreeCourseName = $('#degreeCourseOfNewCourse').val();

    $.ajax({
        type: 'GET',
        url: '/api/v1/degree-course/courses/ajax?name=' + degreeCourseName,
        headers: {
            'Authorization': 'Bearer ' + COURSES_TOKEN,
        },
        dataType: 'json',

        // Funzione eseguita in caso di successo della richiesta AJAX.
        success: function(data) {
            try {
                const jsonData = data;
                console.log(jsonData);
                // Assicurati che jsonData sia un oggetto JSON con una proprietà "degreeCourseName"
                if (!jsonData.degreeCourseName)
                    throw new Error('La risposta non è un oggetto JSON valido.');
                // Svuota la select
                $('#courseToAdd').empty();
                // Aggiungi un'opzione vuota all'inizio
                $('#courseToAdd').append('<option value="">Select a course</option>');
                // popola l'elemento select con la lista dei corsi
                $.each(jsonData.degreeCourseName, function(index, course) {
                    // Qui puoi accedere alle proprietà del corso, ad esempio:
                    const courseName = course.course.name;
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



