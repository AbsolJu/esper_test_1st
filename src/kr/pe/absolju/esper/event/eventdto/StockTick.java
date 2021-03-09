package kr.pe.absolju.esper.event.eventdto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class StockTick {
	
	private String name;
	private String code;
	private Integer price;
	private Integer variance;
	private Double rate;
	
	@Override
	public String toString() {
		String targetDesc = name+"("+code+")";
		String targetValue = price+"KRW(변동량"+variance+"KRW/비율"+rate+")";
		return "Stock [ "+targetDesc+": "+targetValue+" ]";
	}
	
}
