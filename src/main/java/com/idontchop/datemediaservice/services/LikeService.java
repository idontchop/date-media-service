package com.idontchop.datemediaservice.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idontchop.datemediaservice.entities.Like;
import com.idontchop.datemediaservice.entities.LikeType;
import com.idontchop.datemediaservice.entities.Media;
import com.idontchop.datemediaservice.repositories.LikeRepository;
import com.idontchop.datemediaservice.repositories.LikeTypeRepository;
import com.idontchop.datemediaservice.repositories.MediaRepository;

/**
 * Handles Likes and LikeTypes.
 * 
 * Likes can only be added and deleted, no modifying.
 * 
 * LikeType, only one for now, can only be retrieved
 * @author micro
 *
 */
@Service
public class LikeService {
	
	@Autowired
	LikeRepository likeRepository;
	
	@Autowired
	LikeTypeRepository likeTypeRepository;
	
	@Autowired
	MediaRepository mediaRepository;
	
	public long getLikeCount(long mediaId) {
		return likeRepository.countByMedia_Id(mediaId);
	}
	
	public List<Like> getLikeList(Media media) {
		return likeRepository.findAllByMedia_Id(media.getId());
	}
	
	public List<Like> getLikeListByOwner(String owner) {
		return likeRepository.findAllByOwner(owner);
	}
	
	public LikeType getLikeType (long id) {
		return likeTypeRepository.findById(id).orElseThrow();
	}
	
	public Like getLikeByOwnerAndId ( String owner, long mediaId ) {
		return likeRepository.findByOwnerAndMedia_Id(owner, mediaId).orElseThrow();
	}
	
	
	public Like newLike ( String owner, long likeTypeId, long mediaId) {
		
		// check if already exists
		if ( likeRepository.findByOwnerAndMedia_Id(owner, mediaId).isPresent() ) {
			throw new IllegalArgumentException("Like with " + owner + " and " + mediaId + " already exists.");
		}
		
		// check that supplied ids exist
		Media media = mediaRepository.findById(mediaId).orElseThrow();
		LikeType likeType = likeTypeRepository.findById(likeTypeId).orElseThrow();
		
		// setup new Like
		Like like = new Like(owner, likeType, media);
		
		return likeRepository.save(like);
	}
	
	/**
	 * Owner must be passed for verification.
	 * 
	 * @param owner
	 * @param mediaId
	 */
	public void deleteLike ( String owner, long mediaId, String likeType ) {
		
		Optional<Like> likeOpt = likeRepository.findByOwnerAndMedia_Id(owner, mediaId, findLikeTypeId(likeType));
		if ( likeOpt.isEmpty() ) {
			throw new NoSuchElementException ("Like with " + owner + " mediaId: " + mediaId + " doesn't exist.");
		}
		
		likeRepository.delete(likeOpt.get());		
	}
	
	public long findLikeTypeId(String name) throws NoSuchElementException {
		
		LikeType likeType = likeTypeRepository.findByName(name).orElseThrow();
		return likeType.getId();
	}

}
