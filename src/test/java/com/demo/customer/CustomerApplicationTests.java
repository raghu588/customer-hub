package com.demo.customer;

import com.demo.customer.service.CustomerService;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerApplicationTests {
	@Autowired
	private MeterRegistry meterRegistry;

	@Autowired
	private CustomerService customerService;

	@Test
	void contextLoads() {
	}

}
