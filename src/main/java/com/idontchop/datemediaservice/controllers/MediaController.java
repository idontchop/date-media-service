package com.idontchop.datemediaservice.controllers;

import java.io.IOException;
import java.time.Duration;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idontchop.datemediaservice.dtos.Crop;
import com.idontchop.datemediaservice.dtos.MediaDataDto;
import com.idontchop.datemediaservice.entities.Media;
import com.idontchop.datemediaservice.services.DataApiService;
import com.idontchop.datemediaservice.services.ImageService;
import com.idontchop.datemediaservice.services.MediaService;

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
	ImageService imageService;
	
	@Autowired
	MediaService mediaService;
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	Validator validator;
	
	@PostMapping ( "/media" )
	public Media newMedia (
			@RequestParam(name = "file", required = true) MultipartFile file,
			@RequestParam(name = "media", required = true) String mediaJson,
			@RequestParam(name = "crop", required = false) String cropJson) throws IOException {
		
		if (file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		// do crop, set defaults
		Crop crop = null;
		if ( cropJson != null ) {
			crop = mapper.readValue(cropJson, Crop.class);
			validator.validate(crop);
		} 
		
		byte[] newFile = imageService.cropAndResizeImage(file, crop);		// crop and Resize		
		
		// Get new media
		Media newMedia = new Media();
		if ( mediaJson != null ) {
			newMedia = mapper.readValue(mediaJson, Media.class);
			validator.validate(newMedia);
		} else throw new ResponseStatusException ( HttpStatus.BAD_REQUEST,"Unable to parse Media");
		
		MediaDataDto dataDto = dataApiService.saveImage(newMedia.getOwner(), newFile)
				.timeout(Duration.ofMillis(500L)).block();
		
		newMedia.setDataId(dataDto.getId());
		
		return mediaService.saveMedia(newMedia);
		
	}

}
