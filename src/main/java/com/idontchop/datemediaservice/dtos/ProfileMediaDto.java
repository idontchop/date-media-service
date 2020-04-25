package com.idontchop.datemediaservice.dtos;

import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import com.idontchop.datemediaservice.entities.MediaCategory;
import com.idontchop.datemediaservice.entities.Media;
import com.idontchop.datemediaservice.entities.Media.Display;
import com.idontchop.datemediaservice.entities.Media.Validate;
import com.sun.istack.NotNull;

/**
 * Returned at the /profile endpoint
 * 
 * This removes all "non-public" info on the media.
 * 
 * @author nathan
 *
 */
public class ProfileMediaDto {


	
	@NotNull
	@NotEmpty
	private String owner;

	@NotNull
	@ManyToOne ( fetch = FetchType.EAGER)
	@JoinColumn (name = "category_id")
	private MediaCategory category;
		
	private int priority;			// preference in user profile / feed
	
	private String description = "";		// user supplied
	
	
	private String dataId;			// id on data server


	public static ProfileMediaDto from ( Media media ) {
		
		ProfileMediaDto dto = new ProfileMediaDto();
		dto.owner = media.getOwner();
		dto.category = media.getCategory();
		dto.priority = media.getPriority();
		dto.description = media.getDescription();
		dto.dataId = media.getDataId();
		
		return dto;
		
	}
	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public MediaCategory getCategory() {
		return category;
	}


	public void setCategory(MediaCategory category) {
		this.category = category;
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


	public String getDataId() {
		return dataId;
	}


	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	
	
}
