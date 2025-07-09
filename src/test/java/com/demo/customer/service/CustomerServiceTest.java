package com.demo.customer.service;

import com.demo.customer.entity.Customer;
import com.demo.customer.repository.CustomerRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private MeterRegistry meterRegistry;
    private Counter mockCounter;

    @BeforeEach
    void setUp() {
        meterRegistry = Mockito.mock(MeterRegistry.class);
        mockCounter = Mockito.mock(Counter.class);
         meterRegistry = mock(MeterRegistry.class);
        customerService = new CustomerService(customerRepository,meterRegistry); // Inject the mock
    }

    @Test
    void getAllCustomers_returnsList() {
        List<Customer> customers = List.of(new Customer());
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();
        assertEquals(1, result.size());
    }

    @Test
    void getCustomerById_found() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(id);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(id);
        assertEquals(id, result.getId());
    }

    @Test
    void getCustomerById_notFound() {
        UUID id = UUID.randomUUID();
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> customerService.getCustomerById(id));
    }

    @Test
    void createCustomer_success() {
        Customer customer = new Customer();
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(meterRegistry.counter(Mockito.anyString(), Mockito.any(String[].class)))
                .thenReturn(mockCounter);

        Customer result = customerService.createCustomer(customer);

        // Verify that the counter method was called
        verify(meterRegistry).counter(Mockito.eq("customer.created.count"), Mockito.any(String[].class));
        // Verify that increment was called on the mock counter
        verify(mockCounter).increment();
        assertNotNull(result);
    }

    @Test
    void updateCustomer_success() {
        UUID id = UUID.randomUUID();
        Customer existing = new Customer();
        existing.setId(id);
        when(customerRepository.findById(id)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any(Customer.class))).thenReturn(existing);

        Customer details = new Customer();
        details.setFirstName("Updated");
        details.setLastName("Name");
        details.setEmailAddress("updated@example.com");

        Customer result = customerService.updateCustomer(id, details);
        assertEquals("Updated", result.getFirstName());
    }

    @Test
    void updateCustomer_notFound() {
        UUID id = UUID.randomUUID();
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> customerService.updateCustomer(id, new Customer()));
    }

    @Test
    void deleteCustomer_success() {
        UUID id = UUID.randomUUID();
        when(customerRepository.existsById(id)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(id);
        when(meterRegistry.counter(Mockito.anyString(), Mockito.any(String[].class)))
                .thenReturn(mockCounter);

        assertDoesNotThrow(() -> customerService.deleteCustomer(id));
    }

    @Test
    void deleteCustomer_notFound() {
        UUID id = UUID.randomUUID();
        when(customerRepository.existsById(id)).thenReturn(false);
        when(meterRegistry.counter(Mockito.anyString(), Mockito.any(String[].class)))
                .thenReturn(mockCounter);
        assertThrows(NoSuchElementException.class, () -> customerService.deleteCustomer(id));
    }
}
