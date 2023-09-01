package com.food.ordering.system.payment.service.domain.config;

import com.food.ordering.system.payment.service.domain.PaymentDomainService;
import com.food.ordering.system.payment.service.domain.PaymentDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentServiceBeanConfiguration {

    @Bean
    public PaymentDomainService orderDomainService() {
        return new PaymentDomainServiceImpl();
    }
}
