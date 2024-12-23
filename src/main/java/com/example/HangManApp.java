package com.example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example.controller", "com.example.config", "com.example.service", "com.example.repository","com.example.util","com.example.security" })
@EntityScan("com.example.model")
@EnableJpaRepositories(basePackages = "com.example.repository")
public class HangManApp extends SpringBootServletInitializer {

	public static void main(String[] args) {    
		SpringApplication.run(HangManApp.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(HangManApp.class);
	}

}
