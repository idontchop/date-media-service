package com.idontchop.datemediaservice.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.idontchop.datemediaservice.entities.Like;

@EnableKafka
@Configuration
public class KafkaConfig {
	
	Logger logger = LoggerFactory.getLogger(KafkaConfig.class);
	
	@Value("${spring.kafka.consumer.bootstrap-servers}")
	private String bootstrapServers;
	
	@Value("${spring.kafka.consumer.group-id")
	private String groupId;

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Like>
		likeListenerFactory() {
		
		Map<String,Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, "false");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.lovemire.payservice.dtos.Like");

	    ConcurrentKafkaListenerContainerFactory<String, Like> factory =
	      new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(new DefaultKafkaConsumerFactory<String,Like>(props));
	    /*JsonDeserializer<NotificationDto> deserializer = new JsonDeserializer<>(NotificationDto.class);
	    deserializer.setRemoveTypeHeaders(false);
	    deserializer.addTrustedPackages("*");
	    deserializer.setUseTypeMapperForKey(true);
	    factory.setMessageConverter(deserializer);*/
	    factory.setRecordFilterStrategy( // filters out events for listener
	      record -> {
	    	  boolean hasEventType = false;
	    	  for (Header header : record.headers()) {
	    		  if(header.key().equals("event-type"))
	    			  hasEventType = true;
	    	  }
	      return record.key()==null || !hasEventType;
	      });
	    return factory;
	}
	
	/*******************************
	 * Producer
	 * *****************************/
	
	
    @Bean
    public ProducerFactory<String, Like> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
          ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
          bootstrapServers);
        configProps.put(
          ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
          StringSerializer.class);
        configProps.put(
          ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
          JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Like> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
	 

}
