package com.chris.scraper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {

	private static final Logger log = LoggerFactory.getLogger(StockService.class);
	
	private static final LocalDate CRASH = LocalDate.parse("2020-02-20");
	private static final LocalDate START = CRASH.minusMonths(6);

	private List<Stock> stocks;
	private LocalDateTime lastUpdated;

	private String getTicker(String ticker, LocalDate start, LocalDate end) {
		RestTemplate rest = new RestTemplate();
		String url = "https://query1.finance.yahoo.com/v7/finance/download/"
				+ ticker
				+ ".L?period1="
				+ start.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
				+ "&period2="
				+ end.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
				+ "&interval=1d&events=history";
		return rest.getForEntity(url, String.class).getBody();
	}

	@PostConstruct
	public void init() {

		List<String> ftse = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(StockService.class.getResourceAsStream("/stocks.csv")))) {
			ftse = br.lines().collect(Collectors.toList());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		log.info("Getting share prices");

		List<Stock> stocks = new ArrayList<>(ftse.size());
		List<String> exceptions = new ArrayList<>();
		for (String ticker : ftse) {
			try {
				Stock stock = Stock.parseAverage(ticker, getTicker(ticker, START, CRASH));
				stock.setRecent(getTicker(ticker, LocalDate.now().minusDays(5), LocalDate.now().plusDays(1)));
				stocks.add(stock);
			}
			catch (Exception e) {
				exceptions.add(ticker);
			}
		}

		stocks.sort(Comparator.comparing(Stock::getPercentage));
		this.stocks = stocks;
		this.lastUpdated = LocalDateTime.now();

		log.info(stocks.size() + " total results");
		log.info("The following shares were not found: ");
		exceptions.forEach(e -> log.info(e));
	}

	public List<Stock> getStocks() {
		return stocks;
	}

	public String getLastUpdated() {
		LocalDate date = lastUpdated.toLocalDate();
		String formattedDate = date.format(DateTimeFormatter.ofPattern("d MMMM"));
		if (date.equals(LocalDate.now().minusDays(1)))
			formattedDate = "Yesterday";
		if (date.equals(LocalDate.now()))
			formattedDate = "Today";
		return formattedDate + " - " + lastUpdated.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
	}
}
