package net.gspatace.json.schema.podo.generator.templating.exceptions;

public class TemplateExecutionException extends RuntimeException {
    public TemplateExecutionException(final String message){
        super(message);
    }

    public TemplateExecutionException(final String message, final Throwable cause){
        super(message, cause);
    }
}
