package com.alex.universitymanagementsystem.exception;


/**
 * Global Exception Handling
 *
 * Un controller advice consente di utilizzare esattamente le stesse tecniche
 * di gestione delle eccezioni, ma di applicarle all'intera applicazione, non
 * solo a un singolo controller.
 * È possibile considerarle come un intercettore guidato dall'annotazione.
 *
 * Qualsiasi classe annotata con @ControllerAdvice diventa un controller-advice
 * e sono supportati tre tipi di metodo:
 *
 * 1) Metodi di gestione delle eccezioni annotati con @ExceptionHandler.
 *
 * 2) Metodi di miglioramento del modello (per aggiungere dati aggiuntivi
 *    al modello) annotati con @ModelAttribute.
 *
 * Nota che questi attributi non sono disponibili per le viste di gestione
 * delle eccezioni.
 *
 * 3) Metodi di inizializzazione del binder (utilizzati per configurare la
 *    gestione dei form) annotati con @InitBinder.
 *
 * Ci occuperemo solo della gestione delle eccezioni: per maggiori informazioni
 * sui metodi @ControllerAdvice, consultate il manuale online.
 *
 *
 * Tutti i gestori di eccezioni che hai visto sopra possono essere definiti su
 * una classe controller-advice, ma ora si applicano alle eccezioni generate da
 * qualsiasi controller. Ecco un semplice esempio:
 */

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger logger =
        LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    // constant
    private static final String ERROR_URL = "/exception/error";

    /**
     * Handles all uncaught exceptions and returns a ResponseEntity with a status of Internal Server Error (500)
     * and a body containing the error message. The exception is logged at the ERROR level.
     * @param e the exception to be handled
     * @return a ResponseEntity containing the error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Handle the exception and generate a custom error response
        logger.error("An error occurred", e);
        String errorMessage = "A generic exception occurred: " + e.getMessage();

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorMessage);
    }

    /**
     * Handles RuntimeException exceptions and returns a ResponseEntity with a status of Internal Server Error (500)
     * and a body containing the error message. The exception is logged at the ERROR level.
     * @param e the RuntimeException exception to be handled
     * @return a ResponseEntity containing the error message
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        // Handle the exception and generate a custom error response
        logger.error("A runtime exception occurred", e);
        String errorMessage = "A runtime exception occurred: " + e.getMessage();

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorMessage);
    }


    /**
     * Handles exceptions that occur when a resource is not found, such as a 404 Not Found response.
     * Returns a ResponseEntity with a status of Not Found (404) and a body containing a custom error message.
     * The exception is logged at the ERROR level.
     * @param e the exception to be handled
     * @return a ResponseEntity containing the error message
     */
    @ExceptionHandler(value = {NotFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<String> handleNoResourceFoundException(NoResourceFoundException e) {
        // Handle the specific exception and generate a custom error response
        logger.error("Resource not found", e);
        String errorMessage = "Resource not found, NON TROVA LA RISORSA: " + e.getMessage();

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorMessage);
    }


    /**
     * Handles exceptions that occur when parsing a .html file, such as ParseErrors, SAXExceptions, and IOExceptions.
     * Returns a ResponseEntity with a status of Internal Server Error (500) and a body containing a custom error message.
     * The exception is logged at the ERROR level.
     * @param e the exception to be handled
     * @return a ResponseEntity containing the error message
     */
    @ExceptionHandler(ParseException.class)
    public ResponseEntity<String> handleHtmlParseException(Exception e) {
        // Handle the specific exception and generate a custom error response
        logger.error("Error during HTML parsing", e);
        String errorMessage = "Error during HTML parsing, HAI FATTO QUALCHE ERRORE IN UN FILE .HTML: " + e.getMessage();

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorMessage);
    }


    /**
     * Handles exceptions that occur when a null pointer, illegal argument, or unsupported operation is encountered.
     * Returns a ModelAndView with a view name of "error/error" and a model containing the error message.
     * The exception is logged at the ERROR level.
     * @param e
     * @return
     */
    /*
    @ExceptionHandler(NullPointerException.class)
    public ModelAndView handleCommonExceptions(Exception e) {
        // Handle the specific exception and generate a custom error response
        logger.error("Common exception, e.g. null pointer, illegal argument, unsupported operation", e);
        return new ModelAndView(ERROR_URL, "error message", e.getMessage());
    }*/


    @ExceptionHandler(value = AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException e) {
        // handle the specific exception and generate a custom error response
        logger.error("Access denied", e);
        return new ModelAndView(ERROR_URL, "error message", e.getMessage());

    }

    /**
     * Handles exceptions that occur when a username is invalid or already taken.
     * Returns a ModelAndView with a view name of "error/error" and a model containing the error message.
     * The exception is logged at the ERROR level.
     * @param e the exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(UsernameInvalidException.class)
    public ModelAndView handleUsernameInvalidException(UsernameInvalidException e, HttpServletRequest request) {
        // Handle the specific exception and generate a custom error response
        logger.error("Username is invalid", e);
        ModelAndView modelAndView = new ModelAndView("/exception/registration/invalid-username");
        modelAndView.addObject("title", "Error View");
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "Error");
        modelAndView.addObject("stackTrace", e.getStackTrace());
        return modelAndView;
    }










    /**
     * Handles NoHandlerFoundException exceptions and returns a ResponseEntity with a status of Not Found (404).
     * The exception is logged at the ERROR level, and the response body contains a custom error message.
     * This exception is thrown when a request is made to a URL that does not have a handler method.
     * This can occur when a static resource (such as a .html file) is not found, as Spring MVC will not find a handler for the request.
     * @param e the NoHandlerFoundException exception to be handled
     * @return a ResponseEntity containing the error message
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException e) {
        // Handle the specific exception and generate a custom error response
        logger.error("No handler found for request", e);
        String errorMessage = "No controller/handler found for request: " + e.getMessage();

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorMessage);
    }


    /**
     * Handles InternalServerError exceptions and returns a ResponseEntity with a status of Internal Server Error (500).
     * The exception is logged at the ERROR level, and the response body contains a custom error message.
     *
     * @param e the InternalServerError exception to be handled
     * @return a ResponseEntity containing the error message
     */
    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<String> handleInternalServerError(InternalServerError e) {
        // Handle the specific exception and generate a custom error response
        logger.error("Internal server error", e);
        String errorMessage = "Internal server error: " + e.getMessage();

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorMessage);
    }


    // Add more @ExceptionHandler methods for other specific exceptions

}
