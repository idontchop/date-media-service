package com.idontchop.datemediaservice.controllers.kafka;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;

import com.idontchop.datemediaservice.entities.Like;
import com.idontchop.datemediaservice.services.LikeService;
import com.lovemire.messageLibrary.config.enums.LoveMireEvents;


public class LikeListener {

	@Autowired
	LikeService likeService;
	
	Logger logger = LoggerFactory.getLogger(LikeListener.class);
	
	@KafkaListener( topics = LoveMireEvents.Topics.LIKE,
			groupId = "LikeService",
			properties = {ConsumerConfig.AUTO_OFFSET_RESET_CONFIG + ":earliest"},
			containerFactory = "likeListenerFactory")
	public void consumeNotification ( Like like, @Header("event-type") String eventType )
			throws IOException {
		
		if ( eventType.equals("likeInValid")) {
			likeService.deleteLike(like.getId());
			logger.info(like.getId() + "|" + like.getOwner() + "|" + like.getMediaOwner() + " returned as Invalid");
		}
		
	}
}
