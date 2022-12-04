package com.food.ordering.system.payment.service.domain.valueobject;

import com.food.ordering.system.domain.valueobjects.BaseId;

import java.util.UUID;

public class PaymentId extends BaseId<UUID> {
    public PaymentId(UUID value) {
        super(value);
    }
}
