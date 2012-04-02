package com.dugga.examples.SWT.fileExplorer.utility;

import java.net.*;
import org.eclipse.jface.resource.*;

public class Util {
	private static ImageRegistry imageRegistry;
	public static URL newURL(String urlName) {
		try {
			return new URL(urlName);
		} catch(MalformedURLException e) {
			throw new RuntimeException("Malformed URL " + urlName, e);
		}
	}

	public static ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
			imageRegistry.put("folder", ImageDescriptor.createFromURL(newURL("file:icons/folder.gif")));
			imageRegistry.put("file", ImageDescriptor.createFromURL(newURL("file:icons/file.gif")));
		}
		return imageRegistry;
	}
}
