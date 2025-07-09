package com.demo.customer.service;


import com.demo.customer.entity.Customer;
import com.demo.customer.repository.CustomerRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final MeterRegistry meterRegistry;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, MeterRegistry meterRegistry) {
        this.customerRepository = customerRepository;
        this.meterRegistry = meterRegistry;
    }
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(UUID id) {
        log.info("Fetching customer with id: {}", id);
        return customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer not found with id: " + id));
    }

    public Customer createCustomer(Customer customer) {
        log.info("Creating new customer: {}", customer);
        meterRegistry.counter("customer.created.count").increment();
        customer.setId(UUID.randomUUID());
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(UUID id, Customer customerDetails) {
        Customer customer = getCustomerById(id);
        customer.setFirstName(customerDetails.getFirstName());
        customer.setMiddleName(customerDetails.getMiddleName());
        customer.setLastName(customerDetails.getLastName());
        customer.setEmailAddress(customerDetails.getEmailAddress());
        customer.setPhoneNumber(customerDetails.getPhoneNumber());
        log.info("Updating customer with id: {} with details: {}", id, customerDetails);
        return customerRepository.save(customer);
    }

    public void deleteCustomer(UUID id) {
        log.info("Deleting customer with id: {}", id);
        meterRegistry.counter("customer.deleted.count").increment();
        // Check if the customer exists before attempting to delete
        if (!customerRepository.existsById(id)) {
            throw new NoSuchElementException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
}