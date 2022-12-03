package com.food.ordering.system.kafka.producer.service.impl;

import com.food.ordering.system.kafka.producer.exception.KafkaProducerException;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    @Autowired
    public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, K key, V message) {
        log.info("Sending message: {} to topic: {}", message, topicName);

        CompletableFuture<SendResult<K, V>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);

        kafkaResultFuture.whenComplete((successResult, exception) -> {
            if (exception == null) {
                handleSuccess(key, message, successResult);
            } else {
                handleFailure(key, message, topicName, exception);
            }
        });
    }

    private void handleSuccess(K key, V message, SendResult<K,V> successResult) {
        RecordMetadata recordMetadata = successResult.getRecordMetadata();
        log.info("Message sent successfully for Key: {} Message: {} produced to Topic: {} Partition: {} Offset: {} Timestamp:{}",
                key, message, recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp());
    }

    private void handleFailure(K key, V message, String topicName, Throwable exception) {
        log.error("Error on kafka producer while sending the message with Key: {} Message: {} Topic: {} Exception: {}",
                key, message, topicName, exception.getMessage());
        throw new KafkaProducerException(exception.getMessage());
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }
}
