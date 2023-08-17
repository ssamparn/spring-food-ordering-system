package com.food.ordering.system.order.service.domain.exceptions;

public class OrderNotFoundException extends OrderDomainException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
