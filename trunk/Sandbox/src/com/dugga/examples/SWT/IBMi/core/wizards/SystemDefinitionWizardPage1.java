package com.dugga.examples.SWT.IBMi.core.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class SystemDefinitionWizardPage1 extends WizardPage {
	private Text systemNameText;

	/**
	 * Create the wizard.
	 */
	public SystemDefinitionWizardPage1() {
		super("wizardPage");
		setTitle("System Definition Wizard");
		setDescription("The System Definition Wizard is used to step through the steps of defining your systems.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label systemNameLabel = new Label(container, SWT.NONE);
		systemNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		systemNameLabel.setText("System Name:");
		
		systemNameText = new Text(container, SWT.BORDER);
		systemNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}

}
