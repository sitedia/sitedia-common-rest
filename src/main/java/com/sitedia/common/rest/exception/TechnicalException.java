package com.sitedia.common.rest.exception;

import java.io.IOException;

/**
 * Main exception for the application
 * @author cedric
 *
 */
public class TechnicalException extends IOException {

    private static final long serialVersionUID = 1L;

    public TechnicalException(Throwable e) {
        super(e);
    }

}
