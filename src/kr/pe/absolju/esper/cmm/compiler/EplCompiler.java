package kr.pe.absolju.esper.cmm.compiler;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.compiler.client.EPCompiler;
import com.espertech.esper.compiler.client.EPCompilerProvider;

import lombok.NonNull;

public class EplCompiler {
	
	private final EPCompiler compiler = EPCompilerProvider.getCompiler();
	private CompilerArguments configArg;
	
	public EplCompiler(@NonNull Configuration config) {
		this.configArg = new CompilerArguments(config);
	}
	
	public EPCompiled compile(@NonNull String stateName, @NonNull String epl) {
		EPCompiled compiled;
		try {
			compiled = compiler.compile("@name('" + stateName + "') " + epl, configArg);
		} catch(EPCompileException e) {
			throw new RuntimeException(e);
		}
		
		return compiled;
	}
	
}
