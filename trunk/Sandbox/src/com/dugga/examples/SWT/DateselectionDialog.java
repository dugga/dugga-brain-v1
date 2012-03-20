package com.dugga.examples.SWT;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

public class DateselectionDialog extends Dialog {
	private DateTime calender;
	private DateTime selectedDate;
	
	public DateselectionDialog(Shell shell) {
		super(shell);
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		super.create();
	}

	public Control createDialogArea(Composite parent) {
		
		Composite composite = (Composite)super.createDialogArea(parent);
		parent.getShell().setText("Select date");
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		calender = new DateTime(composite, SWT.CALENDAR);
		
		return composite;
	}
	
	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}


	private void saveInput() {
		selectedDate = calender;
		
	}

	public DateTime getSelectedDate() {
		return selectedDate;
	}
}
