package com.idontchop.datemediaservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.idontchop.datemediaservice.dtos.LikesByMedia;
import com.idontchop.datemediaservice.entities.HiddenRequirements;
import com.idontchop.datemediaservice.entities.Like;
import com.idontchop.datemediaservice.entities.LikeType;
import com.idontchop.datemediaservice.entities.Media;
import com.idontchop.datemediaservice.entities.MediaCategory;
import com.idontchop.datemediaservice.repositories.HiddenRequirementsRepository;
import com.idontchop.datemediaservice.repositories.LikeRepository;
import com.idontchop.datemediaservice.repositories.LikeTypeRepository;
import com.idontchop.datemediaservice.repositories.MediaCategoryRepository;
import com.idontchop.datemediaservice.repositories.MediaRepository;
import com.idontchop.datemediaservice.services.DataApiService;

@SpringBootTest
class DateMediaServiceApplicationTests {

	@Autowired
	MediaCategoryRepository mediaCategoryRepository;

	@Autowired
	MediaRepository mediaRepository;

	@Autowired
	LikeTypeRepository likeTypeRepository;

	@Autowired
	DataApiService dataApiService;

	@Autowired
	LikeRepository likeRepository;

	@Autowired
	HiddenRequirementsRepository hiddenRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void createDb() {

	}

	@Test
	void loadHidden() {

		assertTrue(hiddenRepository.findByHiddenMedia_Id(3L).orElseThrow().getOwner().equals("username"));
		/*
		 * List<LikesByMedia> l = likeRepository.countLikesByMediaAndUser(List.of(3L),
		 * "33"); assertEquals(l.size(), 1); assertTrue(l.get(0).getCost() == 0);
		 */
		
		

	}

	@Test
	void findAllByOwner() {
		List<String> owners = List.of("username", "none");

		Set<Media> mediaList = mediaRepository.findAllByOwnerIn(owners);

		Set<String> ownerSet = mediaList.stream().map(media -> media.getOwner()).collect(Collectors.toSet());

		assertEquals(1, ownerSet.size());
	}

	void createLikes() {

		Media media1 = mediaRepository.findById(1L).orElseThrow();
		LikeType likeType = likeTypeRepository.findById(1L).orElseThrow();

		Like newLike = new Like();
		newLike.setMedia(media1);
		newLike.setLikeType(likeType);
		newLike.setOwner("4");

		likeRepository.save(newLike);
	}

	@Test
	void testLikes() {

		// assertEquals (3, likeRepository.countByMedia_Id(1L));
		// assertEquals (1, likeRepository.findAllByOwner("2").size());
		// assertEquals (3, likeRepository.findAllByMedia_Id(1L).size());

	}
}
