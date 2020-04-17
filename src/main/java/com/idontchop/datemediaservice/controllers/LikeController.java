package com.idontchop.datemediaservice.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.idontchop.datemediaservice.dtos.RestMessage;
import com.idontchop.datemediaservice.entities.Like;
import com.idontchop.datemediaservice.services.LikeService;

/**
 * Handles Likes and LikeType.
 * 
 * For now LikeTypes aren't changable here, since we only have one.
 * 
 *  /like/{owner}/{mediaid}/{likeType}  POST
 *  
 *  Owner is typically required for verification
 *  
 *  /likeCount/{owner}/{mediaId}
 *  
 *  
 *  /like/{owner}/{id}		GET DELETE
 *  /like/{owner}			GET
 *  
 *  likes can't be modified, only added or deleted.
 *  
 * @author micro
 *
 */
@RestController
public class LikeController {
	
	Logger logger = LoggerFactory.getLogger(LikeController.class);
	
	@Autowired
	LikeService likeService;
	
	
	@GetMapping ("/likeCount/{mediaId}")
	public RestMessage getLikeCount(
			@PathVariable( name = "mediaId", required = true) long mediaId) {
		
		long likeCount = likeService.getLikeCount(mediaId);
		
		return RestMessage.build(String.valueOf(likeCount))
				.add("likeCount", String.valueOf(likeCount))
				.add("likes", String.valueOf(likeCount));
	}
	
	@GetMapping ("/like/{owner}/{mediaId}")
	public List<Like> getLike (
			@PathVariable( name = "owner", required = true) String owner,
			@PathVariable( name = "mediaId", required = false ) Optional<Long> mediaId  ) {
		
		// find specific
		if ( mediaId.isPresent() ) {
			
			return List.of( likeService.getLikeByOwnerAndId(owner, mediaId.get()));
			
		} else { // find all by owner
			
			return likeService.getLikeListByOwner(owner);
		}
		
	}
	
	@PostMapping ("/like/{owner}/{mediaId}/{likeType}")
	public Like newLike (
			@PathVariable( name = "owner", required = true) String owner,
			@PathVariable( name = "mediaId", required = true) long mediaId,
			@PathVariable( name = "likeType", required = true) String likeType) {
		
		// so likeType can be passed as name or id
		long likeTypeId;
		try {
			likeTypeId = Long.parseLong(likeType);
		} catch (NumberFormatException e) {
			likeTypeId = likeService.findLikeTypeId(likeType);
		}
		
		return likeService.newLike(owner, likeTypeId, mediaId);
	}
	
	@DeleteMapping ( "/like/{owner}/{mediaId}")
	public void deleteLike (
			@PathVariable( name = "owner", required = true) String owner,
			@PathVariable( name = "mediaId", required = true) long mediaId) {
		
		likeService.deleteLike(owner, mediaId);
	}
	
	@ExceptionHandler (NoSuchElementException.class)
	@ResponseStatus ( value = HttpStatus.NOT_FOUND, reason = "Requested like not found" )
	public void noSuchElementExceptionHandler () {
		logger.debug("Caught NoSuchElement in Like Controller.");
		// TODO: more info?
	}

}
