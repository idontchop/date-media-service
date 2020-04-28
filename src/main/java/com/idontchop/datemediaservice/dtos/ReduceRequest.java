package com.idontchop.datemediaservice.dtos;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReduceRequest {

	@NotBlank
	String name;
	
	// This list is some potential options the api has considered.
	// Using the name, the service will check the potentials against the interestedins
	@NotEmpty(message = "Need at least one potential")
	List<String> potentials;
	
	private int mediaCount = 1;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPotentials() {
		return potentials;
	}

	public void setPotentials(List<String> potentials) {
		this.potentials = potentials;
	}

	public int getMediaCount() {
		return mediaCount;
	}

	public void setMediaCount(int mediaCount) {
		this.mediaCount = mediaCount;
	}
	
	
}
