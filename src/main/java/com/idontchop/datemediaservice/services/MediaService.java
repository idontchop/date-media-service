package com.idontchop.datemediaservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idontchop.datemediaservice.entities.Media;
import com.idontchop.datemediaservice.repositories.MediaRepository;

@Service
public class MediaService {
	
	@Autowired
	MediaRepository mediaRepository;

	public Media saveMedia ( Media media ) {
		return mediaRepository.save(media);
	}
}
