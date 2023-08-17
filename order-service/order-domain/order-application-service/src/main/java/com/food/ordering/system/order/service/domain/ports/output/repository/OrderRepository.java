package com.food.ordering.system.order.service.domain.ports.output.repository;

import com.food.ordering.system.domain.valueobjects.OrderId;
import com.food.ordering.system.order.service.domain.entities.Order;
import com.food.ordering.system.order.service.domain.valueobjects.TrackingId;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(OrderId orderId);

    Optional<Order> findByTrackingId(TrackingId trackingId);
}
