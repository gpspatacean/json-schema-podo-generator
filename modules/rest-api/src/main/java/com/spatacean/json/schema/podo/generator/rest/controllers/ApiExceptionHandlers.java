package com.spatacean.json.schema.podo.generator.rest.controllers;

import com.spatacean.json.schema.podo.generator.core.services.CustomOptionsInstantiationException;
import com.spatacean.json.schema.podo.generator.core.services.GeneratorInstantiationException;
import com.spatacean.json.schema.podo.generator.core.services.GeneratorNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

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
    private ResponseEntity<ProblemDetail> handleGeneratorNotFoundException(final GeneratorNotFoundException exception, final HttpServletRequest request) {
        final ProblemDetail problemDetail = buildProblemDetail(HttpStatus.NOT_FOUND, "Generator not found.", exception.getLocalizedMessage(), request);
        log.error("Exception caught:", exception);
        return buildResponseEntity(problemDetail);
    }

    @ExceptionHandler({GeneratorInstantiationException.class})
    private ResponseEntity<ProblemDetail> handleGeneratorInstantiationException(final GeneratorInstantiationException exception, final HttpServletRequest request) {
        final ProblemDetail problemDetail = buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Generator Instantiation error.", exception.getLocalizedMessage(), request);
        log.error("Exception caught:", exception);
        return buildResponseEntity(problemDetail);
    }

    @ExceptionHandler({CustomOptionsInstantiationException.class})
    private ResponseEntity<ProblemDetail> handleCustomOptionsInstantiationException(final CustomOptionsInstantiationException exception, final HttpServletRequest request) {
        final ProblemDetail problemDetail = buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Custom Properties Object Instantiation error.", exception.getLocalizedMessage(), request);
        log.error("Exception caught:", exception);
        return buildResponseEntity(problemDetail);
    }

    private ProblemDetail buildProblemDetail(final HttpStatus status,
                                             final String title,
                                             final String detail,
                                             final HttpServletRequest request) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);

        final String requestUri = request.getRequestURI();
        if (requestUri != null && !requestUri.isBlank()) {
            problemDetail.setInstance(URI.create(requestUri));
        }

        return problemDetail;
    }

    private ResponseEntity<ProblemDetail> buildResponseEntity(final ProblemDetail problemDetail) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<>(problemDetail, headers, HttpStatus.valueOf(problemDetail.getStatus()));
    }
}
