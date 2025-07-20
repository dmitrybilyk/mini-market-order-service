package com.minimarket.orders.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "timestamp", OffsetDateTime.now().toString(),
                    "status", 404,
                    "error", "Not Found",
                    "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleRateLimitExceeded(RateLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(Map.of(
                    "timestamp", OffsetDateTime.now().toString(),
                    "status", 406,
                    "error", "Not Acceptable",
                    "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(IllegalQuantityException.class)
    public ResponseEntity<Map<String, Object>> handIllegalQuantity(IllegalQuantityException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(Map.of(
                    "timestamp", OffsetDateTime.now().toString(),
                    "status", 406,
                    "error", "Not Acceptable",
                    "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "timestamp", OffsetDateTime.now().toString(),
                    "status", 500,
                    "error", "Internal Server Error",
                    "message", ex.getMessage()
                ));
    }
}
