package com.idontchop.datemediaservice.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity (name = "user_like")
public class Like {
	
	public Like () {}
	
	@Id
	@GeneratedValue ( strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@ManyToOne ( fetch = FetchType.EAGER)
	@JoinColumn (name = "media_id")
	private Media media;
	
	@NotNull
	@ManyToOne ( fetch = FetchType.EAGER)
	@JoinColumn (name = "like_type_id")
	private LikeType likeType;
	
	@NotEmpty
	private String owner;
	
	private Date created = new Date();
	
	public Like (String owner, LikeType likeType, Media media) {
		this.owner = owner;
		this.likeType = likeType;
		this.media = media;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public LikeType getLikeType() {
		return likeType;
	}

	public void setLikeType(LikeType likeType) {
		this.likeType = likeType;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	
	
}
