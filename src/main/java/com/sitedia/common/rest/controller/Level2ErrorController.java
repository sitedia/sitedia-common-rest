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
 * Second level of errors thrown by controller. First level failed, so we reply
 * with a simple message
 * 
 * @author sitedia
 */
@RestController
public class Level2ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {

    private static final String PATH = "/error";

    private static Logger logger = Logger.getLogger(Level2ErrorController.class.getName());

    @Autowired
    private ErrorAttributes errorAttributes;

    /**
     * Returns a simple message to the user
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
            String message = new ObjectMapper().writeValueAsString(errors);

            // Specific log for monitoring
            Logger.getLogger("error.critical").severe(message);
            logger.severe(message);

            return new ResponseEntity<>("<h1>Internal server error</h1><h4>Please retry later</h4>", HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {

            // Specific log for monitoring
            Logger.getLogger("error.critical").severe(e.getMessage());
            logger.log(Level.SEVERE, e.getMessage(), e);

            return new ResponseEntity<>("<h1>Internal server error</h1><h4>Please retry later</h4>", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

}
