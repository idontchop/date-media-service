package com.idontchop.datemediaservice.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import dtos.RestWrapper;
import com.idontchop.datemediaservice.entities.HiddenRequirements;
import com.idontchop.datemediaservice.services.HiddenRequirementsService;

@RestController
public class HiddenRequirementsController {
	
	@Autowired
	HiddenRequirementsService hiddenService;
	
	@GetMapping("/hidden/{id}")
	public HiddenRequirements getRequirementsByMediaId(
			@PathVariable(name="id") long mediaId) {
		
		return hiddenService.getRequirements(mediaId);
		
	}
	
	/**
	 * Api for requesting a token to access a hidden image.
	 * If the requirements are not met, will return the requirements and
	 * progress.
	 * 
	 * @param mediaId
	 * @return
	 */
	@GetMapping("/requestHidden/{id}")
	public RestWrapper requestHiddenAccessByMediaId(
			@PathVariable(name="id") long mediaId,
			Principal principal) {
		
		return hiddenService.requestHidden(mediaId, principal.getName());
		
	}
	
	@GetMapping("/requestHiddenByDataId/{id}")
	public RestWrapper requestHiddenAccessByMediaId(
			@PathVariable(name="id") String dataId,
			Principal principal) {
		
		return hiddenService.requestHidden(dataId, principal.getName());
		
	}
		
	

}
