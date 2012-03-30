package com.dugga.examples.SWT.IBMi.core.explorer;

import java.io.File;

import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.*;

public class MyExplorer extends ApplicationWindow {
	
	/**
	 * Create the application window.
	 */
	public MyExplorer() {
		super(null);
	}

	@Override
	protected Control createContents(Composite parent) {
		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL | SWT.NULL);
		
		TreeViewer treeViewer = new TreeViewer(sashForm);
		treeViewer.setContentProvider(new MyExplorerTreeContentProvider());
		treeViewer.setLabelProvider(new MyExplorerTreeLabelProvider());
		treeViewer.setInput(new File("C:\\"));
		
		final TableViewer tableViewer = new TableViewer(sashForm, SWT.BORDER);
		tableViewer.setContentProvider(new MyExplorerFileTableContentProvider());
		tableViewer.setLabelProvider(new MyExplorerFileTableLabelProvider());
		
		TableColumn tableColumn = new TableColumn(tableViewer.getTable(), SWT.LEFT);
		tableColumn.setText("Name");
		tableColumn.setWidth(200);
		tableViewer.getTable().setHeaderVisible(true);
		
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object selectedFile = selection.getFirstElement();
				tableViewer.setInput(selectedFile);
			}
		});
		
		return sashForm;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			MyExplorer myWindow = new MyExplorer();
			myWindow.setBlockOnOpen(true);
			myWindow.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
