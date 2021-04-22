package com.idontchop.datemediaservice.services;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;
import com.idontchop.datemediaservice.dtos.MessageNotificationDto;
import com.idontchop.datemediaservice.entities.Like;

/**
 * Responsible for sending messages via kafka. Notifications for likes.
 * 
 * @author nathan
 *
 */
@Service
public class MessageService {
	
	Logger logger = LoggerFactory.getLogger(MessageService.class);
	
	private final String topic = "Likes";
	
	@Value("${spring.kafka.consumer.bootstrap-servers}")
	private String bootstrap;
		
	public Like sendLike(Like like) {
		
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
		
		// create producer
		KafkaProducer<String, Like> producer = new KafkaProducer<>(properties);
		
		// record
		ProducerRecord<String, Like> record = new ProducerRecord<>(topic, like.getOwner(), like);
		
		// send
		producer.send(record, (recordMetaData, e) -> {
			
			logger.info("(" + topic + ":" +
					recordMetaData.offset() + "-" +
					"Partition: " + recordMetaData.partition() +
					") Like Message sent. " + record.value().getOwner());

		});
		
		producer.close();
		
		return like; // return like unmodified, likely part of save pipeline
	}
	
	/**
	 * Sends the Like as it was requested and saved. This is not validated and could be rejected
	 * by the pay service.
	 * 
	 * Topic: 	Like
	 * key:		Like-Requested
	 * 
	 * Miniscule chance a record could be saved and unsent if kafka was down.
	 * 
	 * @param like
	 * @return
	 */
	public Like sendRawLike(Like like) {
		
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
		
		// create producer
		KafkaProducer<String, Like> producer = new KafkaProducer<>(properties);
		
		// record
		ProducerRecord<String, Like> record = new ProducerRecord<>(topic, key, like);
		
		// send
		producer.send(record, (recordMetaData, e) -> {
			
			logger.info("(" + topic + ":" +
					recordMetaData.offset() + "-" +
					"Partition: " + recordMetaData.partition() +
					") Like Message sent. " + record.value().getOwner() + "->" +
					record.value().getMediaId());
		});
		
		producer.close();
		
		return like; // return like unmodified, likely part of save pipeline
		
	}

}
