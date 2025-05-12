import { APPEALS_TOKEN } from './config.js';

// Quando avviene l'evento "pagina pronta"
// chiama la funzione di aggiornamento della lista di appelli
$(document).ready(function() {
    getExaminationAppeals();
});



// funzione per recuperare la lista degli appelli di esame
function getExaminationAppeals() {
    $.ajax({
        type: "GET",
        url: "/api/v1/examination-appeal/view/professor",
        headers: {
            'Authorization': 'Bearer ' + APPEALS_TOKEN,
        },
        dataType: 'json',

        // success
        success: function(data) {
            try {
                const jsonData = data;
                console.log(jsonData);
                // Assicurati che jsonData sia un oggetto JSON con una proprietà "examAppeals"
                if (!jsonData.examAppeals)
                    throw new Error('La risposta non è un oggetto JSON valido.');
                // Svuota la select
                $('#examAppeals').empty();
                // Aggiungi un'opzione vuota all'inizio
                $('#examAppeals').append('<option value="">Select an exam appeal</option>');
                // popola l'elemento select con la lista degli appelli di esame
                $.each(jsonData.examAppeals, function(index, appeal) {
                    // Qui puoi accedere alle proprietà dell'appello, ad esempio:
                    const courseName = appeal.course.name;
                    const date = appeal.date;
                    $('#appealToDelete').append('<option value="' + courseName + '">' + courseName + ' - ' + date + '</option>');
                });
            } catch (e) {
                console.error('Errore durante il parsing della risposta JSON:', e);
            }
        },

        // error
        error: function(xhr, status, error) {
            console.log("Errore: " + error);
        }
    });
}

// chiama la funzione per recuperare la lista dei corsi di laurea
getExaminationAppeals();