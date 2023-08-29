package com.food.ordering.system.order.service.domain.events;

import com.food.ordering.system.order.service.domain.entities.Order;

import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent {

    // will trigger to publish a message to restaurant-approval-request topic (will trigger restaurant service)
    // Hence, will be included in the restaurant_outbox table as a json object.

    public OrderPaidEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
