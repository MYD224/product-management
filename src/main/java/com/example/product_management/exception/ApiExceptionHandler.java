package com.example.product_management.exception;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = EntityNotFoundException.class)
    public @ResponseBody ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e, WebRequest request){
        ApiException apiException = new ApiException(HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getDescription(false), ZonedDateTime.now());
        return new ResponseEntity<>(
                apiException,
                HttpStatus.BAD_REQUEST
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
        ApiException errorDetails = new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), request.getDescription(false), ZonedDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public @ResponseBody ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public @ResponseBody ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>("You are not authorized to access this resource", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public @ResponseBody ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
        return new ResponseEntity<>("The provided token is expired. login again to get a valid one", HttpStatus.UNAUTHORIZED);
    }
}
