package com.food.ordering.system.kafka.producer.service.impl;

import com.food.ordering.system.kafka.producer.exception.KafkaProducerException;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
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
    public void send(String topicName, K key, V message, CompletableFuture<SendResult<K, V>> futureCallback) {
        log.info("Sending message: {} to topic: {}", message, topicName);

        CompletableFuture<SendResult<K, V>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
        kafkaResultFuture.whenComplete((successResult, exception) -> {
            if (exception == null) {
                handleSuccess(key, message, successResult);
            } else {
                handleFailure(key, message, exception);
            }
        });
    }

    private void handleSuccess(K key, V message, SendResult<K,V> successResult) {
        log.info("Message sent successfully for key: {}, message: {}, produced to partition: {}", key, message, successResult.getRecordMetadata().partition());
    }

    private void handleFailure(K key, V message, Throwable exception) {
        log.error("Error on kafka producer while sending the message with key: {}, message: {} and exception: {}", key, message, exception.getMessage());
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
