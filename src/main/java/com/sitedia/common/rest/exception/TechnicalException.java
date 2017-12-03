package com.sitedia.common.rest.exception;

import java.io.IOException;

/**
 * Unmanaged exceptions thrown by the application.
 * @author sitedia
 *
 */
public class TechnicalException extends IOException {

    private static final long serialVersionUID = 1L;

    public TechnicalException(Throwable e) {
        super(e);
    }

}
