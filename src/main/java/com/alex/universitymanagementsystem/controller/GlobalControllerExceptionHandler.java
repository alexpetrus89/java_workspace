package com.alex.universitymanagementsystem.controller;


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
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.mail.MailException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.alex.universitymanagementsystem.config.UmsConfig;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.DuplicateFiscalCodeException;
import com.alex.universitymanagementsystem.exception.DuplicateUsernameException;
import com.alex.universitymanagementsystem.exception.JsonProcessingException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    // logger
    private static final Logger logger =
        LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    // constants
    private static final String UNKNOWN_VALIDATION_ERROR = "Unknown validation error";
    private static final String MESSAGE = "message";

    @Value("#{genericExceptionUri}")
    private String genericExceptionUri;
    @Value("#{dataAccessExceptionUri}")
    private String dataAccessExceptionUri;
    @Value("#{accessDeniedExceptionUri}")
    private String accessDeniedExceptionUri;
    @Value("#{illegalArgumentExceptionUri}")
    private String illegalArgumentExceptionUri;
    @Value("#{notFoundExceptionUri}")
    private String notFoundExceptionUri;
    @Value("#{alreadyExistsExceptionUri}")
    private String alreadyExistsExceptionUri;
    @Value("#{jsonProcessingExceptionUri}")
    private String jsonProcessingExceptionUri;
    @Value("#{duplicateUsernameUri}")
    private String duplicateUsernameUri;
    @Value("#{duplicateFiscalCodeUri}")
    private String duplicateFiscalCodeUri;

    // instance variables
    private final UmsConfig umsConfig;

    public GlobalControllerExceptionHandler(UmsConfig umsConfig) {
        this.umsConfig = umsConfig;
    }

    /**
     * Handles all uncaught exceptions and returns a ModelAndView with the error message.
     * @param e the exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        Throwable root = getRootCause(e);

        logger.error("Exception caught: {} | Root cause: {}",
            e.getClass().getName(), root.getClass().getName(), e);

        return switch (root) {
            case InternalServerError ise -> handleInternalServerError(ise);
            case NotFoundException nrf -> handleNoResourceFoundException(nrf);
            case NoResourceFoundException nrf -> handleNoResourceFoundException(nrf);
            case ParseException pe -> handleHtmlParseException(pe);
            case AccessDeniedException ade -> handleAccessDeniedException(ade);
            case NoHandlerFoundException nhfe -> handleNoHandlerFoundException(nhfe);
            case AssertionError ae -> handleAssertionError(ae);
            case MissingServletRequestParameterException msrpe -> handleMissingServletRequestParameterException(msrpe);
            case IllegalArgumentException iae -> handleIllegalArgumentException(iae);
            case DataAccessServiceException dae -> handleDataAccessServiceException(dae);
            case JsonProcessingException jpe -> handleJsonProcessingException(jpe);
            case MailException me -> handleMailException(me);
            case DuplicateUsernameException due -> handleDuplicateUsernameException(due);
            case DuplicateFiscalCodeException dfce -> handleDuplicateFiscalCodeException(dfce);
            case MethodArgumentNotValidException manve -> handleValidationException(manve);
            case BindException be -> handleValidationException(be);
            case ConstraintViolationException cve -> handleValidationException(cve);
            default -> buildDetailedErrorView(e, genericExceptionUri);
        };
    }



    /**
     * Handles runtime exceptions and returns a ModelAndView with a status of Internal Server Error (500).
     * Returns a ModelAndView with a view name of genericExceptionUri
     * and a model containing a custom error message.
     * The exception is logged at the ERROR level.
     * @param e the RuntimeException to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException e) {
        logger.error("Unexpected runtime error", e);
        String message = "An unexpected error occurred.";
        return new ModelAndView(genericExceptionUri, MESSAGE, message);
    }


    /**
     * Handles InternalServerError exceptions and returns a ModelAndView with a status of Internal Server Error (500).
     * Returns a ModelAndView with a view name of genericExceptionUri
     * and a model containing a custom error message.
     * The exception is logged at the ERROR level.
     * @param e the InternalServerError exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(InternalServerError.class)
    public ModelAndView handleInternalServerError(InternalServerError e) {
        logger.error("Internal server error", e);
        String message = "Internal server error: " + e.getMessage();
        return new ModelAndView(genericExceptionUri, MESSAGE, message);
    }


    /**
     * Handles exceptions that occur when a resource is not found, such as a 404 Not Found response.
     * Returns a ModelAndView object with a view name of "exception/not_found/not-found-exception"
     * and a model containing a custom error message.
     * The exception is logged at the ERROR level.
     * @param e the exception to be handled
     * @return a ModelAndView object
     */
    @ExceptionHandler(value = {NotFoundException.class, NoResourceFoundException.class})
    public ModelAndView handleNoResourceFoundException(Exception e) {
        logger.error("Resource not found", e);
        String message = "Resource not found: " + e.getMessage();
        return new ModelAndView(notFoundExceptionUri + "/not-found-exception", MESSAGE, message);
    }


    /**
     * Handles exceptions that occur when parsing a .html file, such as ParseErrors, SAXExceptions, and IOExceptions.
     * Returns a ModelAndView object with a view of genericExceptionUri and a status of Internal Server Error (500) and a model
     * containing a custom error message.
     * The exception is logged at the ERROR level.
     * @param e the exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(ParseException.class)
    public ModelAndView handleHtmlParseException(Exception e) {
        logger.error("Error during HTML parsing", e);
        String message = "Error during HTML parsing, HAI FATTO QUALCHE ERRORE IN UN FILE .HTML: " + e.getMessage();
        return new ModelAndView(genericExceptionUri, MESSAGE, message);
    }


    /**
     * Handles AccessDeniedException exceptions and returns a ModelAndView with a status of Forbidden (403).
     * The exception is logged at the ERROR level, and a model containing a custom error message.
     * This exception is thrown when a user tries to access a resource they do not have permission to access.
     * @param e the AccessDeniedException exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException e) {
        logger.error("Access denied", e);
        String message = "Access denied, NON HAI I PERMESSI: " + e.getMessage();
        return new ModelAndView(accessDeniedExceptionUri + "/access-denied-exception", MESSAGE, message);
    }


    /**
     * Handles NoHandlerFoundException exceptions and returns a ModelAndView with a status of Not Found (404).
     * The exception is logged at the ERROR level, and a model containing a custom error message.
     * This exception is thrown when a request is made to a URL that does not have a handler method.
     * This exception is thrown when a static resource (such as a .html file) is not found, as Spring MVC will not find a
     * handler for the request.
     * @param e the NoHandlerFoundException exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNoHandlerFoundException(NoHandlerFoundException e) {
        logger.error("No handler found for request", e);
        String message = "No controller/handler found for request: " + e.getMessage();
        return new ModelAndView(notFoundExceptionUri + "/object-not-found", MESSAGE, message);
    }


    /**
     * Handles exceptions that occur when Assertion error is throw.
     * Returns a ModelAndView with a view name of genericExceptionUri and
     * a model containing the error message.
     * The exception is logged at the ERROR level.
     * @param e the exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(AssertionError.class)
    public ModelAndView handleAssertionError(AssertionError e) {
        logger.error("Assertion error", e);
        String message = "An assertion error occurred: " + e.getMessage();
        return new ModelAndView(genericExceptionUri, MESSAGE, message);
    }


    /**
     * Handles exceptions that occur when a servlet request parameter is missing.
     * Returns a ModelAndView with a view of "exception/illegal/illegal-parameter" and
     * a model containing the error message.
     * The exception is logged at the ERROR level.
     * @param e the exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error("Missing servlet request parameter", e);
        String message = "Parameter cannot be null: " + e.getMessage();
        return new ModelAndView(illegalArgumentExceptionUri + "/illegal-parameter", MESSAGE, message);
    }


    /**
     * Handles IllegalArgumentException exceptions and returns a ModelAndView with a view name of "exception/illegal/illegal-parameters".
     * The exception is logged at the ERROR level and a model containing the error message.
     * @param e the IllegalArgumentException exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("Illegal argument", e);
        String message = "Illegal argument exception: " + e.getMessage();
        // fallback for others IllegalArgumentException
        return new ModelAndView(illegalArgumentExceptionUri + "/illegal-parameters", MESSAGE, message);
    }




    // Add more @ExceptionHandler methods for other specific exceptions


    /**
     * Handles exceptions that occur when there is an data access error.
     * Returns a ModelAndView with a view name of dataAccessExceptionUri
     * and a model containing a custom error message.
     * The exception is logged at the ERROR level.
     * @param e the DataAccessServiceException to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(DataAccessServiceException.class)
    public ModelAndView handleDataAccessServiceException(DataAccessServiceException e) {
        logger.error("Data access service error", e);
        String message = "Data access service error: " + e.getMessage();
        return new ModelAndView(dataAccessExceptionUri, MESSAGE, message);
    }


    /**
     * Handles custom ObjectNotFoundException and returns a ModelAndView with a view name of "exception/not_found/object-not-found"
     * The exception is logged at the ERROR level with the error message.
     * @param e the ObjectNotFoundException exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(ObjectNotFoundException.class)
    public ModelAndView handleObjectNotFoundException(ObjectNotFoundException e) {
        logger.error("Object not found", e);
        String message = "Object not found: " + e.getMessage();
        return new ModelAndView(notFoundExceptionUri + "/object-not-found", MESSAGE, message);
    }


    /**
     * Handles custom ObjectAlreadyExistsException and returns a ModelAndView with a view name of "exception/already_exists/object-already-exists"
     * The exception is logged at the ERROR level with the error message.
     * @param e the ObjectAlreadyExistsException exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ModelAndView handleObjectAlreadyExistsException(ObjectAlreadyExistsException e) {
        logger.error("Object already exists", e);
        String message = "Object already exists: " + e.getMessage();
        return new ModelAndView(alreadyExistsExceptionUri + "/object-already-exists", MESSAGE, message);
    }


    /**
     * Handles JsonProcessingException exceptions and returns a ModelAndView with a view name of "exception/data/json-processing-error"
     * The exception is logged at the ERROR level with the error message.
     * @param e the JsonProcessingException exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(JsonProcessingException.class)
    public ModelAndView handleJsonProcessingException(JsonProcessingException e) {
        logger.error("JSON processing error", e);
        String message = "An error occurred while processing JSON: " + e.getMessage();
        return new ModelAndView(jsonProcessingExceptionUri, MESSAGE, message);
    }


    @ExceptionHandler(MailException.class)
    public ModelAndView handleMailException(MailException e) {
        logger.error("Mail sending error", e);
        String message = "An error occurred while sending email: " + e.getMessage();
        return new ModelAndView(genericExceptionUri, MESSAGE, message);
    }


    /**
     * Handles DuplicateUsernameException exceptions and returns a ModelAndView with a view name of "exception/already_exists/duplicate-username"
     * The exception is logged at the ERROR level with the error message.
     * @param e the DuplicateUsernameException exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(DuplicateUsernameException.class)
    public ModelAndView handleDuplicateUsernameException(DuplicateUsernameException e) {
        logger.error("Duplicate username error", e);
        String message = "Username not available: " + e.getMessage();
        return new ModelAndView(duplicateUsernameUri, MESSAGE, message);
    }


    /**
     * Handles DuplicateFiscalCodeException exceptions and returns a ModelAndView with a view name of "exception/already_exists/duplicate-fiscal-code"
     * The exception is logged at the ERROR level with the error message.
     * @param e the DuplicateFiscalCodeException exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler(DuplicateFiscalCodeException.class)
    public ModelAndView handleDuplicateFiscalCodeException(DuplicateFiscalCodeException e) {
        logger.error("Duplicate fiscal code error", e);
        String message = "Codice fiscale già in uso: " + e.getMessage();
        return new ModelAndView(duplicateFiscalCodeUri, MESSAGE, message);
    }


    /**
     * Handles validation exceptions and returns a ModelAndView with a variable view name
     * and a model containing the error message.
     * The exception is logged at the ERROR level.
     *
     * @param e the exception to be handled
     * @return a ModelAndView containing the error message
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public ModelAndView handleValidationException(Exception e) {
        logger.error("Validation error: {}", e.getMessage(), e);

        ValidationInfo info = switch (e) {
            case MethodArgumentNotValidException ex -> extractValidationInfo(ex.getBindingResult());
            case BindException ex -> extractValidationInfo(ex.getBindingResult());
            case ConstraintViolationException ex -> extractConstraintViolationInfo(ex);
            default -> new ValidationInfo("", UNKNOWN_VALIDATION_ERROR);
        };

        String viewName = umsConfig.resolveView(info.fieldName());
        return new ModelAndView(viewName, MESSAGE, Optional.ofNullable(info.message()).orElse(UNKNOWN_VALIDATION_ERROR));
    }






    // helpers
    /**
     * Helper record ValidationInfo.
     */
    private record ValidationInfo(String fieldName, String message) {}


    /**
     * Helper for BindingResult (FieldError + ObjectError).
     */
    private ValidationInfo extractValidationInfo(BindingResult br) {
        if (br == null) return new ValidationInfo("", UNKNOWN_VALIDATION_ERROR);

        // Gestione FieldError
        FieldError fieldError = br.getFieldError();
        if (fieldError != null)
            return new ValidationInfo(
                normalizeFieldName(fieldError.getField()),
                fieldError.getDefaultMessage()
            );

        // Gestione ObjectError con mappatura scalabile
        ObjectError objectError = br.getGlobalError();
        if (objectError != null) {
            String fieldKey = mapObjectErrorCodeToField(objectError.getCode());
            return new ValidationInfo(
                fieldKey,
                objectError.getDefaultMessage() != null ? objectError.getDefaultMessage() : UNKNOWN_VALIDATION_ERROR
            );
        }

        // Nessun errore trovato
        return new ValidationInfo("", UNKNOWN_VALIDATION_ERROR);
    }


    /**
     * Helper for ConstraintViolationException.
     * Extracts the first constraint violation information.
     * @param ex the ConstraintViolationException to be handled
     * @return the extracted validation information
     */
    private ValidationInfo extractConstraintViolationInfo(ConstraintViolationException ex) {
    ConstraintViolation<?> violation = ex.getConstraintViolations().stream().findFirst().orElse(null);
        if (violation != null) {
            String path = violation.getPropertyPath().toString();
            String fieldName = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
            return new ValidationInfo(fieldName, violation.getMessage());
        }
        return new ValidationInfo("", UNKNOWN_VALIDATION_ERROR);
    }


    /**
     * Normalize field, take the last part after the dot if it exists.
     * Es: "username.usernameAlreadyTaken" -> "usernameAlreadyTaken"
     * @param rawFieldName the raw field name
     * @return the normalized field name
     */
    private String normalizeFieldName(String rawFieldName) {
        if (rawFieldName.contains("."))
            return rawFieldName.substring(rawFieldName.lastIndexOf('.') + 1);
        return rawFieldName;
    }


    /**
     * Maps an object error code to a field name.
     * @param code the error code
     * @return the corresponding field name
     */
    private String mapObjectErrorCodeToField(String code) {
        if (code == null) return "";

        return switch (code) {
            case "PasswordMatches" -> "passwordsDoNotMatch";
            case "SwapCoursesConstraint" -> "invalidChoice";
            default -> "";
        };
    }


    /**
     * Helper for extracting the root cause of an exception.
     * @param e the exception to inspect
     * @return the root cause of the exception
     */
    private Throwable getRootCause(Throwable e) {
        Throwable cause = e;
        while (cause.getCause() != null && cause.getCause() != cause)
            cause = cause.getCause();
        return cause;
    }


    /**
     * Helper for building a detailed error view.
     * @param e the exception to inspect
     * @param viewName the name of the view to render
     * @return a ModelAndView object containing the error details
     */
    private ModelAndView buildDetailedErrorView(Throwable e, String viewName) {
        ModelAndView mav = new ModelAndView(viewName);

        // exception type
        mav.addObject("errorType", e.getClass().getName());

        // Message
        mav.addObject(MESSAGE, e.getMessage());

        // Stack trace formatted as readable HTML
        StringBuilder stackTraceBuilder = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace())
            stackTraceBuilder.append(element.toString()).append("<br/>");
        mav.addObject("stackTrace", stackTraceBuilder.toString());

        // Cause
        Throwable cause = e.getCause();
        mav.addObject("cause", cause != null ? cause.toString() : "N/A");

        // Timestamp
        mav.addObject("timestamp", java.time.LocalDateTime.now());

        return mav;
    }






}
