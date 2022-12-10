package com.food.ordering.system.restaurant.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.restaurant.service.domain.config.RestaurantServiceConfigData;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderApprovedKafkaMessagePublisher implements OrderApprovedMessagePublisher {

    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final RestaurantServiceConfigData restaurantServiceConfigData;

    public OrderApprovedKafkaMessagePublisher(RestaurantMessagingDataMapper restaurantMessagingDataMapper,
                                              KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer,
                                              RestaurantServiceConfigData restaurantServiceConfigData) {
        this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.restaurantServiceConfigData = restaurantServiceConfigData;
    }

    @Override
    public void publish(OrderApprovedEvent domainEvent) {
        String orderId = domainEvent.getOrderApproval().getOrderId().getValue().toString();
        log.info("Received OrderApprovedEvent for order id: {}", orderId);

        try {
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel = restaurantMessagingDataMapper
                    .orderApprovedEventToRestaurantApprovalResponseAvroModel(domainEvent);

            kafkaProducer.send(restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                    orderId,
                    restaurantApprovalResponseAvroModel
            );

            log.info("RestaurantApprovalResponseAvroModel sent to Kafka at: {}", System.nanoTime());
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalResponseAvroModel message to kafka with order id: {}. error: {}",
                    orderId, e.getMessage());
        }
    }
}
