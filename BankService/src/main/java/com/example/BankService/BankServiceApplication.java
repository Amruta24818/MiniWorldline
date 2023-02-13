package com.example.BankService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.example")
@ComponentScan("com.example.databaseConnection")
@ComponentScan(basePackages="com.example.model")
@ComponentScan(basePackages="com.example.controller")
@ComponentScan(basePackages="com.example.service")
@EnableAutoConfiguration
public class BankServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankServiceApplication.class, args);
	}

}
