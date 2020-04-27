package com.idontchop.datemediaservice.dtos;

import java.util.List;

/**
 * Just a wrapper with username and media. This is necessary so search-service knows
 * where to save media.
 * 
 * @author nathan
 *
 */
public class UserProfileDto {

	private String username;
	
	private List<ProfileMediaDto> media;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<ProfileMediaDto> getMedia() {
		return media;
	}

	public void setMedia(List<ProfileMediaDto> media) {
		this.media = media;
	}
	
	
}
