package com.idontchop.datemediaservice.dtos;

import java.util.Date;
import org.springframework.data.annotation.Id;

public class MediaDataDto {
	
	public MediaDataDto() {}
	
	@Id
	private String id;
	
	private String owner;
	
	private Date created = new Date();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	
}
