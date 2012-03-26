package com.dugga.examples.SWT.dogShowEntryWindow;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DogShowEntryWindow extends ApplicationWindow {
	private Text dogsNameText;
	private Text ownerNameText;
	private Text ownerPhoneText;
	private List categoriesList;
	private Image dogImage;

	/**
	 * Create the application window.
	 */
	public DogShowEntryWindow() {
		super(null);
		createActions();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		
		Label dogsNameLabel = new Label(container, SWT.NONE);
		dogsNameLabel.setText("Dog's Name:");
		
		dogsNameText = new Text(container, SWT.BORDER);
		dogsNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label breedLabel = new Label(container, SWT.NONE);
		breedLabel.setText("Breed:");
				
		Combo breedSelectionCombo = new Combo(container, SWT.NONE);
		breedSelectionCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		breedSelectionCombo.setItems(new String[] {"Collie", "Pitbull", "Poodle", "Scottie", "Black Lab"});
		
		Label categoriesLabel = new Label(container, SWT.NONE);
		categoriesLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		categoriesLabel.setText("Categories");
		
		Label photoLabel = new Label(container, SWT.NONE);
		photoLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		photoLabel.setText("Photo:");
		
		Canvas dogPhotoCanvas = new Canvas(container, SWT.NONE);
		dogPhotoCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				if (dogImage != null) {
					event.gc.drawImage(dogImage, 0, 0);
				}
			}
		});
		GridData gd_photoCanvas = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3);
		gd_photoCanvas.widthHint = 80;
		gd_photoCanvas.heightHint = 80;
		dogPhotoCanvas.setLayoutData(gd_photoCanvas);
		
		categoriesList = new List(container, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		categoriesList.setItems(getList());
		GridData gd_categoriesList = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 4);
		int listHeight = categoriesList.getItemHeight() * 12;
		Rectangle trim = categoriesList.computeTrim(0, 0, 0, listHeight);
		gd_categoriesList.heightHint = trim.height;
		categoriesList.setLayoutData(gd_categoriesList);
		
		Button browseButton = new Button(container, SWT.PUSH);
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				String fileName = new FileDialog(getShell()).open();
				if (fileName != null) {
					dogImage = new Image(Display.getCurrent(), fileName);
				}
			}
		});
		browseButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		browseButton.setText("Browse...");
		
		Button deleteButton = new Button(container, SWT.PUSH);
		deleteButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false, 1, 1));
		deleteButton.setText("Delete");
		
		Group ownerInfoGroup = new Group(container, SWT.NONE);
		ownerInfoGroup.setText("Owner Info");
		ownerInfoGroup.setLayout(new GridLayout(2, false));
		ownerInfoGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		{
			Label ownderNameLable = new Label(ownerInfoGroup, SWT.NONE);
			GridData gd_ownderNameLable = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			gd_ownderNameLable.widthHint = 65;
			ownderNameLable.setLayoutData(gd_ownderNameLable);
			ownderNameLable.setText("Name:");
		
			ownerNameText = new Text(ownerInfoGroup, SWT.BORDER);
			ownerNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Label ownerPhoneLabel = new Label(ownerInfoGroup, SWT.NONE);
			ownerPhoneLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			ownerPhoneLabel.setText("Phone:");
			
			ownerPhoneText = new Text(ownerInfoGroup, SWT.BORDER);
			ownerPhoneText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
		
		Button enterButton = new Button(container, SWT.NONE);
		enterButton.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false, 3, 1));
		enterButton.setText("Enter");
		

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			DogShowEntryWindow window = new DogShowEntryWindow();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Dog Show Entry");
	}

	public String[] getList() {
		String[] list = new String[] {
			"Best of Breed",  
			"Prettiest Female",
			"Handsomest Male", 
			"Best Dressed", 
			"Fluffiest Ears",
			"Most Colors",
			"Best Performer",
			"Loudest Bark",
			"Best Behaved",
			"Prettiest Eyes",
			"Most Hair",
			"Logest Tail",
			"Cutest Trick"};
		return list;
	}
}
