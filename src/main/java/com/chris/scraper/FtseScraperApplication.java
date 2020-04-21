package com.chris.scraper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class FtseScraperApplication {

	public static void main(String[] args) {
		
		RestTemplate rest = new RestTemplate();
		
		List<String> ftse = Arrays.asList("3IN","FOUR","ASL","AGK","AAF","ATST","APAX","ASCL","ASHM","AGR","AML","AVST","AGT","AVON","BME",
				"BAB","BGFD","BAKK","BBY","BNKR","BGEO","BAG","BBGI","BEZ","AJB","BWY","BIFF","BYG","BRSC","BRWM","BCPT","BGSC","BOY","BRW",
				"BVIC","GCC","CNE","CLDN","CAPC","CPI","CEY","CHG","CINE","CTY","CKN","CBG","CLI","COA","CCC","GLO","CTEC","CSP","CWK","CRST",
				"DJAN","DPH","DLN","DPLM","DLG","DC.","DOM","DRX","DNLM","EDIN","ECM","ELM","ENOG","EQN","ESNT","ERM","FDM","FXPO","FCSS",
				"FEV","FSV","FGT","FGP","FSJ","FRCL","FSFL","FORT","FRAS","FUTR","GFS","GAW","GCP","DIGS","GSS","GNS","GOG","GFTU","GRI",
				"GPOR","UKW","GNC","GRG","GVC","GYS","HMSO","HVPE","HSTG","HAS","HTWS","HSL","HRI","HGT","HICL","HILS","HFG","SONG","HSX",
				"HOC","HSV","HWDN","HYVE","IBST","ICGT","IGG","IMI","IEM","INCH","ICP","INPP","INVP","IPO","IWG","JLG","JII","JAM","JMG",
				"JFJ","JEO","JUP","JUST","KNOS","KAZ","KGF","LRE","LWDB","LMP","LXI","EMG","MKS","MSLH","MARS","MCS","MDC","MRC","MCRO",
				"MAB","MONY","MNKS","MGAM","MGNS","MYI","NEX","NETW","NESF","N91","NMC","OTB","OXIG","PAGE","PIN","PAG","PAY","PLI","PSH",
				"PNL","PFC","POG","PETS","PTEC","PLUS","PCT","P2P","PLP","PPH","PHP","PFG","PRTC","PZC","QQ.","QLT","RNK","RAT","RDW","TRIG",
				"RSW","RHIM","RCP","ROR","RMG","SBRE","SAFE","SNN","SVS","SDP","SOI","SCIN","SNR","SEQI","SRP","SHB","SIG","SRE","SSON","SCT",
				"SXS","SPT","SSPG","SGC","SMP","SYNC","SYNT","TALK","TATE","TBCG","TEP","TMPL","TEM","TIFS","TCAP","TRN","TPK","BBOX","TRY",
				"TUI","UDG","UKCM","ULE","UTG","VSVS","VCT","VEIL","VOF","VMU","VTY","VVO","WOSG","WEIR","JDW","SMWH","WMH","WTAN","WIZZ",
				"WG","WKP","WWH","XPP");
		
		List<StockInfo> stocks = new ArrayList<>(ftse.size());
		for (String ticker : ftse) {
			String url = "https://query1.finance.yahoo.com/v7/finance/download/" + ticker + ".L?period1=1555804800&period2=1587427200&interval=1wk&events=history";
			try {
				stocks.add(new StockInfo(ticker, rest.getForEntity(url, String.class).getBody()));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		stocks.sort((s1, s2) -> s1.percentage().compareTo(s2.percentage()));
		stocks.forEach(s -> System.out.println(s));
		System.out.println(stocks.size() + " total results");
	}
	
}
