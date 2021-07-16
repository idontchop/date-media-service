package com.idontchop.datemediaservice.repositories;

import org.springframework.data.repository.CrudRepository;

import com.idontchop.datemediaservice.entities.MediaCategory;

public interface MediaCategoryRepository extends CrudRepository<MediaCategory, Long> {

}
