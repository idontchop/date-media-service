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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sun.istack.NotNull;

@Entity
@JsonInclude(Include.NON_NULL)
public class Media {
	
	public Media() {}
	
	@Id
	@GeneratedValue ( strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@NotEmpty
	private String owner;

	@NotNull
	@ManyToOne ( fetch = FetchType.EAGER)
	@JoinColumn (name = "category_id")
	private MediaCategory category;
	
	public enum Display {
		PROFILE,		// All
		PROFILEONLY,	// Only on profile
		FEED			// Only displays on feed		
	}
	
	private Display display 	= Display.PROFILE;
	
	public enum Validate {
		APPROVED,
		QUEUED,
		REJECTED
	}
	
	private Validate validate = Validate.QUEUED;
	
	private boolean active = true;
	
	private int priority;			// preference in user profile / feed
	
	private String description = "";		// user supplied
	
	private Date created = new Date();
	
	private String dataId;			// id on data server
	
	@ManyToOne
	private HiddenRequirements hidden = null;

	/**
	 * Receives a media class and updates only the metadata:
	 * 
	 * category, display, priority, description
	 * 
	 * @param newMedia
	 */
	public void updateMedia (Media newMedia) {
		
		this.category = newMedia.getCategory();
		this.display = newMedia.getDisplay();
		this.priority = newMedia.getPriority();
		this.description = newMedia.getDescription();
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getOwner() {
		return owner;
	}

	public String getUsername() {
		return owner;
	}

	public void setUsername(String username) {
		this.owner = username;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}

	public MediaCategory getCategory() {
		return category;
	}

	public void setCategory(MediaCategory mediaType) {
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

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public Validate getValidate() {
		return validate;
	}

	public void setValidate(Validate validate) {
		this.validate = validate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isValidated () {
		return validate == Validate.APPROVED;
	}

	/**
	 * Returns true if this media is active, validated, and profile.
	 * @return
	 */
	public boolean isProfileApproved() {
		return ( active && (display == Display.PROFILE || display == Display.PROFILEONLY )
				&& validate == Validate.APPROVED);
	}

	/**
	 * Returns true if media can be shown.
	 * @return
	 */
	public boolean isApproved() {
		return ( active && isValidated());
	}
	
	public boolean isFeedApproved() {
		return ( active && isValidated() && display == Display.FEED );
	}

	public HiddenRequirements getHidden() {
		return hidden;
	}

	public void setHidden(HiddenRequirements hidden) {
		this.hidden = hidden;
	}
	
	
}
