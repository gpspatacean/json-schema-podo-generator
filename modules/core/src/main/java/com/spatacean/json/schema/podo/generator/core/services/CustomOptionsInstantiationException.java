package com.spatacean.json.schema.podo.generator.core.services;

/**
 * Exception class that wraps
 * {@link com.spatacean.json.schema.podo.generator.core.annotations.CustomProperties}
 * objects Instantiation Errors
 *
 * @author George Spătăcean
 */
public class CustomOptionsInstantiationException extends RuntimeException {

    public CustomOptionsInstantiationException(final String message) {
        super(message);
    }

    public CustomOptionsInstantiationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
