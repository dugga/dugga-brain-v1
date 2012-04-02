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
		addStatusLine();
	}

	@Override
	protected Control createContents(Composite parent) {
		getShell().setText("JFace File Explorer");
			
		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL | SWT.NULL);
		
		TreeViewer treeViewer = new TreeViewer(sashForm);
		treeViewer.setContentProvider(new MyExplorerTreeContentProvider());
		treeViewer.setLabelProvider(new MyExplorerTreeLabelProvider());
		treeViewer.setInput(new File("C:\\"));
		treeViewer.addFilter(new AllowOnlyFoldersFilter());
		
		final TableViewer tableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tableViewer.setContentProvider(new MyExplorerFileTableContentProvider());
		tableViewer.setLabelProvider(new MyExplorerFileTableLabelProvider());
		tableViewer.setSorter(new FileSorter());
		
		TableColumn tableColumn = new TableColumn(tableViewer.getTable(), SWT.LEFT);
		tableColumn.setText("Name");
		tableColumn.setWidth(200);
		
		tableColumn = new TableColumn(tableViewer.getTable(), SWT.RIGHT);
		tableColumn.setText("Size");
		tableColumn.setWidth(100);
		
		tableViewer.getTable().setHeaderVisible(true);
		
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object selectedFile = selection.getFirstElement();
				tableViewer.setInput(selectedFile);
			}
		});
		
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				setStatus("Number of items selected is " + selection.size());
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
