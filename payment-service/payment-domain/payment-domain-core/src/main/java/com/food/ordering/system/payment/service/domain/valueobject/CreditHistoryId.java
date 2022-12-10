package com.food.ordering.system.payment.service.domain.valueobject;

import com.food.ordering.system.domain.valueobjects.BaseId;

import java.util.UUID;

public class CreditHistoryId extends BaseId<UUID> {
    public CreditHistoryId(UUID value) {
        super(value);
    }
}
