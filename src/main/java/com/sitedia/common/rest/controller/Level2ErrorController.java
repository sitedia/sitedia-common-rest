package com.sitedia.common.rest.controller;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Error controller
 * @author cedric
 *
 */
@RestController
public class Level2ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {

    private static final String PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    /**
     * Format all errors
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = PATH, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Extract message
            RequestAttributes requestAttributes = new ServletRequestAttributes(request);
            Map<String, Object> errors = errorAttributes.getErrorAttributes(requestAttributes, true);

            // Return a basic page with the header
            if (errors.get("error") != null && errors.get("error").equals("Not Found")) {
                Logger.getLogger("error.404").info(String.format("Page not found: %s", errors.get("path")));
                return new ResponseEntity<>("<h1>Page non trouvée</h1>", HttpStatus.NOT_FOUND);
            } else {
                String message = new ObjectMapper().writeValueAsString(errors);
                Logger.getLogger("error.500").severe(String.format("Internal error: %s", message));
                return new ResponseEntity<>("<h1>Site en cours de maintenance</h1>", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

}
