package com.idontchop.datemediaservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.idontchop.datemediaservice.dtos.MediaDataDto;

import reactor.core.publisher.Mono;

@Service
public class DataApiService {
	
	@Value("${date.data.url}")
	private String dataUrl;
	
	@Value("${spring.application.name}")
	private String appName;
	
	/**
	 * API to save the media to the mediadata-service.
	 * 
	 * Returns a DTO with the ID supplied by mediadata-service.
	 * 
	 * @param owner
	 * @param file
	 * @return
	 */
	public Mono<MediaDataDto> saveImage ( String owner, byte[] file) {
		
		WebClient webClient = WebClient.builder()
				.baseUrl(dataUrl)
				.defaultHeader(HttpHeaders.USER_AGENT, appName )
				.build();
		
		Mono<MediaDataDto> dto = webClient
				.post()
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(BodyInserters.fromMultipartData("file", new ByteArrayResource( file ) {
						@Override public String getFilename() {return "f.jpg";}	// need a filename for media-data api
					}).with("owner",owner))
				.retrieve()
				.bodyToMono(MediaDataDto.class);
				
		
		return dto;
	}

}
