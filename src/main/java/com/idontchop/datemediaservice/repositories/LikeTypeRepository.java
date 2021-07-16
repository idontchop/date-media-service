package com.idontchop.datemediaservice.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.idontchop.datemediaservice.entities.LikeType;

public interface LikeTypeRepository extends CrudRepository<LikeType,Long> {

	public Optional<LikeType> findByName(String name);
}
