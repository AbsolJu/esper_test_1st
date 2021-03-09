package kr.pe.absolju.esper;

import java.util.Map;

import kr.pe.absolju.esper.event.eventdto.StockTick;
import kr.pe.absolju.esper.event.service.StockFinderBean;
import kr.pe.absolju.esper.event.service.StockFinderMap;
import kr.pe.absolju.esper.event.service.StockFinderJson;
import kr.pe.absolju.esper.event.service.StockFoundListener;

/**
 * Esper 8 테스트
 * 2021-03-05 ~
 * @author jysong
 */
public class App {

	public static void main(String[] args) {
		// esper service 생성
//		StockFinderBean stockFinder = new StockFinderBean();
//		StockFinderMap stockFinder = new StockFinderMap();
		StockFinderJson stockFinder = new StockFinderJson();
		
		// listener 선언
		StockFoundListener stockTickListener = new StockFoundListener() {
			@Override
			public void foundBean(StockTick stockTick) {
				System.out.println("TICK: " + stockTick);
			}

			@Override
			public void foundMap(Map<String, Object> stockTick) {
				System.out.println("TICK: " + stockTick);
			}
		};
		StockFoundListener stockTockListener = new StockFoundListener() {
			@Override
			public void foundBean(StockTick stockTick) {
				System.out.println("tock: " + stockTick);
			}

			@Override
			public void foundMap(Map<String, Object> stockTick) {
				System.out.println("tock: " + stockTick);
			}
		};
		
		// 초기화
		stockFinder.init();
		stockFinder.setTickListener(stockTickListener);
		stockFinder.setTockListener(stockTockListener);
		
		// 테스트 데이터(21년도 첫 주차)
		stockFinder.sendNewStockTick(new StockTick("삼성전자", "005930", 83000, +2000, +2.47));	// 21.01.04
		stockFinder.sendNewStockTick(new StockTick("삼성전자", "005930", 83900, +900, +1.08));	// 21.01.05
		stockFinder.sendNewStockTick(new StockTick("삼성전자", "005930", 82200, -1700, -2.03));	// 21.01.06
		stockFinder.sendNewStockTick(new StockTick("삼성전자", "005930", 82900, +700, +0.85));	// 21.01.07
		stockFinder.sendNewStockTick(new StockTick("삼성전자", "005930", 88800, +5900, +7.12));	// 21.01.08
		
		stockFinder.sendNewStockTock(new StockTick("삼성전자", "005930", 83000, +2000, +2.47));	// 21.01.04
		stockFinder.sendNewStockTock(new StockTick("삼성전자", "005930", 83900, +900, +1.08));	// 21.01.05
		stockFinder.sendNewStockTock(new StockTick("삼성전자", "005930", 82200, -1700, -2.03));	// 21.01.06
		stockFinder.sendNewStockTock(new StockTick("삼성전자", "005930", 82900, +700, +0.85));	// 21.01.07
		stockFinder.sendNewStockTock(new StockTick("삼성전자", "005930", 88800, +5900, +7.12));	// 21.01.08
	}

}
