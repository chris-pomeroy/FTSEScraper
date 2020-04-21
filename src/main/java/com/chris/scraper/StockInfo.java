package com.chris.scraper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class StockInfo {
	
	private BigDecimal average;
	private BigDecimal recent;
	private String ticker;
	
	private static final LocalDate CRASH = LocalDate.parse("2020-02-20");
	
	public StockInfo(String ticker, String s) {
		this.ticker = ticker;
		String[] lines = s.split("\n");
		String r = lines[lines.length-1].split(",")[1];
		recent = new BigDecimal(r);
		BigDecimal rows = BigDecimal.ZERO;
		BigDecimal sum = BigDecimal.ZERO;
		for (int c = 1; c < lines.length; c++) {
			String[] line = lines[c].split(",");
			LocalDate date = LocalDate.parse(line[0]);
			if (date.isBefore(CRASH)) {
				sum = sum.add(new BigDecimal(line[1]));
				rows = rows.add(BigDecimal.ONE);
			}
		}
		average = sum.divide(rows, RoundingMode.HALF_UP);
	}
	
	public BigDecimal percentage() {
		return recent.divide(average, RoundingMode.HALF_UP).multiply(new BigDecimal(100D));
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public String toString() {
		return ticker + ": trading at " + percentage() + "%";
	}

}
