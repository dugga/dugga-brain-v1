package com.dugga.examples.SWT;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Button;
import org.eclipse.jface.window.Window;

public class SWTApplicationWindow {

	protected Shell sampleShell;
	private DateTime dateTime;
	private Label statusLabel;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SWTApplicationWindow window = new SWTApplicationWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		sampleShell.pack();
		sampleShell.open();
		sampleShell.layout();
		while (!sampleShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		sampleShell = new Shell();
		sampleShell.setSize(514, 245);
		sampleShell.setText("Sample Window with selection Dialog");
		GridLayout gl_sampleShell = new GridLayout();
		sampleShell.setLayout(gl_sampleShell);
		
		Menu topMenuBar = new Menu(sampleShell, SWT.BAR);
		sampleShell.setMenuBar(topMenuBar);
		
		MenuItem fileMenuItem = new MenuItem(topMenuBar, SWT.CASCADE);
		fileMenuItem.setText("File");
		
		Menu exitMenu = new Menu(fileMenuItem);
		fileMenuItem.setMenu(exitMenu);
		
		MenuItem exitMenuItem = new MenuItem(exitMenu, SWT.NONE);
		exitMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MessageBox fileExitMessageBox = new MessageBox(sampleShell);
				fileExitMessageBox.setText("Exit");
				fileExitMessageBox.setMessage("File/Exit selected");
				fileExitMessageBox.open();
			}
		});
		exitMenuItem.setText("Exit");
		
		MenuItem helpMenuItem = new MenuItem(topMenuBar, SWT.CASCADE);
		helpMenuItem.setText("Help");
		
		Menu aboutMenu = new Menu(helpMenuItem);
		helpMenuItem.setMenu(aboutMenu);
		
		MenuItem aboutMenuItem = new MenuItem(aboutMenu, SWT.NONE);
		aboutMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox helpMessage = new MessageBox(sampleShell);
				helpMessage.setText("About");
				helpMessage.setMessage("Just a test of an applicaiton window.");
				helpMessage.open();		
			}
		});
		aboutMenuItem.setText("About");
		
		Label separatorLabel = new Label(sampleShell, SWT.SEPARATOR | SWT.HORIZONTAL);
		separatorLabel.setLayoutData(new GridData(SWT.FILL , SWT.BOTTOM, true, false, 1, 1));
		
		Composite dateComposite = new Composite(sampleShell, SWT.NONE);
		dateComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		dateComposite.setLayout(new GridLayout(3, false));
		
		Label selectDateLabel = new Label(dateComposite, SWT.NONE);
		selectDateLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		selectDateLabel.setText("Select a Date:");
		
		dateTime = new DateTime(dateComposite, SWT.BORDER);
		dateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button selectDateButton = new Button(dateComposite, SWT.NONE);
		selectDateButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		selectDateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DateSelectionDialog dateSelectionDialog = new DateSelectionDialog(sampleShell);
				dateSelectionDialog.create();
				dateSelectionDialog.createDialogArea(sampleShell);
				if (dateSelectionDialog.open() == Window.OK) {
					dateTime.setDate(dateSelectionDialog.getSelectedYear(), dateSelectionDialog.getSelectedMonth(), dateSelectionDialog.getSelectedDay());
					statusLabel.setText("OK pressed");
				} else {
					statusLabel.setText("Cancel pressed");
				}
			}
		});
		selectDateButton.setText("Select Date");
		new Label(sampleShell, SWT.NONE);
		
		statusLabel = new Label(sampleShell, SWT.BORDER);
		statusLabel.setLayoutData(new GridData(SWT.FILL , SWT.BOTTOM, true, true, 1, 1));
	}

}
