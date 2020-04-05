package com.idontchop.datemediaservice.controllers;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.idontchop.datemediaservice.dtos.MediaDataDto;
import com.idontchop.datemediaservice.dtos.RestMessage;
import com.idontchop.datemediaservice.services.DataApiService;

import reactor.core.publisher.Mono;

@RestController
public class MainController {
	
	@Autowired
	DataApiService dataApiService;
	
	@PostMapping ( "/testMedia" )
	public Mono<MediaDataDto> newMedia (
			@RequestParam("file") MultipartFile file,
			@RequestParam( value = "owner", required=true) String owner) throws IOException {
		
		if (file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		return dataApiService.saveImage(owner, file.getBytes());
		
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
