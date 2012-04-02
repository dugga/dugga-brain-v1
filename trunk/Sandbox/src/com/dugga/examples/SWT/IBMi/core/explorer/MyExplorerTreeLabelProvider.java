package com.dugga.examples.SWT.IBMi.core.explorer;

import java.io.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;

import com.dugga.examples.SWT.IBMi.core.utility.Util;

public class MyExplorerTreeLabelProvider  extends LabelProvider {
	public String getText(Object element) {
		return ((File)element).getName();
	}
	
	public Image getImage(Object element) {
		if (((File)element).isDirectory()) {
			return Util.getImageRegistry().get("folder");
		} else {
			return Util.getImageRegistry().get("file");
		}
	}

}
