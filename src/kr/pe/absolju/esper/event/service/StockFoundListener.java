package kr.pe.absolju.esper.event.service;

import java.util.Map;

import kr.pe.absolju.esper.event.eventdto.StockTick;

public interface StockFoundListener {
	
	public void foundBean(StockTick stockTick);
	public void foundMap(Map<String,Object> stockTick);
	
}
