package com.spatacean.json.schema.podo.generator.core.base;

/**
 * Schema retrieval exception
 *
 * @author George Spătăcean
 */
public class SchemaRetrievalException extends RuntimeException {
    public SchemaRetrievalException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
