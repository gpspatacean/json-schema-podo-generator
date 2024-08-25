package com.spatacean.json.schema.podo.generator.rest.controllers;

import com.spatacean.json.schema.podo.generator.rest.models.ProblemDetails;
import lombok.extern.slf4j.Slf4j;
import com.spatacean.json.schema.podo.generator.core.services.CustomOptionsInstantiationException;
import com.spatacean.json.schema.podo.generator.core.services.GeneratorInstantiationException;
import com.spatacean.json.schema.podo.generator.core.services.GeneratorNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Generic Controller Advice for handling exceptions in the
 * REST API layer
 *
 * @author George Spătăcean
 */
@Slf4j
@ControllerAdvice
public class ApiExceptionHandlers extends ResponseEntityExceptionHandler {

    @ExceptionHandler({GeneratorNotFoundException.class})
    private ResponseEntity<Object> handleGeneratorNotFoundException(final GeneratorNotFoundException exception, final HttpServletRequest request) {
        final ProblemDetails problemDetails = new ProblemDetails("Generator not found.", HttpStatus.NOT_FOUND.value(), exception.getLocalizedMessage(), request.getRequestURI());
        log.error("Exception caught:", exception);
        return buildResponseEntity(problemDetails);
    }

    @ExceptionHandler({GeneratorInstantiationException.class})
    private ResponseEntity<Object> handleGeneratorInstantiationException(final GeneratorInstantiationException exception, final HttpServletRequest request){
        final ProblemDetails problemDetails = new ProblemDetails("Generator Instantiation error.", HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getLocalizedMessage(), request.getRequestURI());
        log.error("Exception caught:", exception);
        return buildResponseEntity(problemDetails);
    }

    @ExceptionHandler({CustomOptionsInstantiationException.class})
    private ResponseEntity<Object> handleCustomOptionsInstantiationException(final CustomOptionsInstantiationException exception, final HttpServletRequest request){
        final ProblemDetails problemDetails = new ProblemDetails("Custom Properties Object Instantiation error.", HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getLocalizedMessage(), request.getRequestURI());
        log.error("Exception caught:", exception);
        return buildResponseEntity(problemDetails);
    }

    private ResponseEntity<Object> buildResponseEntity(final ProblemDetails problemDetails){
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<>(problemDetails, headers, HttpStatus.valueOf(problemDetails.status()));
    }
}
