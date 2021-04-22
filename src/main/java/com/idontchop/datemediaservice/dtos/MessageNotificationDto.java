package com.idontchop.datemediaservice.dtos;

import java.time.LocalDateTime;

import com.idontchop.datemediaservice.entities.Like;
import com.idontchop.datemediaservice.services.MessageService;

/**
 * Used to build kafka message.
 * 
 * @author nathan
 *
 */
public class MessageNotificationDto {
	
	private String fromId;
	private String toId;
	private String typeName;
	private String referenceId;
	private boolean seen = false;
	private LocalDateTime seenTime;
	private LocalDateTime created = LocalDateTime.now();
	
	public MessageNotificationDto fromLike(Like like) {
		MessageNotificationDto dto = new MessageNotificationDto();
		dto.fromId = like.getOwner();
		dto.typeName = like.getLikeType().getName();
		dto.referenceId = String.valueOf(like.getMedia().getId());
		dto.toId = like.getMedia().getOwner();
		return dto;
	}
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public boolean isSeen() {
		return seen;
	}
	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	public LocalDateTime getSeenTime() {
		return seenTime;
	}
	public void setSeenTime(LocalDateTime seenTime) {
		this.seenTime = seenTime;
	}
	public LocalDateTime getCreated() {
		return created;
	}
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
	

}
