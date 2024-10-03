package com.example.JustGetStartedBackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JustGetStartedBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(JustGetStartedBackEndApplication.class, args);
	}

}
