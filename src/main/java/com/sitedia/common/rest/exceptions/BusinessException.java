package com.sitedia.common.rest.exceptions;

import java.io.IOException;

/**
 * Managed exceptions thrown by the developer. To be used for business errors.
 * @author sitedia
 *
 */
public class BusinessException extends IOException {

    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }

}
