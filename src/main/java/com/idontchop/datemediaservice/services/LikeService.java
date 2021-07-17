package com.idontchop.datemediaservice.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idontchop.datemediaservice.dtos.LikesByMedia;
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
	
	/*
	 * Repositories
	 */
	@Autowired
	LikeRepository likeRepository;
	
	@Autowired
	LikeTypeRepository likeTypeRepository;
	
	@Autowired
	MediaRepository mediaRepository;
	
	@Autowired
	MessageService messageService;
	
	/*
	 * Libraries
	 */
	
	
	
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
	
	public List<Like> getLikeByOwnerAndId ( String owner, List<Long> mediaIds ) {
		return likeRepository.findByOwnerAndMedia_IdIn(owner, mediaIds);
	}
	
	public Like getLikeByOwnerMediaLikeType ( String owner, long mediaId, String likeType ) {
		
		Optional<Like> likeOpt = likeRepository.findByOwnerAndMedia_IdAndLikeType_Id(owner, mediaId, findLikeTypeId(likeType));
		if ( likeOpt.isEmpty() ) {
			throw new NoSuchElementException ("Like with " + owner + " mediaId: " + mediaId + " likeType: " + likeType + " doesn't exist.");
		}
		
		return likeOpt.get();
	}
	
	public Like newLike ( String owner, long likeTypeId, long mediaId) {
		
		// check if already exists
		if ( likeRepository.findByOwnerAndMedia_IdAndLikeType_Id(owner, mediaId, likeTypeId).isPresent() ) {
			throw new IllegalArgumentException("Like with " + owner + " and " + mediaId + " already exists.");
		}
		
		// check that supplied ids exist
		Media media = mediaRepository.findById(mediaId).orElseThrow();
		LikeType likeType = likeTypeRepository.findById(likeTypeId).orElseThrow();
		
		// setup new Like
		Like like = new Like(owner, likeType, media);
		
		return 
				messageService.sendRawLike(
						likeRepository.save(like));
	}
	
	/**
	 * Owner must be passed for verification. This isn't called by a user exposed endpoint. For that,
	 * look at undoLike
	 * 
	 * @param owner
	 * @param mediaId
	 */
	public Like deleteLike ( String owner, long mediaId, String likeType ) {
		
		Like like = getLikeByOwnerMediaLikeType(owner, mediaId, likeType);
		
		return deleteLike(like);
	}
	
	/**
	 * User requested like removed. This always results in deleting the like as well, but
	 * additionally fires a like_undo message to attempt payment refund.
	 * 
	 * @param owner
	 * @param mediaId
	 * @param likeType
	 */
	public Like undoLike ( String owner, long mediaId, String likeType ) {
		
		Like like = getLikeByOwnerMediaLikeType(owner, mediaId, likeType);
		
		messageService.undoLike(like);
		
		return deleteLike(like);
	}
	
	/**
	 * Deletes a like by the Like ID.
	 * 
	 * This method called by internal processes and isn't exposed to REST.
	 * 
	 * (message from pay service comes back likeInvalid)
	 * @param id
	 */
	public Like deleteLike (long id) {
		return deleteLike(likeRepository.findById(id).orElseThrow());
	}
	
	/**
	 * All deletes should pass through here to insure message sent
	 * 
	 * @param like
	 * @return
	 */
	public Like deleteLike (Like like) {
		messageService.deleteLike(like);
		likeRepository.delete(like);
		return like;
	}
	
	public long findLikeTypeId(String name) throws NoSuchElementException {
		
		LikeType likeType = likeTypeRepository.findByName(name).orElseThrow();
		return likeType.getId();
	}
	
	public List<LikesByMedia> getLikesByMedia (List<Long> mediaIds) {
		return likeRepository.countLikesByMedia(mediaIds);
	}

}
