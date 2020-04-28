package com.idontchop.datemediaservice.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idontchop.datemediaservice.entities.Media;
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
	
	public Set<String> reduceHasMediaCount ( List <String> potentials, int mediaCount ) {
		
		List<String> mediaList = mediaRepository.findAllByOwnerIn(potentials)
				.stream()
				.filter( Media::isApproved )
				.map( media -> media.getOwner() )
				.collect( Collectors.toList() );
			
		return mediaList
			.stream()
			.collect( Collectors.groupingBy ( Function.identity(), Collectors.counting() ) )
			.entrySet()
			.stream()
			.filter( p -> p.getValue() >= mediaCount)
			.map( Map.Entry :: getKey )
			.collect( Collectors.toSet() );
			
	}
	
	/**
	 * Returns potentials not found.
	 * 
	 * @param potentials
	 * @return
	 */
	public List<String> reduceNoMedia ( List<String> potentials ) {
		
		potentials.removeAll(reduceHasMedia(potentials));
		return potentials;
		
	}

}
