package com.example.product_management.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ApiException {
    private final int statusCode;
    private final String message;
    private final String details;
    private final ZonedDateTime zonedDateTime;
}
