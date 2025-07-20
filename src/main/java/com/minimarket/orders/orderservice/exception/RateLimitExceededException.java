package com.minimarket.orders.orderservice.exception;

public class RateLimitExceededException extends RuntimeException {

    public RateLimitExceededException(Long orderId) {
        super("Order not found with ID: " + orderId);
    }

    public RateLimitExceededException(String message) {
        super(message);
    }
}
