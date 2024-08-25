package com.spatacean.json.schema.podo.generator.core.templating.exceptions;

public class TemplateNotLoadedException extends RuntimeException {
    public TemplateNotLoadedException(String message) {
        super(message);
    }

    public TemplateNotLoadedException(String message, Throwable cause) {
        super(message, cause);
    }
}
