package com.idontchop.datemediaservice.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

@Entity
public class HiddenRequirements {

	public HiddenRequirements () {}
	
	@Id
	@GeneratedValue ( strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@NotEmpty
	@JsonIgnore
	private String owner;
	
	@OneToOne
	@JoinColumn
	@JsonIgnore
	private Media hiddenMedia;
	
	// Contains list of Ids that are used to determine if score reached.
	// This will usually be media in previous post but doesn't have to be
	@JsonIgnore
	@OneToMany
	private List<Media> referencedMedia = new ArrayList<>();
	
	private int requiredScore;

	// Other tags may require user to be a connection or TODO
	private boolean requiredConnection = false;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<Media> getReferencedMedia() {
		return referencedMedia;
	}

	public void setReferencedMedia(List<Media> referencedMedia) {
		this.referencedMedia = referencedMedia;
	}

	public int getRequiredScore() {
		return requiredScore;
	}

	public void setRequiredScore(int requiredScore) {
		this.requiredScore = requiredScore;
	}

	public boolean isRequiredConnection() {
		return requiredConnection;
	}

	public void setRequiredConnection(boolean requiredConnection) {
		this.requiredConnection = requiredConnection;
	}

	public List<Long> getMediaReferences () {
		return referencedMedia.stream().map( m -> m.getId()			
		).collect(Collectors.toList());
	}

	@JsonIgnore
	public Media getHiddenMedia() {
		return hiddenMedia;
	}

	public void setHiddenMedia(Media hiddenMedia) {
		this.hiddenMedia = hiddenMedia;
	}
	
	
}
