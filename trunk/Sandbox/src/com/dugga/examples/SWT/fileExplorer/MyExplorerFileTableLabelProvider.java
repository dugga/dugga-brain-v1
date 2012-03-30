package com.dugga.examples.SWT.fileExplorer;

import java.io.File;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class MyExplorerFileTableLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

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
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}

	@Override
	public String getColumnText(Object obj, int i) {
		return ((File)obj).getName();
	}

}
