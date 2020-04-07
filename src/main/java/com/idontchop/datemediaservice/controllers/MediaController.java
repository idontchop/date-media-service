package com.idontchop.datemediaservice.controllers;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
 * /media  {POST, PUT, DELETE}
 * The media endpoint requires a multipart file with media json and crop json as parameters.
 * A call here without a pathvariable always results in a call to the media-data service
 * 
 *  /media/{id}  {POST, PUT, GET}
 *  
 *  This call only modifies the media information on the service. The file data is not modified.
 *  The owner in the attached media must match the owner stored already in the media
 *  
 *  /media/owner/{owner} {GET}
 *  
 *  Gets a list of all the owner's media
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
	
	@GetMapping ( value = "/media/{id}")
	public Media getMedia (
			@PathVariable ( name = "id", required = true) long id) {
		return mediaService.getMedia(id);
	}
	
	@RequestMapping ( value = "/media/{id}", method = {RequestMethod.POST, RequestMethod.PUT})
	public Media updateMedia (
			@PathVariable ( name = "id", required = true) long id,
			@Valid @RequestBody Media media) {
		return mediaService.updateMedia(media);
	}
	
	@GetMapping ( value = "/owner/{owner}")
	public List<Media> getMediaByOwner (
			@PathVariable ( name = "owner", required = true ) String owner) {
		return mediaService.getMediaByOwner(owner);
	}
	
	@ExceptionHandler (NoSuchElementException.class)
	@ResponseStatus ( value = HttpStatus.NOT_FOUND, reason = "Requested media not found" )
	public void noSuchElementExceptionHandler () {
		// TODO: log
	}
	
	/**
	 * Saves new media or updates existing.
	 * 
	 * If mediaJson argument contains an Id, it will attempt to update. If the Id
	 * is missing or invalid, a new media will be saved.
	 * 
	 * If the Id is found, service will make every attempt to save the new media to 
	 * data service, replacing existing or saving new data.
	 * 
	 * @param file
	 * @param mediaJson
	 * @param cropJson
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping ( value = "/media", method = {RequestMethod.POST, RequestMethod.PUT} )
	public Media newMedia (
			@RequestParam(name = "file", required = true) MultipartFile file,
			@RequestParam(name = "media", required = true) String mediaJson,
			@RequestParam(name = "crop", required = false) String cropJson,
			HttpServletRequest request) throws IOException {
		
		if (file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		// do crop, set defaults
		Crop crop = null;
		if ( cropJson != null ) {
			crop = mapper.readValue(cropJson, Crop.class);
			validator.validate(crop);
		} 
		
		// crop and Resize, convert to byte[]
		byte[] newFile = imageService.cropAndResizeImage(file, crop);				
		
		// Get new media
		Media newMedia = new Media();
		if ( mediaJson != null ) {
			newMedia = mapper.readValue(mediaJson, Media.class);
			validator.validate(newMedia);
		} else throw new ResponseStatusException ( HttpStatus.BAD_REQUEST,"Unable to parse Media");
			
		if ( request.getMethod().equals("POST") ) {	
			return mediaService.saveMedia(newMedia, newFile);
		} else { // PUT			
			return mediaService.replaceMedia(newMedia, newFile);
		}
	}
	
	/**
	 * Owner must be supplied here for verification.
	 * 
	 * @param id
	 * @throws NoSuchElementException
	 * @throws IOException
	 */
	@DeleteMapping ( value = "/media/{id}/{owner}" )
	public void deleteMedia (
			@PathVariable ( name = "id", required = true) long id,
			@PathVariable (name = "owner", required = true) String owner) throws NoSuchElementException, IOException {
			mediaService.deleteMedia(id,owner);
	}


}
