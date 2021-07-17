package com.idontchop.datemediaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lovemire.messageLibrary.config.EnableMessageLibrary;

@SpringBootApplication
@EnableMessageLibrary
public class DateMediaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DateMediaServiceApplication.class, args);
	}

}
