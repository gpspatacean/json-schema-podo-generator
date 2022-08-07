package net.gspatace.json.schema.podo.generator.core.services;

/**
 * Exception class for when a generator is not found by name
 *
 * @author George Spătăcean
 */
public class GeneratorNotFoundException extends Exception {

    public GeneratorNotFoundException(final String message) {
        super(message);
    }

    public GeneratorNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
