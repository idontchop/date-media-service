package com.idontchop.datemediaservice.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Media {
	
	public Media() {}
	
	@Id
	@GeneratedValue ( strategy = GenerationType.IDENTITY)
	private long id;
	
	private String owner;

	@ManyToOne ( fetch = FetchType.EAGER)
	@JoinColumn (name = "category_id")
	private MediaCategory category;
	
	public enum Display {
		PROFILE,		// All
		PROFILEONLY,	// Only on profile
		FEED			// Only displays on feed		
	}
	
	private Display display 	= Display.PROFILE;
	
	private int priority;			// preference in user profile / feed
	
	private String description;		// user supplied
	
	private Date created = new Date();
	
	private String dataId;			// id on data server

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return owner;
	}

	public void setUsername(String username) {
		this.owner = username;
	}

	public MediaCategory getMediaType() {
		return category;
	}

	public void setMediaType(MediaCategory mediaType) {
		this.category = mediaType;
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getHref() {
		return dataId;
	}

	public void setHref(String href) {
		this.dataId = href;
	}
	
	
}
