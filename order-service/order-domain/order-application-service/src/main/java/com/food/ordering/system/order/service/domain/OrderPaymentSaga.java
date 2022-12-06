package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.events.EmptyEvent;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.events.OrderPaidEvent;
import com.food.ordering.system.saga.SagaStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class OrderPaymentSaga implements SagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {

    @Override
    @Transactional
    public OrderPaidEvent process(PaymentResponse data) {
        return null;
    }

    @Override
    @Transactional
    public EmptyEvent rollback(PaymentResponse data) {
        return null;
    }
}
