package com.chris.scraper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class FtseScraperApplication {

	public static void main(String[] args) {
		
		RestTemplate rest = new RestTemplate();
		ResponseEntity<String> entity = rest.getForEntity("http://www.google.com", String.class);
		System.out.println(entity.getBody());
	}
	
}
