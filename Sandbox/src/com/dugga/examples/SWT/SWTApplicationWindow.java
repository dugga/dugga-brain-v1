package com.dugga.examples.SWT;

import java.awt.Dialog;

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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.window.Window;

public class SWTApplicationWindow {

	protected Shell shell;
	private Text text;
	private Text text_1;
	private DateTime dateTime;

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
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		GridLayout gl_shell = new GridLayout();
		shell.setLayout(gl_shell);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MessageBox fileExitMessageBox = new MessageBox(shell);
				fileExitMessageBox.setText("Exit");
				fileExitMessageBox.setMessage("File/Exit selected");
				fileExitMessageBox.open();
			}
		});
		mntmExit.setText("Exit");
		
		MenuItem mntmHelp_1 = new MenuItem(menu, SWT.CASCADE);
		mntmHelp_1.setText("Help");
		
		Menu menu_2 = new Menu(mntmHelp_1);
		mntmHelp_1.setMenu(menu_2);
		
		MenuItem mntmHelp = new MenuItem(menu_2, SWT.NONE);
		mntmHelp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox helpMessage = new MessageBox(shell);
				helpMessage.setText("About");
				helpMessage.setMessage("Just a test of an applicaiton window.");
				helpMessage.open();		
			}
		});
		mntmHelp.setText("Help");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		
		Label lblUserProfile = new Label(composite, SWT.NONE);
		lblUserProfile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUserProfile.setText("User Profile:");
		
		text = new Text(composite, SWT.BORDER);
		text.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent arg0) {
			}
		});
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		
		Label lblPassword = new Label(composite, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText("Password:");
		
		text_1 = new Text(composite, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		
		Label lblStartDate = new Label(composite, SWT.NONE);
		lblStartDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStartDate.setText("Start Date:");
		
		dateTime = new DateTime(composite, SWT.BORDER);
		
		Button btnSelectDate = new Button(composite, SWT.NONE);
		btnSelectDate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DateselectionDialog dateSelectionDialog = new DateselectionDialog(shell);
				dateSelectionDialog.create();
				dateSelectionDialog.createDialogArea(shell);
				if (dateSelectionDialog.open() == Window.OK) {
					dateTime = dateSelectionDialog.getSelectedDate();
				}
			}
		});
		btnSelectDate.setText("Select Date");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Button btnShowResults = new Button(composite, SWT.NONE);
		btnShowResults.setText("Show Results");
		
		TableTreeViewer tableTreeViewer = new TableTreeViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		TableTree tableTree = tableTreeViewer.getTableTree();
		tableTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
}
