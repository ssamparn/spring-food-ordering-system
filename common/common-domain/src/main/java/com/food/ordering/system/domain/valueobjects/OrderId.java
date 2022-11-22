package com.food.ordering.system.domain.valueobjects;

import java.util.UUID;

public class OrderId extends BaseId<UUID> {

    public OrderId(UUID value) {
        super(value);
    }
}
