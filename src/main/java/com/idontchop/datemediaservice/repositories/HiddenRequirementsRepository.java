package com.idontchop.datemediaservice.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.idontchop.datemediaservice.entities.HiddenRequirements;

public interface HiddenRequirementsRepository extends CrudRepository <HiddenRequirements, Long>{

	public Optional<HiddenRequirements> findByHiddenMedia_Id(long id);
	public Optional<HiddenRequirements> findByHiddenMedia_DataId(String dataId);
}
