package com.dugga.examples.SWT.selectionDialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;

/**
 * @author davie
 *    Produces a calendar for selection.
 *    Must pass back primitive types, so here we set int types for year, month, day
 *     
 */

public class DateSelectionDialog extends Dialog {
	private DateTime calender;
	private DateTime selectedDate;
	private int selectedYear;
	private int selectedMonth;
	private int selectedDay;
	
	
	public DateSelectionDialog(Shell shell) {
		super(shell);
	}
	
	public Control createDialogArea(Composite parent) {
		
		Composite composite = (Composite)super.createDialogArea(parent);
		parent.getShell().setText("Select date");
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		calender = new DateTime(composite, SWT.CALENDAR);
		calender.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource() instanceof DateTime) {
					selectedDate = (DateTime)e.getSource();
					}
			}
		});
		
		return composite;
	}
	
	@Override
	protected void okPressed() {
		if (selectedDate != null) {
			selectedYear = selectedDate.getYear();
			selectedMonth = selectedDate.getMonth();
			selectedDay = selectedDate.getDay();
		} else {
			selectedYear = calender.getYear();
			selectedMonth = calender.getMonth();
			selectedDay = calender.getDay();
		}
		super.okPressed();
	}

	public int getSelectedYear() {
		return selectedYear;
	}

	public int getSelectedMonth() {
		return selectedMonth;
	}

	public int getSelectedDay() {
		return selectedDay;
	}

}
