package com.idontchop.datemediaservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
	
	public Mono<MediaDataDto> updateImage (String owner, String id, byte[] file) {
		
		WebClient webClient = WebClient.builder()
				.baseUrl(dataUrl)
				.defaultHeader(HttpHeaders.USER_AGENT, appName)
				.build();
				
		Mono<MediaDataDto> dto = webClient
				.put()
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(BodyInserters.fromMultipartData("file", new ByteArrayResource( file ) {
						@Override public String getFilename() {return "f.jpg";}	// need a filename for media-data api
					}).with("owner",owner).with("id",id) )
				.retrieve()
				.bodyToMono(MediaDataDto.class);
		
		return dto;
	}
	
	public Mono<String> deleteImage ( String id ) {
		
		WebClient webClient = WebClient.builder()
				.baseUrl(dataUrl)
				.defaultHeader(HttpHeaders.USER_AGENT, appName )
				.build();
		
		return webClient.delete()
			.uri ( uriBuilder -> uriBuilder.path(id).build() )
			.retrieve()
			.onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new IllegalArgumentException(id)))
			.bodyToMono(String.class);
			
	}

}

/*
 * https://www.codota.com/code/java/methods/org.springframework.web.reactive.function.client.WebClient$ResponseSpec/onStatus
 *
private void redirectPost(EthereumAccount ethereumAccount, String uri, BigInteger nonce) {
this.webClient
    .post()
    .uri(uri)
    .contentType(MediaType.APPLICATION_JSON_UTF8)
    .retrieve()
    .onStatus(HttpStatus::is4xxClientError, clientResponse -> clientResponse
        .bodyToMono(ByteArrayResource.class)
        .map(ByteArrayResource::getByteArray)
        .map(String::new)
        .map(ClientResponseException::new)
    )
    .onStatus(HttpStatus::is5xxServerError, clientResponse -> clientResponse
        .bodyToMono(ByteArrayResource.class)
        .map(ByteArrayResource::getByteArray)
        .map(String::new)
        .map(ClientResponseException::new)
    )
    .bodyToMono(String.class)
    .doOnSuccess(faucetResponseDto -> ethereumAccount.setNonce(null != nonce?nonce:BigInteger.ZERO))
    .doOnError(RuntimeException::new)
    .block();
*/