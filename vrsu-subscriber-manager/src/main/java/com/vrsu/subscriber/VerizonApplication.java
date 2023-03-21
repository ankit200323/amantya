package com.vrsu.subscriber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.google.gson.JsonParser;

@SpringBootApplication
@CrossOrigin(origins = "http://localhost:4200")
@EnableScheduling
@EnableTransactionManagement
@ComponentScan
public class VerizonApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(VerizonApplication.class, args);
	}
	@SuppressWarnings("deprecation")
	@Bean
	public JsonParser getInstance()
	{
		return new JsonParser();
	}
}