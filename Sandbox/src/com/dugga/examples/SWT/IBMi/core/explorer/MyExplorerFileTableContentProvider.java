package com.dugga.examples.SWT.IBMi.core.explorer;

import java.io.File;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class MyExplorerFileTableContentProvider implements
		IStructuredContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldObject, Object newObject) {
	}

	@Override
	public Object[] getElements(Object element) {
		Object[] kids = null;
		kids = ((File)element).listFiles();
		return kids == null ? new Object[0] : kids;
	}

}
