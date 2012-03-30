package com.dugga.examples.SWT.IBMi.core.connections;

import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;
import com.ibm.as400.access.AS400;

public class MyConnection {
	private Properties properties;
	private String system;
	private String userName;
	private String password;

	public MyConnection(Properties properties) {
		super();
		this.properties = properties;
	}

	public void load(Properties props) {
		system = props.getProperty("system", "SYSTEM");
		userName = props.getProperty("userName", "USERNAME");
		password = props.getProperty("password", "PASSWORD");
		properties = props;
	}
	
	public void store(FileOutputStream out) throws Exception {
		Properties props = new Properties();
		if (system != null) {
			props.setProperty("system", system);
		}
		if (userName != null) {
			props.setProperty("userName", userName);
		}
		if (password != null) {
			props.setProperty("password", password);
		}
		Enumeration<?> enumeration = properties.propertyNames();
		while(enumeration.hasMoreElements()) {
			String propName = (String)enumeration.nextElement();
			if (props.getProperty(propName) == null) props.setProperty(propName, properties.getProperty(propName));
		}
		props.store(out,  system);
	}
}
