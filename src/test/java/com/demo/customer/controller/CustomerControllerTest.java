package com.demo.customer.controller;


import com.demo.customer.entity.Customer;
import com.demo.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)

class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCustomers_returnsList() throws Exception {
        List<Customer> customers = List.of(new Customer());
        Mockito.when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getCustomerById_found() throws Exception {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(id);
        Mockito.when(customerService.getCustomerById(id)).thenReturn(customer);

        mockMvc.perform(get("/api/customers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void getCustomerById_notFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(customerService.getCustomerById(id)).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/api/customers/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomer_success() throws Exception {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmailAddress("john@example.com");
        Mockito.when(customerService.createCustomer(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void updateCustomer_success() throws Exception {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName("Jane");
        Mockito.when(customerService.updateCustomer(eq(id), any(Customer.class))).thenReturn(customer);

        mockMvc.perform(put("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    void updateCustomer_notFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(customerService.updateCustomer(eq(id), any(Customer.class))).thenThrow(new NoSuchElementException());

        mockMvc.perform(put("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Customer())))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCustomer_success() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/customers/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCustomer_notFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.doThrow(new NoSuchElementException()).when(customerService).deleteCustomer(id);

        mockMvc.perform(delete("/api/customers/{id}", id))
                .andExpect(status().isNotFound());
    }
}