package com.idontchop.datemediaservice.repositories;

import org.springframework.data.repository.CrudRepository;

import com.idontchop.datemediaservice.entities.Media;

public interface MediaRepository extends CrudRepository <Media, Long> {

}
