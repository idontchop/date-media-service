package com.idontchop.datemediaservice.services;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idontchop.datemediaservice.dtos.MediaDataDto;
import com.idontchop.datemediaservice.dtos.ProfileMediaDto;
import com.idontchop.datemediaservice.entities.Media;
import com.idontchop.datemediaservice.entities.MediaCategory;
import com.idontchop.datemediaservice.repositories.MediaRepository;

import javassist.NotFoundException;

@Service
public class MediaService {
	
	@Autowired
	MediaRepository mediaRepository;
	
	@Autowired
	DataApiService dataApiService;
	
	@Autowired
	CategoryService categoryService;

	/**
	 * Saves new media and saves to data service
	 * 
	 * @param media
	 * @param file
	 * @return
	 */
	public Media saveMedia ( Media media , byte[] file) {
		
		MediaDataDto dataDto = dataApiService.saveImage(media.getOwner(), file)
				.timeout(Duration.ofMillis(500L)).block();
		
		media.setDataId(dataDto.getId());
		
		return mediaRepository.save(media);
	}
	
	public Media replaceMedia ( Media media, byte[] file ) throws IllegalArgumentException {
		
		Media newMedia = null;
		try {
			if ( media.getId() > 0) newMedia = updateMedia (media);
			MediaDataDto dataDto = dataApiService.updateImage(media.getOwner(), media.getDataId(), file)
				.timeout(Duration.ofMillis(500L)).block();
		} catch (IllegalArgumentException e) {
			// did not find data id on data service, this is ok, we can create
			media = saveMedia ( media, file);
		} catch (NoSuchElementException e) {
			// this happens if media was supplied with an id and owner that didn't match
			// this is a no go
			throw new IllegalArgumentException ("Id and Owner did not match. If you want to create a new media"
					+ " file, use POST or leave ID field empty.");
		}
		
		return newMedia;
	}
	
	/**
	 * Deletes media. Deletes from data service.
	 * 
	 * @param id
	 * @throws IOException
	 * @throws NoSuchElementException
	 */
	public void deleteMedia ( long id, String owner ) throws IOException, NoSuchElementException {
		
		Media deleteMedia = mediaRepository.findByIdAndOwner(id, owner).orElseThrow();
		try {
			dataApiService.deleteImage(deleteMedia.getDataId())
				.timeout(Duration.ofMillis(500L)).block();
		} catch ( IllegalArgumentException e) {
			// This is ok, it wasn't on dataservice. TODO: log?
		} 
		
		mediaRepository.delete(deleteMedia);
		
	}
	
	public Media updateMedia ( Media newMedia ) throws NoSuchElementException {
		
		// find by owner and id. Wont allow updates to either after created if not known.
		Media oldMedia = mediaRepository.findByIdAndOwner(newMedia.getId(), newMedia.getOwner())
				.orElseThrow();
		
		// find category to make sure repository doesnt cascade
		// also disallows changing of category
		MediaCategory category = categoryService.getCategory(oldMedia.getCategory().getId());		
		newMedia.setCategory(category);
		
		oldMedia.updateMedia(newMedia);
		
		return mediaRepository.save(oldMedia);
	}

	public Media getMedia(long id) {
		return mediaRepository.findById(id).orElseThrow();
	}
	
	public List<Media> getMediaByOwner(String owner) {
		return mediaRepository.findAllByOwner(owner);
	}
	
	public List<ProfileMediaDto> getProfileMediaByOwner (String owner) {
		List<Media> mediaList = getMediaByOwner(owner);
		
		return mediaList.stream()
				.filter( media -> media.isProfileApproved() )
				.map( media -> ProfileMediaDto.from(media))
				.collect(Collectors.toList());
	}
}
