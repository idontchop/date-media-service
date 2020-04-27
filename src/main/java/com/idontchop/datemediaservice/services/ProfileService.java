package com.idontchop.datemediaservice.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idontchop.datemediaservice.dtos.ProfileMediaDto;
import com.idontchop.datemediaservice.dtos.UserProfileDto;

@Service
public class ProfileService {
	
	@Autowired
	MediaService mediaService;
	
	public List<UserProfileDto> findProfilesInList ( List<String> names ) {
		
		
		return names.stream().map ( name -> {
			
			List<ProfileMediaDto> media = mediaService.getProfileMediaByOwner(name);
			UserProfileDto dto = new UserProfileDto();
			dto.setUsername(name);
			dto.setMedia(media);
			return dto;
		})
		.collect(Collectors.toList());
		
	}

}
