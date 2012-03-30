package com.dugga.examples.SWT.IBMi.core.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class SystemDefinitionWizardPage2 extends WizardPage {
	private Text userNameText;
	private Text passwordText;

	/**
	 * Create the wizard.
	 */
	public SystemDefinitionWizardPage2() {
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
		
		Label UserNameLabel = new Label(container, SWT.NONE);
		UserNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		UserNameLabel.setText("User Name:");
		
		userNameText = new Text(container, SWT.BORDER);
		userNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label passwordLabel = new Label(container, SWT.NONE);
		passwordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		passwordLabel.setText("Password:");
		
		passwordText = new Text(container, SWT.BORDER);
		passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}

}
