package com.food.ordering.system.payment.service.domain.valueobject;

import com.food.ordering.system.domain.valueobjects.BaseId;

import java.util.UUID;

public class CreditEntryId extends BaseId<UUID> {

    public CreditEntryId(UUID value) {
        super(value);
    }
}
