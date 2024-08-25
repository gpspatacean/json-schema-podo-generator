package com.spatacean.json.schema.podo.generator.core.services;

/**
 * Exception class that wraps
 * {@link com.spatacean.json.schema.podo.generator.core.annotations.SchemaGenerator}
 * objects (generators) Instantiation Errors
 *
 * @author George Spătăcean
 */
public class GeneratorInstantiationException extends RuntimeException {

    public GeneratorInstantiationException(final String message) {
        super(message);
    }

    public GeneratorInstantiationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
