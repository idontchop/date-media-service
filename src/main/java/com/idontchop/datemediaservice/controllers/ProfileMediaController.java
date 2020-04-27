package com.idontchop.datemediaservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.idontchop.datemediaservice.dtos.ProfileMediaDto;
import com.idontchop.datemediaservice.dtos.UserProfileDto;
import com.idontchop.datemediaservice.services.MediaService;
import com.idontchop.datemediaservice.services.ProfileService;

/**
 * Handles returning media for public calls.
 * 
 * Other users calling someone's profile.
 * 
 * @author nathan
 *
 */
@RestController
public class ProfileMediaController {

	@Autowired
	MediaService mediaService;
	
	@Autowired
	ProfileService profileService;
	
	/**
	 * Returns a public-info array of the owner's media.
	 * 
	 *  May return a zero length list.
	 *  
	 * @param owner
	 * @return
	 */
	@GetMapping ( "/profileMedia/{owner}")
	public List<ProfileMediaDto> getProfileMedia (
			@PathVariable(name = "owner", required = true) String owner) {
		
		return mediaService.getProfileMediaByOwner(owner);
		
	}
	
	@GetMapping ( "/api/profile/{names}")
	public List<UserProfileDto> getProfilesInList ( @PathVariable (name = "names", required = true) List<String> names) {
		return profileService.findProfilesInList(names);
	}
}
