package com.dugga.examples.SWT.IBMi.core.wizards;

import org.eclipse.jface.wizard.Wizard;

public class SystemDefinitionWizard extends Wizard {

	public SystemDefinitionWizard() {
		setWindowTitle("New Wizard");
	}

	@Override
	public void addPages() {
		addPage(new SystemDefinitionWizardPage1());
		addPage(new SystemDefinitionWizardPage2());
	}

	@Override
	public boolean performFinish() {
		return false;
	}

}
