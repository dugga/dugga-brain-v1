package com.dugga.stayalive;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;

public class StayAlivePropertiesDialog extends Dialog {

	private Slider slider;
	private Label sliderValueLabel;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public StayAlivePropertiesDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(3, false));

		Label refreshIntervalLabel = new Label(container, SWT.NONE);
		refreshIntervalLabel.setText("Refresh interval");

		slider = new Slider(container, SWT.NONE);
		GridData gd_slider = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_slider.widthHint = 250;
		slider.setLayoutData(gd_slider);
		slider.setToolTipText("Minutes");
		slider.setPageIncrement(15);
		slider.setMaximum(480);
		slider.setMinimum(1);
		slider.setSelection(getRefreshInterval());
		slider.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				sliderValueLabel.setText(String.valueOf(slider.getSelection()) + " minutes");
			}
		});

		sliderValueLabel = new Label(container, SWT.NONE);
		sliderValueLabel.setText(String.valueOf(slider.getSelection()) + " minutes");

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		StayAliveProperties defaultStayAliveProperties = new StayAliveProperties("StayAliveDefaults");
		defaultStayAliveProperties.setProperty("refreshInterval", String.valueOf(slider.getSelection()));
		super.okPressed();
	}

	/*
	 * (non-Javadoc) set the title of the window
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Stay Alive Properties");
	}

	/*
	 * Gets the refresh interval from the properties file.  Default value will be 30 minutes.
	 */
	private int getRefreshInterval() {
		StayAliveProperties defaultStayAliveProperties = new StayAliveProperties("StayAliveDefaults");
		String refreshInterval =  defaultStayAliveProperties.getProperty("refreshInterval", "30");
		return Integer.valueOf(refreshInterval);

	}

}













