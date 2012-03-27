package com.dugga.examples.SWT.IBMi.core;

import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;
import com.ibm.as400.access.AS400;

public class MyConnection {
	private Properties properties;
	private String system;
	private String userProfile;
	private String userPassword;

	public MyConnection(Properties properties) {
		super();
		this.properties = properties;
	}

	public void load(Properties props) {
		system = props.getProperty("system", "SYSTEM");
		userProfile = props.getProperty("userProfile", "USER");
		userPassword = props.getProperty("userPassword", "PASSWORD");
		properties = props;
	}
	
	public void store(FileOutputStream out) throws Exception {
		Properties props = new Properties();
		if (system != null) {
			props.setProperty("system", system);
		}
		if (userProfile != null) {
			props.setProperty("userProfile", userProfile);
		}
		if (userPassword != null) {
			props.setProperty("userPassword", userPassword);
		}
		Enumeration<?> enumeration = properties.propertyNames();
		while(enumeration.hasMoreElements()) {
			String propName = (String)enumeration.nextElement();
			if (props.getProperty(propName) == null) props.setProperty(propName, properties.getProperty(propName));
		}
		props.store(out,  system);
	}
}
