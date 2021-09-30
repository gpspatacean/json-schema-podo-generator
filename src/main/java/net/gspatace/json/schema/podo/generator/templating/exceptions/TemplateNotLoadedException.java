package net.gspatace.json.schema.podo.generator.templating.exceptions;

public class TemplateNotLoadedException extends RuntimeException {
    public TemplateNotLoadedException(String message) {
        super(message);
    }

    public TemplateNotLoadedException(String message, Throwable cause) {
        super(message, cause);
    }
}
