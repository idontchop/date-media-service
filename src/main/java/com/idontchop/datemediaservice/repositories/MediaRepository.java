package com.idontchop.datemediaservice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.idontchop.datemediaservice.entities.Media;

public interface MediaRepository extends CrudRepository <Media, Long> {
	
	List<Media> findAllByOwner(String owner);
	Optional<Media> findByIdAndOwner(long id, String owner);	// used for owner verification

}
