package net.gspatace.json.schema.podo.generator.base;

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
