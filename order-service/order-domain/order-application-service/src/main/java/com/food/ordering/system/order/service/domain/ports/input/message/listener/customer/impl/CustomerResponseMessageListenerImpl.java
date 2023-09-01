package com.food.ordering.system.order.service.domain.ports.input.message.listener.customer.impl;

import com.food.ordering.system.order.service.domain.dto.message.CustomerModel;
import com.food.ordering.system.order.service.domain.entities.Customer;
import com.food.ordering.system.order.service.domain.exceptions.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.customer.CustomerResponseMessageListener;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerResponseMessageListenerImpl implements CustomerResponseMessageListener {

    private final OrderDataMapper orderDataMapper;
    private final CustomerRepository customerRepository;

    public CustomerResponseMessageListenerImpl(OrderDataMapper orderDataMapper, CustomerRepository customerRepository) {
        this.orderDataMapper = orderDataMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public void customerCreated(CustomerModel customerModel) {
        Customer customer = orderDataMapper.customerModelToCustomer(customerModel);
        Customer savedCustomer = customerRepository.save(customer);

        if (savedCustomer == null) {
            log.error("Customer could not be created in order database with id: {}", customerModel.getId());
            throw new OrderDomainException("Customer could not be created in order database with id: " + customerModel.getId());
        }
        log.info("Customer is created in order database with id: {}", customer.getId().getValue());
    }
}
