package com.example.api_calling_techniques;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiCallingTechniquesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCallingTechniquesApplication.class, args);
	}

}
