package com.minimarket.orders.orderservice.exception;

public class IllegalQuantityException extends RuntimeException {

    public IllegalQuantityException(Integer quantity) {
        super("Wrong quantity value (should be positive): " + quantity);
    }

    public IllegalQuantityException(String message) {
        super(message);
    }
}
