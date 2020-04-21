package com.chris.scraper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class FtseScraperApplication {

	public static void main(String[] args) {
		
		RestTemplate rest = new RestTemplate();
		ResponseEntity<String> entity = rest.getForEntity("https://query1.finance.yahoo.com/v7/finance/download/BARC.L?period1=1555804800&period2=1587427200&interval=1wk&events=history", String.class);
		
		System.out.println(new StockInfo("BARC.L", entity.getBody()));
		
	}
	
}
