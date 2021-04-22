package com.idontchop.datemediaservice.controllers.kafka;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.idontchop.datemediaservice.entities.Like;
import com.idontchop.datemediaservice.services.LikeService;


public class LikeListener {

	@Autowired
	LikeService likeService;
	
	Logger logger = LoggerFactory.getLogger(LikeListener.class);
	
	@KafkaListener( topics = "Like",
			groupId = "LikeService",
			containerFactory = "likeListenerFactory")
	public void consumeNotification ( Like like ) throws IOException {
		
	}
}
