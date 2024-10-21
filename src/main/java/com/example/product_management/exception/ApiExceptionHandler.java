package com.example.product_management.exception;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.time.ZonedDateTime;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = EntityNotFoundException.class)
    public @ResponseBody ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e, WebRequest request){
        ApiException apiException = new ApiException(HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getDescription(false), ZonedDateTime.now());
        return new ResponseEntity<>(
                apiException,
                HttpStatus.NOT_FOUND
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = AuthenticationException.class)
    public @ResponseBody ResponseEntity<Object> handleAutheticationException(AuthenticationException e, WebRequest request){
        ApiException apiException = new ApiException(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), request.getDescription(false), ZonedDateTime.now());
        return new ResponseEntity<>(
                apiException,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        ApiException apiException = new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), request.getDescription(false), ZonedDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public @ResponseBody ResponseEntity<ApiException> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ApiException apiException = new ApiException(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials", request.getDescription(false), ZonedDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public @ResponseBody ResponseEntity<ApiException> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ApiException apiException = new ApiException(HttpStatus.FORBIDDEN.value(), "You are not authorized to access this resource", request.getDescription(false), ZonedDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public @ResponseBody ResponseEntity<ApiException> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        StringBuilder message = new StringBuilder();
        ex.getConstraintViolations().forEach(violation -> message.append(violation.getMessage()).append("; "));
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST.value(), "Validation failed", message.toString(), ZonedDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody ResponseEntity<ApiException> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        StringBuilder message = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> message.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; "));
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST.value(), "Validation error", message.toString(), ZonedDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public @ResponseBody ResponseEntity<ApiException> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST.value(), "Invalid argument", ex.getMessage(), ZonedDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public @ResponseBody ResponseEntity<ApiException> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        ApiException apiException = new ApiException(HttpStatus.CONFLICT.value(), "Data integrity violation", ex.getRootCause().getMessage(), ZonedDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ExpiredJwtException.class)
    public @ResponseBody ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
        return new ResponseEntity<>("The provided token is expired. login again to get a valid one", HttpStatus.UNAUTHORIZED);
    }
}
