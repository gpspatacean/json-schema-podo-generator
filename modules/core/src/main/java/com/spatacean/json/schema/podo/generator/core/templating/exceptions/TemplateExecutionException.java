package com.spatacean.json.schema.podo.generator.core.templating.exceptions;

public class TemplateExecutionException extends RuntimeException {
    public TemplateExecutionException(final String message){
        super(message);
    }

    public TemplateExecutionException(final String message, final Throwable cause){
        super(message, cause);
    }
}
