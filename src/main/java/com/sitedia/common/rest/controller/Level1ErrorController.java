package com.sitedia.common.rest.controller;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.sitedia.common.rest.dto.ErrorDTO;
import com.sitedia.common.rest.exception.BusinessException;
import com.sitedia.common.rest.exception.TechnicalException;

/**
 * Interceptor for all exceptions in controller
 * @author cedric
 *
 */
@ControllerAdvice
public class Level1ErrorController {

    private static final Logger businessLogger = Logger.getLogger("error.Business");

    private static final Logger applicationLogger = Logger.getLogger("error.Application");

    private static final Logger exceptionLogger = Logger.getLogger("error.Internal");

    private static final Logger authLogger = Logger.getLogger("auth.AccessDenied");

    /**
     * Business exception
     * @param ex
     * @return
     */
    @ExceptionHandler(value = { BusinessException.class, HttpMessageNotReadableException.class, HttpMediaTypeNotSupportedException.class })
    public ResponseEntity<ErrorDTO> businessException(Exception e) {
        businessLogger.info(e.getMessage());
        ErrorDTO error = new ErrorDTO("BUSINESS", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Mapping error
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO> businessException(MethodArgumentTypeMismatchException e) {
        applicationLogger.warning(e.getMessage());
        ErrorDTO error = new ErrorDTO("MAPPING", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = TechnicalException.class)
    public ResponseEntity<ErrorDTO> technicalException(TechnicalException e) {
        exceptionLogger.log(Level.SEVERE, e.getMessage(), e);
        ErrorDTO error = new ErrorDTO("TECHNICAL", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // MethodArgumentNotValidException

    /**
     * Technical exception
     * @param ex
     * @return
     */
    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<ErrorDTO> handleValidationFailure(ConstraintViolationException ex) {
        ConstraintViolation<?> violation = ex.getConstraintViolations().iterator().next();
        ErrorDTO error = new ErrorDTO("CONSTRAINT", violation.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Technical exception
     * @param ex
     * @return
     */
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<ErrorDTO> handleArgumentNotValid(MethodArgumentNotValidException e) {

        // Extract default message
        String message = e.getBindingResult().getFieldError().getDefaultMessage();

        // Extract all errors
        List<ObjectError> objectErrors = e.getBindingResult().getAllErrors();
        List<String> errors = new ArrayList<>();
        for (ObjectError error : objectErrors) {
            errors.add(error.getDefaultMessage());
        }

        // Return the error
        businessLogger.info(message);
        ErrorDTO error = new ErrorDTO("VALIDATION", message, errors);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Incorrect credentials
     * @param ex
     * @return
     */
    @ExceptionHandler(value = { AccessDeniedException.class })
    public ResponseEntity<ErrorDTO> handleAccessDenied(AccessDeniedException e) {
        authLogger.warning(e.getMessage());
        ErrorDTO error = new ErrorDTO("ACCESS_DENIED", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Internal error
     * @param ex
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ErrorDTO> handleOther(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)
            throws IOException {
        exceptionLogger.log(Level.SEVERE, e.getMessage(), e);
        ErrorDTO errorDTO = new ErrorDTO("OTHER", "Sorry, an internal error occured.");
        String path = request.getRequestURI();

        if (path.startsWith("/api") || path.contains("/404/") || path.contains("/500/")) {

            // API
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        } else {

            // HTML
            response.sendRedirect("../500/index.html");
            return null;
        }
    }

}
