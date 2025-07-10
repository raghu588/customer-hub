package com.demo.customer.controller;


import com.demo.customer.entity.Customer;
import com.demo.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.MySQLDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        log.info("Fetching all customers");
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable UUID id) {
        try {
            log.info("Fetching customer with id: {}", id);
            return ResponseEntity.ok(customerService.getCustomerById(id));
        } catch (Exception e) {
            log.error("Customer not found with id: {} {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Customer createCustomer(@Valid @RequestBody Customer customer) {
        log.info("Creating new customer: {}", customer);
        return customerService.createCustomer(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable UUID id, @Valid @RequestBody Customer customerDetails) {
        try {
            log.info("Updating customer with id: {} with details: {}", id, customerDetails);
            return ResponseEntity.ok(customerService.updateCustomer(id, customerDetails));
        } catch (Exception e) {
            log.error("Error updating customer with id: {} {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        try {
            log.info("Deleting customer with id: {}", id);
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting customer with id: {} {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
