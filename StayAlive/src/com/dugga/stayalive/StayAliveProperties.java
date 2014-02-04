package com.dugga.stayalive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.resources.ResourcesPlugin;

public class StayAliveProperties {
	private Properties properties = new Properties();
	private File workingDirectory = null;
	private File propertiesFile = null;

	/**
	 * Creates a properties file with the given name in the default plugin location.
	 * Directories will also be created if necessary.
	 * @param fileName
	 */
	public StayAliveProperties(String fileName) {
		workingDirectory = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() + File.separator + ".metadata" + File.separator + ".plugins" + File.separator + "com.dugga.stayalive");
		workingDirectory.mkdirs();

		propertiesFile = new File(workingDirectory + File.separator + fileName + ".props");

		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(propertiesFile);
			properties.load(fileInputStream);
			fileInputStream.close();
		} catch (Exception e) {
			save();
		}
	}

	/**
	 * Saves the properties after they have been modified.
	 */
	public void save() {
		try {
			propertiesFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(propertiesFile); 
			properties.store(fileOutputStream, propertiesFile.getName() + " stayAlive properties");
			fileOutputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	/**
	 * Gets a specific property from the properties file
	 * @param key the key of the property
	 * @return String value of the property
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * Gets a specific property from the properties file.
	 * If it is not found the default value will be returned.
	 * @param key the key of the property
	 * @param defaultValue the default value that should be returned if no value is found
	 * @return String value of the property
	 */
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * Sets the specified property to the specified value, then saves the properties file.
	 * @param key the key of the property
	 * @param value the value of the property
	 */
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
		save();
	}

}
