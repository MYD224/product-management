package com.example.product_management.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ApiException {
    private int statusCode;
    private String message;
    private String details;
    private final ZonedDateTime zonedDateTime;
}
