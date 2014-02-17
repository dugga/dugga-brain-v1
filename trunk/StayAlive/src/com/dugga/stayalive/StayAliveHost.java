package com.dugga.stayalive;

import org.eclipse.rse.core.model.Host;

public class StayAliveHost {
	private Host host;
	private StayAliveProperties hostProperties;
	private boolean include = false;
	private String sourceLibrary;
	private String sourceFile;
	private String sourceMember;
	
	
	
	public StayAliveHost(Host host) {
		super();
		this.host = host;
		hostProperties = new StayAliveProperties(host.getAliasName());
		include = Boolean.parseBoolean(hostProperties.getProperty("include", "false"));
		sourceFile = hostProperties.getProperty("sourcefile", "QTXTSRC");
		sourceLibrary = hostProperties.getProperty("sourcelibrary", "QGPL");
		sourceMember = hostProperties.getProperty("sourcemember", "SADEFAULT");
	}
	
	public Host getHost() {
		return host;
	}
	public void setHost(Host host) {
		this.host = host;
	}
	public boolean getInclude() {
		return include;
	}
	public void setInclude(boolean include) {
		this.include = include;
		hostProperties.setProperty("include", String.valueOf(include));
	}
	public String getSourceLibrary() {
		return sourceLibrary;
	}
	public void setSourceLibrary(String sourceLibrary) {
		this.sourceLibrary = sourceLibrary;
		hostProperties.setProperty("sourcelibrary", sourceLibrary);
		
	}
	public String getSourceFile() {
		return sourceFile;
	}
	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
		hostProperties.setProperty("sourcefile", sourceFile);
	}
	public String getSourceMember() {
		return sourceMember;
	}
	public void setSourceMember(String sourceMember) {
		this.sourceMember = sourceMember;
		hostProperties.setProperty("sourcemember", sourceMember); 
	}
	
}
