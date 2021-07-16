package com.idontchop.datemediaservice.config;

import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class DateConfiguration {
	
	  @Bean
	    public Validator validator() {

	        return new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
	    }
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		return mapper;
	}

}
