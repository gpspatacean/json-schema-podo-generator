package com.spatacean.json.schema.podo.generator.rest.models;

/**
 * Presentation API Error class for REST layer.
 * See <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC Standard</a>
 *
 * @param title generic failure explanation
 * @param status HTTP response code
 * @param details more detailed failure explanation
 * @param instance the origin URI
 *
 * @author George Spătăcean
 */
public record ProblemDetails(String title, int status, String details, String instance) {
}
