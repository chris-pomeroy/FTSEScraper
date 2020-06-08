package com.chris.scraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.client.RestTemplate;

public class FtseScraperApplication {

	public static void main(String[] args) throws IOException {
		
		RestTemplate rest = new RestTemplate();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(FtseScraperApplication.class.getResourceAsStream("/stocks.csv")));
		List<String> ftse = br.lines().collect(Collectors.toList());
		br.close();
		
		List<StockInfo> stocks = new ArrayList<>(ftse.size());
		List<String> exceptions = new ArrayList<String>();
		for (String ticker : ftse) {
			String url = "https://query1.finance.yahoo.com/v7/finance/download/" + ticker + ".L?period1=1556064000&period2=1591660800&interval=1d&events=history";
			try {
				stocks.add(new StockInfo(ticker, rest.getForEntity(url, String.class).getBody()));
			}
			catch (Exception e) {
				exceptions.add(ticker);
			}
		}
		stocks.sort((s1, s2) -> s1.percentage().compareTo(s2.percentage()));
		System.out.println("------------------------");
		stocks.forEach(System.out::println);
		System.out.println(stocks.size() + " total results");
		System.out.println("------------------------");
		System.out.println("The following shares were not found: ");
		exceptions.forEach(System.out::println);
		System.out.println("------------------------");
		System.out.println("WARNING: Ensure period1 and period2 are updated");
	}
	
}
