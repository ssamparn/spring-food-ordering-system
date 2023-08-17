package com.food.ordering.system.order.service.domain.mapper;

import com.food.ordering.system.domain.valueobjects.CustomerId;
import com.food.ordering.system.domain.valueobjects.Money;
import com.food.ordering.system.domain.valueobjects.ProductId;
import com.food.ordering.system.domain.valueobjects.RestaurantId;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.create.OrderItem;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.entities.Order;
import com.food.ordering.system.order.service.domain.entities.Product;
import com.food.ordering.system.order.service.domain.entities.Restaurant;
import com.food.ordering.system.order.service.domain.valueobjects.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.Builder.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand
                        .getItems()
                        .stream()
                        .map(orderItemDto -> new Product(new ProductId(orderItemDto.getProductId())))
                        .collect(Collectors.toList()))
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.Builder.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getOrderAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .orderItems(mapOrderItemsDtoToEntities(createOrderCommand.getItems()))
                .build();
    }

    private List<com.food.ordering.system.order.service.domain.entities.OrderItem> mapOrderItemsDtoToEntities(List<OrderItem> orderItemsDto) {
        return orderItemsDto.stream()
                .map(orderItemDto -> com.food.ordering.system.order.service.domain.entities.OrderItem.Builder.builder()
                        .product(new Product(new ProductId(orderItemDto.getProductId())))
                        .price(new Money(orderItemDto.getPrice()))
                        .quantity(orderItemDto.getQuantity())
                        .subTotal(new Money(orderItemDto.getSubTotal()))
                        .build()
                )
                .collect(Collectors.toList());
    }


    private StreetAddress orderAddressToStreetAddress(OrderAddress orderAddress) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAddress.getStreetName(),
                orderAddress.getPostalCode(),
                orderAddress.getCity()
        );
    }

    public CreateOrderResponse createOrderToCreateOrderResponse(Order savedOrder, String message) {
        return CreateOrderResponse.builder()
                .orderTrackingId(savedOrder.getTrackingId().getValue())
                .orderStatus(savedOrder.getOrderStatus())
                .message(message)
                .build();
    }

    public TrackOrderResponse createOrderToTrackOrderResponse(Order order) {
        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }
}
