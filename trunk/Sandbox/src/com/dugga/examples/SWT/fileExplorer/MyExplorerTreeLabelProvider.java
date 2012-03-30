package com.dugga.examples.SWT.fileExplorer;

import java.io.*;
import org.eclipse.jface.viewers.*;

public class MyExplorerTreeLabelProvider  extends LabelProvider {
	public String getText(Object element) {
		return ((File)element).getName();
	}

}
