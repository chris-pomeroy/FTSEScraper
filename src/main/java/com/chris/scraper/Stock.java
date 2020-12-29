package com.chris.scraper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Stock {

	private String ticker;
	private BigDecimal average;
	private BigDecimal recent;
	private BigDecimal percentage;

	private static final LocalDate CRASH = LocalDate.parse("2020-02-20");

	private Stock() {}

	public static Stock parseAverage(String ticker, String s) {
		Stock stock = new Stock();
		stock.ticker = ticker;
		String[] lines = s.split("\n");
		BigDecimal sum = BigDecimal.ZERO;
		for (int c = 1; c < lines.length; c++) {
			String[] line = lines[c].split(",");
			LocalDate date = LocalDate.parse(line[0]);
			sum = sum.add(new BigDecimal(line[1]));
		}
		stock.average = sum.divide(new BigDecimal(lines.length - 1), RoundingMode.HALF_UP);
		return stock;
	}
	
	public void setRecent(String s) {
		String[] lines = s.split("\n");
		recent = new BigDecimal(lines[lines.length-1].split(",")[1]);
		percentage = recent.divide(average, RoundingMode.HALF_UP).multiply(new BigDecimal(100D));
	}
	
	public String getTicker() {
		return ticker;
	}

	public BigDecimal getAverage() {
		return average;
	}

	public BigDecimal getRecent() {
		return recent;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public String toString() {
		return ticker + ": average " + average + ", recent " + recent;
	}
}
