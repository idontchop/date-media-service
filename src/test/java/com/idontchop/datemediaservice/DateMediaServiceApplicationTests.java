package com.idontchop.datemediaservice;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.idontchop.datemediaservice.entities.LikeType;
import com.idontchop.datemediaservice.entities.MediaCategory;
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

	@Test
	void contextLoads() {
	}

	@Test
	void createDb () {
		
		
	}
	
	@Test
	void testApi () {
		
		String s = dataApiService.deleteImage("none").block();
		
		assertTrue(s.equals(""));
		
	}
}
