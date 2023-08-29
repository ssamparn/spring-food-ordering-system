package com.food.ordering.system.saga;

public enum SagaStatus {
    // Holds the SAGA Status in the Outbox table.
    STARTED, FAILED, SUCCEEDED, PROCESSING, COMPENSATING, COMPENSATED
}
