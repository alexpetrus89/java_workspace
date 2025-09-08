package com.alex.universitymanagementsystem.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/exception")
public class ThymeleafExceptionController implements ErrorController {

    @GetMapping("/generic-exception")
    public String handleError(HttpServletRequest request, Model model) {
        Throwable exception = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        ErrorDetails errorDetails = buildErrorDetails(exception);

        model.addAttribute("errorType", errorDetails.errorType());
        model.addAttribute("message", errorDetails.message());
        model.addAttribute("cause", errorDetails.cause());
        model.addAttribute("timestamp", errorDetails.timestamp());
        model.addAttribute("stackTrace", errorDetails.stackTrace());
        model.addAttribute("rootCause", errorDetails.rootCause());

        return "exception/generic-exception";
    }

    private ErrorDetails buildErrorDetails(Throwable exception) {
        if (exception == null) {
            return new ErrorDetails(
                "UnknownError",
                "Unknown error",
                "N/A",
                LocalDateTime.now(),
                "No stack trace available",
                "N/A"
            );
        }

        Throwable rootCause = getRootCause(exception);

        return new ErrorDetails(
            exception.getClass().getSimpleName(),
            Optional.ofNullable(exception.getMessage()).orElse("No message"),
            Optional.ofNullable(exception.getCause())
                .map(c -> c.getClass().getSimpleName() + ": " + c.getMessage())
                .orElse("N/A"),
            LocalDateTime.now(),
            Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n")),
            Optional.ofNullable(rootCause)
                .map(c -> c.getClass().getSimpleName() + ": " + c.getMessage())
                .orElse("N/A")
        );
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause == null || cause == throwable)
            return throwable;
        return getRootCause(cause);
    }

    private record ErrorDetails(
        String errorType,
        String message,
        String cause,
        LocalDateTime timestamp,
        String stackTrace,
        String rootCause
    ) {}


}

