package com.ashfaq.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ashfaq.concepts.dto"})
public class RestApiSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiSpringbootApplication.class, args);
		
	}

}
