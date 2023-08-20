package com.food.ordering.system.kafka.consumer.service;

import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;

public interface KafkaConsumer<T extends SpecificRecordBase> {
    void receive(List<String> keys, List<T> messages, List<Integer> partitions, List<Long> offsets);
}
