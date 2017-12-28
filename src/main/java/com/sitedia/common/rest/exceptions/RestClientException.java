package com.sitedia.common.rest.exceptions;

import java.io.IOException;

/**
 * Main exception for the application
 * @author cedric
 *
 */
public class RestClientException extends IOException {

    private static final long serialVersionUID = 1L;

    public RestClientException(String message) {
        super(message);
    }

}
