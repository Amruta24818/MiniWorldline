package com.cybage.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = "com.example")
@ComponentScan("com.example.utils")
@ComponentScan(basePackages="com.example.model")
@ComponentScan(basePackages="com.example.controller")
@ComponentScan(basePackages="com.example.service")
public class PaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceApplication.class, args);
		
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
}

}