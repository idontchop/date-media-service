package com.idontchop.datemediaservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.idontchop.datemediaservice.entities.LikeType;
import com.idontchop.datemediaservice.entities.MediaCategory;
import com.idontchop.datemediaservice.repositories.LikeTypeRepository;
import com.idontchop.datemediaservice.repositories.MediaCategoryRepository;
import com.idontchop.datemediaservice.repositories.MediaRepository;

@SpringBootTest
class DateMediaServiceApplicationTests {
	
	@Autowired
	MediaCategoryRepository mediaCategoryRepository;
	
	@Autowired
	MediaRepository mediaRepository;
	
	@Autowired
	LikeTypeRepository likeTypeRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void createDb () {
		
		MediaCategory mc = new MediaCategory();
		mc.setType(MediaCategory.Type.IMAGE);
		mc.setUrl("http://localhost:8980/image/");
		
		mediaCategoryRepository.save(mc);
		
		LikeType lt = new LikeType();
		lt.setName("token");
		
		likeTypeRepository.save(lt);
	}
}
