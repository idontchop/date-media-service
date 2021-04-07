package com.idontchop.datemediaservice.dtos;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class RestWrapper {
	
	private HashMap<String, Object> message = new HashMap<>();
	
	public RestWrapper() {}
	public RestWrapper (Object value) {
		message.put("data", value);
	}
	public RestWrapper (String key, Object value) {
		message.put(key, value);
	}
	
	public static RestWrapper build ( Object value ) {
		RestWrapper rm = new RestWrapper(value);		
		return rm;
	}
	
	public static RestWrapper build ( String key, Object value ) {
		RestWrapper rm = new RestWrapper( key, value);
		return rm;
	}
	
	public RestWrapper add(String key, Object value) {
		message.put(key,value);
		return this;
	}
	
	/**
	 * Consumes another RestWrapper.
	 * This OVERWRITES all current data.
	 * 
	 * @param rw
	 * @return
	 */
	public RestWrapper consume ( RestWrapper rw ) {
		message.putAll(rw.getMessages());
		return this;
	}
	
	/**
	 * Adds another RestWrapper, will not overwrite any data.
	 * 
	 * @param rw
	 * @return
	 */
	public RestWrapper append ( RestWrapper rw ) {
		
		rw.getMessages().keySet().forEach( k -> {
			if ( this.message.containsKey(k)) {
				this.message.put(k + " " + LocalDateTime.now().toString(), rw.getMessage().get(k));
			} else {
				this.message.put(k, rw.getMessage().get(k));
			}
		});
		
		return this;
	}
	
	@JsonAnyGetter
	public HashMap<String, Object> getMessage() {
		return message;
	}
	
	@JsonAnySetter
	public void setMessage(HashMap<String, Object> message) {
		this.message = message;
	}

	@JsonIgnore
	public HashMap<String, Object> getMessages() {
		return message;
	}
	
}
