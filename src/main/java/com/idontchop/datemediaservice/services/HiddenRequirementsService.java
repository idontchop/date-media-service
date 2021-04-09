package com.idontchop.datemediaservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idontchop.datemediaservice.dtos.LikesByMedia;
import com.idontchop.datemediaservice.dtos.RestWrapper;
import com.idontchop.datemediaservice.entities.HiddenRequirements;
import com.idontchop.datemediaservice.entities.Media;
import com.idontchop.datemediaservice.repositories.HiddenRequirementsRepository;
import com.idontchop.datemediaservice.repositories.LikeRepository;

@Service
public class HiddenRequirementsService {
	
	@Autowired
	HiddenRequirementsRepository hiddenRepository;
	
	@Autowired
	LikeRepository likeRepository;
	
	@Autowired
	MediaService mediaService;
	
	@Autowired
	JwtTokenService jwtService;
	
	public HiddenRequirements getRequirements(long mediaId) {
		
		return hiddenRepository.findByHiddenMedia_Id(mediaId).orElseThrow();
		
	}
	
	/**
	 * Using a mediaId and username, will determine if the user has suffecient score
	 * to provide access to a hidden image.
	 * 
	 * Returns a ready to deliver payload of RestWrapper with keys:
	 * 
	 * "token": the token if requirements met
	 * "requirements": OBJ - the requirements for this media (if not met)
	 * "like_counts" : OBJ - LikesByMedia that includes cost (score)
	 * 
	 * @param mediaId
	 * @param username
	 * @return
	 */
	public RestWrapper requestHidden(HiddenRequirements requirements, String username) {
		
		int score = 0;
		String token = "";
		
		List<LikesByMedia> likesByMedia = 
				likeRepository.countLikesByMediaAndUser(requirements.getMediaReferences(), username);
		
		// calculate score		
		for (int c = 0; c < likesByMedia.size(); c++) {
			score += likesByMedia.get(c).getCost();
		}
		
		// Check Score
		if ( score >= requirements.getRequiredScore() ) {

			Media media = requirements.getHiddenMedia();
			
			token = jwtService.buildToken(media.getDataId());	
		}
		
		// TODO: Handle required connection
		
		RestWrapper rw = 
				RestWrapper.build("requirements", requirements)
				.add("like_counts", likesByMedia);
		
		if ( ! token.equals(""))
			rw.add("token", token);
		
		return rw;
		
	}
	
	/**
	 * Overloaded to send different find by data
	 * @param requirements
	 * @param username
	 * @return
	 */
	public RestWrapper requestHidden(String dataId, String username) {
		
		return requestHidden(hiddenRepository.findByHiddenMedia_DataId(dataId).orElseThrow()
				, username);
	}
	
	public RestWrapper requestHidden(long mediaId, String username) {
		
		return requestHidden(hiddenRepository.findByHiddenMedia_Id(mediaId).orElseThrow()
				, username);
	}

}
