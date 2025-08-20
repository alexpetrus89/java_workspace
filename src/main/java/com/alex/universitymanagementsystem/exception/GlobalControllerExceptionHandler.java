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
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    // logger
    private static final Logger logger =
        LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    // constants
    private static final String EXCEPTION_VIEW = "exception/error";
    private static final String MESSAGE = "message";

    /**
     * Handles all uncaught exceptions and returns a ModelAndView with the error message.
     * @param e the exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        logger.error("Generic exception occurred", e);
        String message = "A generic exception occurred: " + e.getMessage();
        return new ModelAndView(EXCEPTION_VIEW, MESSAGE, message);
    }


    /**
     * Handles InternalServerError exceptions and returns a ModelAndView with a status of Internal Server Error (500).
     * The exception is logged at the ERROR level, and the response body contains a custom error message.
     * @param e the InternalServerError exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(InternalServerError.class)
    public ModelAndView handleInternalServerError(InternalServerError e) {
        logger.error("Internal server error", e);
        String message = "Internal server error: " + e.getMessage();
        return new ModelAndView(EXCEPTION_VIEW, MESSAGE, message);
    }


    /**
     * Handles exceptions that occur when a resource is not found, such as a 404 Not Found response.
     * Returns a ModelAndView object with a view name and a custom error message.
     * The exception is logged at the ERROR level.
     * @param e the exception to be handled
     * @return a ModelAndView object
     */
    @ExceptionHandler(value = {NotFoundException.class, NoResourceFoundException.class})
    public ModelAndView handleNoResourceFoundException(Exception e) {
        logger.error("Resource not found", e);
        String message = "Resource not found: " + e.getMessage();
        return new ModelAndView("exception/not_found/error-not-found", MESSAGE, message);
    }


    /**
     * Handles exceptions that occur when parsing a .html file, such as ParseErrors, SAXExceptions, and IOExceptions.
     * Returns a ModelAndView with a status of Internal Server Error (500) and a body containing a custom error message.
     * The exception is logged at the ERROR level.
     * @param e the exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(ParseException.class)
    public ModelAndView handleHtmlParseException(Exception e) {
        logger.error("Error during HTML parsing", e);
        String message = "Error during HTML parsing, HAI FATTO QUALCHE ERRORE IN UN FILE .HTML: " + e.getMessage();
        return new ModelAndView(EXCEPTION_VIEW, MESSAGE, message);
    }


    /**
     * Handles AccessDeniedException exceptions and returns a ModelAndView with a status of Forbidden (403).
     * The exception is logged at the ERROR level, and the response body contains a custom error message.
     * This exception is thrown when a user tries to access a resource they do not have permission to access.
     * @param e the AccessDeniedException exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException e) {
        logger.error("Access denied", e);
        String message = "Access denied, NON HAI I PERMESSI: " + e.getMessage();
        return new ModelAndView("exception/access_denied/access-denied", MESSAGE, message);
    }


    /**
     * Handles NoHandlerFoundException exceptions and returns a ModelAndView with a status of Not Found (404).
     * The exception is logged at the ERROR level, and the response body contains a custom error message.
     * This exception is thrown when a request is made to a URL that does not have a handler method.
     * This can occur when a static resource (such as a .html file) is not found, as Spring MVC will not find a handler for the request.
     * @param e the NoHandlerFoundException exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNoHandlerFoundException(NoHandlerFoundException e) {
        logger.error("No handler found for request", e);
        String message = "No controller/handler found for request: " + e.getMessage();
        return new ModelAndView(EXCEPTION_VIEW, MESSAGE, message);
    }








    // Add more @ExceptionHandler methods for other specific exceptions

    /**
     * Handles exceptions that occur when a username is invalid or already taken.
     * Returns a ModelAndView with a view name of "error/error" and a model containing the error message.
     * The exception is logged at the ERROR level.
     * @param e the exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(UsernameInvalidException.class)
    public ModelAndView handleUsernameInvalidException(UsernameInvalidException e, HttpServletRequest request) {
        logger.error("Username is invalid", e);
        String message = "Username is invalid: " + e.getMessage();
        return new ModelAndView("/exception/illegal/registration/invalid-username", MESSAGE, message);
    }


    /**
     * Handles exceptions that occur when Assertion error is throw.
     * Returns a ModelAndView with a view name of "exception/assertion/assertion" and
     * a model containing the error message.
     * The exception is logged at the ERROR level.
     */
    @ExceptionHandler(AssertionError.class)
    public ModelAndView handleAssertionError(AssertionError e) {
        logger.error("Assertion error", e);
        String message = "An assertion error occurred: " + e.getMessage();
        return new ModelAndView(EXCEPTION_VIEW, MESSAGE, message);
    }


    /**
     * Handles exceptions that occur when a method argument is not valid.
     * Returns a ModelAndView with a view name of "exception/illegal/illegal-parameter" and
     * a model containing the error message.
     * The exception is logged at the ERROR level.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public ModelAndView handleValidationException(Exception e) {
        logger.error("Validation error", e);
        String message = "An validation error occurred: " + getErrorMessage(e);
        return new ModelAndView("exception/illegal/illegal-parameter", MESSAGE, message);
    }


    /**
     * Handles exceptions that occur when a servlet request parameter is missing.
     * Returns a ModelAndView with a view of "exception/illegal/illegal-parameter" and
     * a model containing the error message.
     * The exception is logged at the ERROR level.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handlerMissingServletRequestParameter(MissingServletRequestParameterException e) {
        logger.error("Missing servlet request parameter", e);
        String message = "Parameter cannot be null: " + e.getMessage();
        return new ModelAndView("exception/illegal/illegal-parameter", MESSAGE, message);
    }


    /**
     * Handles IllegalArgumentException exceptions and returns a ModelAndView with a view name of "exception/illegal/illegal-parameters".
     * The exception is logged at the ERROR level, and the response body contains the error message.
     * @param e the IllegalArgumentException exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("Illegal argument", e);
        if (e.getMessage() != null && e.getMessage().contains("fiscal code"))
            return new ModelAndView("exception/illegal/registration/invalid-fiscal-code");

        // fallback for others IllegalArgumentException
        return new ModelAndView("exception/illegal/illegal-parameters", MESSAGE, e.getMessage());
    }



    /**
     * Extracts the error message from the given exception.
     * @param e the exception from which to extract the error message
     * @return the extracted error message
     */
    private String getErrorMessage(Exception e) {
        return switch (e) {
            case MethodArgumentNotValidException ex -> {
                BindingResult bindingResult = ex.getBindingResult();
                yield bindingResult != null ? bindingResult.getAllErrors().get(0).getDefaultMessage() : "An error occurred";
            }
            case BindException ex -> ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
            default -> "An error occurred";
        };
    }

}
