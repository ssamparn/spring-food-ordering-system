package com.food.ordering.system.kafka.producer.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.order.service.domain.exceptions.OrderDomainException;
import com.food.ordering.system.outbox.OutboxStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
public class KafkaMessageHelper {

    private final ObjectMapper objectMapper;

    public KafkaMessageHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T getOrderEventPayload(String payload, Class<T> outputType) {
        try {
            return objectMapper.readValue(payload, outputType);
        } catch (JsonProcessingException e) {
            log.error("Could not read {} object!", outputType.getName(), e);
            throw new OrderDomainException("Could not read " + outputType.getName() + " object!", e);
        }
    }

    public <T, U> BiConsumer<SendResult<String, T>, Throwable> getKafkaCallback(
            String topicName,
            T avroModel,
            U outboxMessage,
            BiConsumer<U, OutboxStatus> outboxCallback,
            String key,
            String avroModelName) {

        return (successResult, exception) -> {
            if (exception == null) {
                RecordMetadata recordMetadata = successResult.getRecordMetadata();
                log.info("Received successful response from Kafka for order id: {} Topic: {} Partition: {} Offset: {} Timestamp:{}",
                        key,
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.offset(),
                        recordMetadata.timestamp());
                outboxCallback.accept(outboxMessage, OutboxStatus.COMPLETED);
            } else {
                log.error("Error while sending {} with message: {} and outbox type: {} to topic {}",
                        avroModelName,
                        avroModel.toString(),
                        outboxMessage.getClass().getName(),
                        topicName, exception);
                outboxCallback.accept(outboxMessage, OutboxStatus.FAILED);
            }
        };
    }
}
