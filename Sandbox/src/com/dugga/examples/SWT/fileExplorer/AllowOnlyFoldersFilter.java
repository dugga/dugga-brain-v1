package com.dugga.examples.SWT.fileExplorer;

import java.io.File;
import org.eclipse.jface.viewers.*;

public class AllowOnlyFoldersFilter extends ViewerFilter {

	@Override
	public boolean select(Viewer viewer, Object parent, Object element) {
		return ((File)element).isDirectory();
	}
	
	

}
