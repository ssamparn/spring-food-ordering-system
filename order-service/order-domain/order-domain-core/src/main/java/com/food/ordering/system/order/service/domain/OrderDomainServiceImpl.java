package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.entities.Order;
import com.food.ordering.system.order.service.domain.entities.Product;
import com.food.ordering.system.order.service.domain.entities.Restaurant;
import com.food.ordering.system.order.service.domain.events.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.events.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.events.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exceptions.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.food.ordering.system.domain.DomainConstants.UTC_TIMEZONE;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initialized", order.getId().getValue());

        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC_TIMEZONE)));
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id: " + restaurant.getId().getValue() +
                    " does not receive any order!");
        }
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getOrderItems().forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct -> {
            Product currentProduct = orderItem.getProduct();
            if (currentProduct.equals(restaurantProduct)) {
                currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
            }
        }));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id: {} is paid", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC_TIMEZONE)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} is approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order,
                                                  List<String> failureMessages) {
        order.initializeCancellation(failureMessages);
        log.info("Order payment is cancelling for order id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC_TIMEZONE)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id: {} is cancelled", order.getId().getValue());
    }
}
