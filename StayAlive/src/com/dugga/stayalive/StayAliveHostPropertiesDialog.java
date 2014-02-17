package com.dugga.stayalive;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class StayAliveHostPropertiesDialog extends Dialog {

	private StayAliveHost host;
	private Text sourceFile;
	private Text sourceLibrary;
	private Text sourceMember;
	private Button include;
	private Color red = Display.getCurrent().getSystemColor(SWT.COLOR_RED);

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public StayAliveHostPropertiesDialog(Shell parentShell, StayAliveHost host) {
		super(parentShell);
		this.host = host;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));

		Label sourceLibraryLabel = new Label(container, SWT.NONE);
		sourceLibraryLabel.setText("Library:");
		sourceLibrary = new Text(container, SWT.BORDER);
		sourceLibrary.setTextLimit(10);
		sourceLibrary.setText(host.getSourceLibrary());
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		sourceLibrary.setLayoutData(gd);
		sourceLibrary.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				e.text = e.text.toUpperCase();
			}
		});
		KeyListener keyListener = new KeyListener() {
			@Override
			public void keyReleased(KeyEvent event) {
				sourceLibrary.setBackground(null);
				sourceFile.setBackground(null);
				sourceMember.setBackground(null);
				getButton(IDialogConstants.OK_ID).setEnabled(true);
				
				if (sourceLibrary.getText().toString().trim().equals("QTEMP") ||
					sourceLibrary.getText().toString().trim().equals("")) {
						getButton(IDialogConstants.OK_ID).setEnabled(false);
						sourceLibrary.setBackground(red);
				}
				if (sourceFile.getText().toString().trim().equals("")) {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
					sourceFile.setBackground(red);
				}
				if (sourceMember.getText().toString().trim().equals("")) {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
					sourceMember.setBackground(red);
				}
			}
			@Override
			public void keyPressed(KeyEvent event) {
			}
		};
		sourceLibrary.addKeyListener(keyListener);
				
		Label sourceFileLabel = new Label(container, SWT.NONE);
		sourceFileLabel.setText("File:");
		sourceFile = new Text(container, SWT.BORDER);
		sourceFile.setTextLimit(10);
		sourceFile.setText(host.getSourceFile());
		sourceFile.setLayoutData(gd);
		sourceFile.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				e.text = e.text.toUpperCase();
			}
		});
		sourceFile.addKeyListener(keyListener);
		
		Label sourceMemberLabel = new Label(container, SWT.NONE);
		sourceMemberLabel.setText("Member:");
		sourceMember = new Text(container, SWT.BORDER);
		sourceMember.setTextLimit(10);
		sourceMember.setLayoutData(gd);
		sourceMember.setText(host.getSourceMember());
		sourceMember.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				e.text = e.text.toUpperCase();
			}
		});
		sourceMember.addKeyListener(keyListener);

		Label includeLabel = new Label(container, SWT.NONE);
		includeLabel.setText("Include in Monitor:");
		include = new Button(container, SWT.CHECK);
		include.setSelection(host.getInclude());
		
		
		return container;
	}

	private void addMyListener() {
		
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
	protected Button getButton(int id) {
		// TODO Auto-generated method stub
		return super.getButton(id);
	}

	@Override
	protected void okPressed() {
		host.setSourceFile(sourceFile.getText());
		host.setSourceLibrary(sourceLibrary.getText());
		host.setSourceMember(sourceMember.getText());
		host.setInclude(include.getSelection());
		super.okPressed();
	}

	/*
	 * (non-Javadoc) set the title of the window
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Stay Alive Host Properties");
	}

}













