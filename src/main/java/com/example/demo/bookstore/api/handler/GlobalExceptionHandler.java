package com.example.demo.bookstore.api.handler;

import com.example.demo.bookstore.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ProblemDetail buildProblemDetail(String title, Exception ex, HttpStatus status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setProperty("traceId", getTraceId(request));
        problemDetail.setTitle(title);
        try {
            problemDetail.setInstance(new URI(getRequestUri(request)));
        } catch (URISyntaxException e) {
            log.error(e.getMessage(), e);
        }
        return problemDetail;
    }

    private String getRequestUri(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            return ((ServletWebRequest) request).getRequest().getRequestURI();
        } else {
            return "Unknown";
        }
    }

    private String getTraceId(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            return ((ServletWebRequest) request).getHeader("trace-id");
        } else {
            return "Unknown";
        }
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ProblemDetail> handleIllegalStateException(
            IllegalStateException ex, WebRequest request) {
        log.error("IllegalState: ", ex);
        ProblemDetail errorResponse = buildProblemDetail("IllegalState", ex, HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<ProblemDetail>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ProblemDetail> handleEntityNotFoundException(
            EntityNotFoundException ex, WebRequest request) {
        log.error("Entity NotFound: {}", request, ex);
        ProblemDetail errorResponse = buildProblemDetail("Not Found", ex, HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<ProblemDetail>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected ResponseEntity<ProblemDetail> handleHttpMediaTypeNotAcceptableException(
            HttpMediaTypeNotAcceptableException ex, WebRequest request) {
        log.error("Request Not Supported: ", ex);
        ProblemDetail errorResponse = buildProblemDetail("Request Not Supported", ex, HttpStatus.NOT_ACCEPTABLE, request);
        return new ResponseEntity<ProblemDetail>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected ResponseEntity<ProblemDetail> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {
        log.error("Request Not Supported: ", ex);
        ProblemDetail errorResponse = buildProblemDetail("Request Not Supported", ex, HttpStatus.METHOD_NOT_ALLOWED, request);
        return new ResponseEntity<ProblemDetail>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ProblemDetail> handleRuntimeException(
            Exception ex, WebRequest request) {
        log.error("UnexpectedError: ", ex);
        ProblemDetail errorResponse = buildProblemDetail("Request Error", ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
        return new ResponseEntity<ProblemDetail>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        log.error("Request Input Invalid: ", ex);
        ArrayList<String> errorMessages = new ArrayList<>();
        ex.getFieldErrors().forEach(fieldError -> errorMessages.add(fieldError.getDefaultMessage()));
        String join = String.join(",", errorMessages);
        ProblemDetail errorResponse = buildProblemDetail("Request Input Invalid", ex, HttpStatus.BAD_REQUEST, request);
        errorResponse.setDetail(join);
        return new ResponseEntity<ProblemDetail>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
