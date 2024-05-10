package com.youtubeMiner.youtubeMiner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class YoutubeMinerApplication {

	public static void main(String[] args) {
		//if the application start successfully priint a message

		SpringApplication.run(YoutubeMinerApplication.class, args);
		if(args.length == 0) {
			System.out.println("Application started successfully");

		}
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
