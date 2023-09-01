package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.service.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.CustomerAvroModel;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.customer.CustomerResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CustomerResponseKafkaListener implements KafkaConsumer<CustomerAvroModel> {

    private final CustomerResponseMessageListener customerResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public CustomerResponseKafkaListener(CustomerResponseMessageListener customerResponseMessageListener,
                                         OrderMessagingDataMapper orderMessagingDataMapper) {
        this.customerResponseMessageListener = customerResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.customer-group-id}", topics = "${order-service.customer-topic-name}")
    public void receive(@Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Payload List<CustomerAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of customer create messages received with keys {}, partitions {} and offsets {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(customerAvroModel ->
                customerResponseMessageListener.customerCreated(orderMessagingDataMapper.customerAvroModelToCustomerModel(customerAvroModel)));
    }
}
