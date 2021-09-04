package net.gspatace.json.schema.podo.generator.templating.exceptions;

import com.samskivert.mustache.Template;

public class TemplateNotLoadedException extends RuntimeException {
    public TemplateNotLoadedException(String message) {
        super(message);
    }

    public TemplateNotLoadedException(String message, Throwable cause) {
        super(message, cause);
    }
}
