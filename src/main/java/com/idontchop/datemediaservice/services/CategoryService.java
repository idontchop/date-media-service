package com.idontchop.datemediaservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idontchop.datemediaservice.entities.MediaCategory;
import com.idontchop.datemediaservice.repositories.MediaCategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	MediaCategoryRepository categoryRepository;
	
	public MediaCategory getCategory (long id) {
		return categoryRepository.findById(id).orElseThrow();
	}
	
	public Iterable<MediaCategory> getCategoryList () {
		return categoryRepository.findAll();
	}

}
