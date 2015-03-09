package com.dugga.ece;

import org.eclipse.core.expressions.PropertyTester;

public class PropertyTester1 extends PropertyTester {
	public static final String PROPERTY_NAMESPACE = "EclipseCommandExtension";
	public static final String PROPERTY_ISSOURCE = "isSource";
	
	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		
		if (PROPERTY_ISSOURCE.equals(property)) {
			try {
				return (receiver.getClass().getDeclaredField("type").get(receiver).equals("SOURCE"));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
			
		return false;
	}
}
