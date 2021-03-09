package kr.pe.absolju.esper.event.service;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.compiler.client.EPCompiler;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.runtime.client.EPDeployException;
import com.espertech.esper.runtime.client.EPDeployment;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeDestroyedException;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;

import kr.pe.absolju.esper.event.eventdto.StockTick;

public class StockFinderBean {
	
	private EPRuntime runtime;
	private StockFoundListener tickListener;
	private StockFoundListener tockListener;
	
	// 초기화
	// esper 7->8 변경점은 https://www.espertech.com/category/esper-8/ 참고
	public void init() {
		/// config 생성
		Configuration config = new Configuration();
		config.getCommon().addEventType("StockTick", StockTick.class);
		config.getCommon().addEventType("StockTock", StockTick.class);

		/// EPL 컴파일
		EPCompiler compiler = EPCompilerProvider.getCompiler();
		CompilerArguments configArg = new CompilerArguments(config);
		EPCompiled eplCompiledTick;
		EPCompiled eplCompiledTock;
		try {
			eplCompiledTick = compiler.compile("@name('stock-rate-rise-0') SELECT * FROM StockTick t WHERE t.rate > 0;", configArg);
			eplCompiledTock = compiler.compile("@name('stock-rate-drop-0') SELECT * FROM StockTock t WHERE t.rate < 0;", configArg);
		} catch (EPCompileException e) {
			throw new RuntimeException(e);
		}
		
		/// EPL 런타임
		runtime = EPRuntimeProvider.getRuntime("Stock", config);
		EPDeployment deploymentTick = null;
		EPDeployment deploymentTock = null;
		try {
			deploymentTick = runtime.getDeploymentService().deploy(eplCompiledTick);
			deploymentTock = runtime.getDeploymentService().deploy(eplCompiledTock);
		} catch (EPRuntimeDestroyedException | EPDeployException e) {
			throw new RuntimeException(e);
		}
		EPStatement statementTick = runtime.getDeploymentService().getStatement(deploymentTick.getDeploymentId(), "stock-rate-rise-0");
		EPStatement statementTock = runtime.getDeploymentService().getStatement(deploymentTock.getDeploymentId(), "stock-rate-drop-0");
		
		/// listener 부착
		statementTick.addListener(new UpdateListener() {
			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
				// String targetName = (String) newEvents[0].get("name");
				// String targetCode = (String) newEvents[0].get("code");
				// ...
				StockTick target = (StockTick) newEvents[0].getUnderlying();
				
				// System.out.println(target.toString());
				tickListener.foundBean(target);
			}
		});
		statementTock.addListener(new UpdateListener() {
			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
				StockTick target = (StockTick) newEvents[0].getUnderlying();
				tockListener.foundBean(target);
			}
		});
	}
	
	// listener 지정
	public void setTickListener(StockFoundListener listener) {
		this.tickListener = listener;
	}
	public void setTockListener(StockFoundListener listener) {
		this.tockListener = listener;
	}
	
	// data 입력
	public void sendNewStockTick(StockTick tick) {
		/// sendEventJson, sendEventMap 등 있음
		runtime.getEventService().sendEventBean(tick, "StockTick");
	}
	public void sendNewStockTock(StockTick tock) {
		/// sendEventJson, sendEventMap 등 있음
		runtime.getEventService().sendEventBean(tock, "StockTock");
	}
	
}
