package com.idontchop.datemediaservice.dtos;

/**
 * Returns with /likes endpoint
 * Calculates total likes for a media.
 * 
 * @author nathan
 *
 */
public interface LikesByMedia {

	long getId();
	long getCount();
	long getCost();
	/*
	private long mediaId;
	private long likes;
	public long getMediaId() {
		return mediaId;
	}
	public void setMediaId(long mediaId) {
		this.mediaId = mediaId;
	}
	public long getLikes() {
		return likes;
	}
	public void setLikes(long likes) {
		this.likes = likes;
	}
	*/
	
}
