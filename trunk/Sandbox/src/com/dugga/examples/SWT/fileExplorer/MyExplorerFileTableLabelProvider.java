package com.dugga.examples.SWT.fileExplorer;

import java.io.File;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.dugga.examples.SWT.fileExplorer.utility.*;

public class MyExplorerFileTableLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener iLableProviderListener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object obj, String s) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener iLabelProviderListener) {
	}

	@Override
	public Image getColumnImage(Object obj, int columnIndex) {
		if (columnIndex != 0) {
			return null;
		}
		if (((File)obj).isDirectory()) {
			return Util.getImageRegistry().get("folder");
		} else {
			return Util.getImageRegistry().get("file");
		}
	}

	@Override
	public String getColumnText(Object obj, int columnIndex) {
		if (columnIndex == 0) {
			return ((File)obj).getName();
		}
		if (columnIndex == 1) {
			return "" + ((File)obj).length();
		}
		return "";
	}

}
