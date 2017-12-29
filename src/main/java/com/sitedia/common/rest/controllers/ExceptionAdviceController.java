package com.sitedia.common.rest.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sitedia.common.rest.dto.StatusDTO;
import com.sitedia.common.rest.exceptions.BusinessException;

/**
 * First level of errors thrown by controller. At this level we try to reply with a user-friendly message to the user
 * 
 * @author sitedia
 */
@ControllerAdvice
public class ExceptionAdviceController {

    private static Logger logger = Logger.getLogger(ExceptionAdviceController.class.getName());

    @Autowired
    private MessageSource messageSource;

    /**
     * Business exceptions, managed by the application. That's why we don't display stack trace in INFO level
     */
    @ExceptionHandler(value = { BusinessException.class, HttpMessageNotReadableException.class, HttpMediaTypeNotSupportedException.class })
    public ResponseEntity<StatusDTO> handleBusinessException(Exception e) {

        // Specific log for monitoring
        Logger.getLogger("error.Business").info(e.getMessage());
        logger.log(Level.FINE, e.getMessage(), e);

        StatusDTO error = new StatusDTO("BUSINESS", e.getMessage(), null);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Validation exceptions, thrown by controller and DTO checks
     */
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<StatusDTO> handleArgumentNotValidException(MethodArgumentNotValidException e) {

        // Specific log for monitoring
        Logger.getLogger("error.Business").info(e.getMessage());
        logger.log(Level.FINE, e.getMessage(), e);

        // Extract all errors
        List<ObjectError> objectErrors = e.getBindingResult().getAllErrors();
        List<String> errors = new ArrayList<>();
        for (ObjectError error : objectErrors) {
            errors.add(error.getDefaultMessage());
        }

        StatusDTO error = new StatusDTO("VALIDATION", e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Access denied exceptions
     */
    @ExceptionHandler(value = { AccessDeniedException.class, BadCredentialsException.class })
    public ResponseEntity<StatusDTO> handleAccessDeniedException(Exception e) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = authentication.getName() != null && !authentication.getName().equals("anonymousUser");

        // Specific log for monitoring
        Logger.getLogger("endpoint.AccessDenied").warning(e.getMessage());
        logger.log(Level.FINE, e.getMessage(), e);

        String message = messageSource.getMessage("sitedia.commonRest.endpoint.accessDenied", null, Locale.getDefault());
        StatusDTO error = new StatusDTO("ACCESS_DENIED", message, null);
        return new ResponseEntity<>(error, authenticated ? HttpStatus.FORBIDDEN : HttpStatus.UNAUTHORIZED);
    }

    /**
     * Others exceptions. For security reasons, the message is not returned to the user
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<StatusDTO> handleException(Exception e) {

        // Specific log for monitoring
        Logger.getLogger("error.Internal").severe(e.getMessage());
        logger.log(Level.SEVERE, e.getMessage(), e);

        String message = messageSource.getMessage("sitedia.commonRest.endpoint.internalError", null, LocaleContextHolder.getLocale());
        StatusDTO error = new StatusDTO("INTERNAL", message, null);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
