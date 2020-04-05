package com.idontchop.datemediaservice.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idontchop.datemediaservice.dtos.MediaDataDto;
import com.idontchop.datemediaservice.entities.Media;
import com.idontchop.datemediaservice.services.DataApiService;

import reactor.core.publisher.Mono;

/**
 * Handles media endpoints including when a file is uploaded.
 * 
 * @author micro
 *
 */
@RestController
public class MediaController {
	
	@Autowired
	DataApiService dataApiService;
	
	@Autowired
	ObjectMapper mapper;
	
	@PostMapping ( "/media" )
	public Mono<Media> newMedia (
			@RequestParam(name = "file", required = true) MultipartFile file,
			@RequestParam(name = "media", required = true) String mediaJson) throws IOException {
		
		if (file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		if ( mediaJson != null ) {
			mapper.readValue(mediaJson, Media.class);
		} else {
			throw new ResponseStatusException ( HttpStatus.BAD_REQUEST,"Unable to parse Media");
		}
		
		return dataApiService.saveImage(owner, file.getBytes());
		
	}

}
