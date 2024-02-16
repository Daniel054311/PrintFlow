package com.spring.printFlow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.spring.printFlow.controllers")
@ComponentScan("com.spring.printFlow.repository")
@ComponentScan("com.spring.printFlow.models")
@ComponentScan("com.spring.printFlow.dataBaseServices")
@ComponentScan("com.spring.printFlow.services")
@ComponentScan("com.spring.printFlow.config")
@SpringBootApplication

public class PrintFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrintFlowApplication.class, args);
	}

}
