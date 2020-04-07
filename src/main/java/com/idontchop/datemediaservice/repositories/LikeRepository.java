package com.idontchop.datemediaservice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.idontchop.datemediaservice.entities.Like;

public interface LikeRepository extends CrudRepository<Like,Long> {

	List<Like> findAllByOwner(String owner);
	Optional<Like> findByOwnerAndMedia_Id(String owner, long mediaId);
	List<Like> findAllByMedia_Id(long id);
	Long countByMedia_Id(long id);
}
