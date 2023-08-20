package com.food.ordering.system.order.service.domain.sagacoordinator;

import com.food.ordering.system.domain.valueobjects.OrderId;
import com.food.ordering.system.order.service.domain.entities.Order;
import com.food.ordering.system.order.service.domain.exceptions.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderSagaHelper {

    private final OrderRepository orderRepository;

    public OrderSagaHelper(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findOrder(String orderId) {
        Optional<Order> orderResponseOptional = this.orderRepository.findById(new OrderId(UUID.fromString(orderId)));
        if (orderResponseOptional.isEmpty()) {
            log.error("Order with id: {} could not be found!", orderId);
            throw new OrderNotFoundException("Order with id " + orderId + " could not be found");
        }
        return orderResponseOptional.get();
    }

    public void saveOrder(Order order) {
        this.orderRepository.save(order);
    }
}
