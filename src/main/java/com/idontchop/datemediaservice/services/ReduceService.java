package com.idontchop.datemediaservice.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idontchop.datemediaservice.repositories.MediaRepository;

@Service
public class ReduceService {
	
	@Autowired
	MediaRepository mediaRepository;
	
	/**
	 * Reduces the list of potentials to only potentials with a media image.
	 * 
	 * @param potentials
	 * @return
	 */
	public Set<String> reduceHasMedia ( List<String> potentials ) {
		
		return mediaRepository.findAllByOwnerIn(potentials)
				.stream()
				.filter(media -> media.isApproved())
				.map ( media -> media.getOwner())
				.collect(Collectors.toSet());
		
	}

}
