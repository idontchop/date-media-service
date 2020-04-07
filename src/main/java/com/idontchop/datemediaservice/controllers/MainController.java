package com.idontchop.datemediaservice.controllers;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.idontchop.datemediaservice.dtos.MediaDataDto;
import com.idontchop.datemediaservice.dtos.RestMessage;
import com.idontchop.datemediaservice.entities.Media;
import com.idontchop.datemediaservice.entities.MediaCategory;
import com.idontchop.datemediaservice.services.CategoryService;
import com.idontchop.datemediaservice.services.DataApiService;

import reactor.core.publisher.Mono;

/**
 *  /category/  GET
 *  /category/{id} GET
 *  
 * @author micro
 *
 */
@RestController
public class MainController {
	
	@Autowired
	DataApiService dataApiService;
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping ("/category")
	public Iterable<MediaCategory> getCategoryList() {
		return categoryService.getCategoryList();
	}
	
	@GetMapping ("/category/{id}")
	public MediaCategory getCategory(
			@PathVariable ( name = "id", required = true) long id) {
		return categoryService.getCategory(id);
	}
	
	@PostMapping ( "/testMediaData" )
	public Mono<MediaDataDto> newMedia (
			@RequestParam("file") MultipartFile file,
			@RequestParam( value = "owner", required=true) String owner) throws IOException {
		
		if (file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		return dataApiService.saveImage(owner, file.getBytes());
		
	}
	
	@GetMapping ( "/testMedia")
	public Media testMedia () {
		Media m = new Media();
		MediaCategory mc = new MediaCategory();
		mc.setId(1L);
		mc.setName("test");
		mc.setUrl("mediadata-service");
		m.setCategory(mc);
		m.setDataId("5e8924ee9072d9381990584b");
		m.setDescription("test description");
		m.setOwner("1");
		return m;
	}
	
	@Value ("${server.port}")
	private String serverPort;
	
	@Value("${spring.application.name}")
	private String appName;

	@GetMapping("/helloWorld")
	public RestMessage helloWorld () {
		String serverAddress,serverHost;
		try {
			serverAddress = NetworkInterface.getNetworkInterfaces().nextElement()
					.getInetAddresses().nextElement().getHostAddress();
		} catch (SocketException e) {
			serverAddress = e.getMessage();
		}
		try {
			serverHost = NetworkInterface.getNetworkInterfaces().nextElement()
					.getInetAddresses().nextElement().getHostName();
		} catch (SocketException e) {
			serverHost = e.getMessage();
		}
		return RestMessage.build("Hello from " + appName)
				.add("service", appName)
				.add("host", serverHost)
				.add("address", serverAddress)
				.add("port", serverPort);
			
	}
}
