package com.food.ordering.system.order.service.domain.events;

import com.food.ordering.system.order.service.domain.entities.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {

    // will trigger to publish a message to payment-request topic (will trigger payment service)
    // Hence, will be included in the payment_outbox table as a json object.

    public OrderCreatedEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
