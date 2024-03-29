package com.dugga.stayalive;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.rse.core.IRSEInitListener;
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.core.model.Host;
import org.eclipse.rse.core.model.SystemStartHere;
import org.eclipse.rse.ui.RSESystemTypeAdapter;
import org.eclipse.rse.ui.SystemBasePlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class StayAliveView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.dugga.stayalive";

	private TableViewer viewer;
	private Action toggleActivation;
	private Action doubleClickAction;
	private Action refresh;
	private Action startMonitor;
	private Action stopMonitor;
	private Action setProperties;
	private StayAliveJob stayAliveJob;
	private ArrayList<StayAliveHost> stayAliveHosts = new ArrayList<StayAliveHost>();

	/**
	 * The constructor.
	 */
	public StayAliveView() {
		// Listens for the RSE to be available, then refreshes the view
		RSECorePlugin.addInitListener(new IRSEInitListener() {
			@Override
			public void phaseComplete(int phase) {
				if (phase == 0) {
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							refreshView();
						}
					});
				}
			}
		});
	}

	/* 
	 * create the columns
	 */
	private TableViewerColumn createTableViewerColumn(String title, int width, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(width);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	private void createColumns(Composite parent, TableViewer viewer2) {
		String[] titles = { "Connection", "Include in Monitor", "Monitoring", "Source Library", "Source File", "Source Member" };
		int[] bounds = { 200, 150, 150, 150, 150, 150 };

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col = createTableViewerColumn(titles[4], bounds[4], 4);
		col = createTableViewerColumn(titles[5], bounds[5], 5);
	}

	/*
	 * Gets the content for the table from RSE connections already defined.
	 */
	class ViewContentProvider implements IStructuredContentProvider {
		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		@Override
		public void dispose() {
		}
		@Override
		public Object[] getElements(Object parent) {
			return SystemStartHere.getConnectionsBySystemType("IBM i");
		}
	}

	/*
	 * Using the RSE host list, create our column data
	 */
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int columnIndex) {
			String result = "";
			if (obj instanceof Host) {
				Host host = (Host)obj;
				StayAliveHost stayAliveHost = new StayAliveHost(host);
				switch (columnIndex) {
				case 0: 
					result = host.getAliasName();
					if (stayAliveHost.getInclude()) {
						stayAliveHosts.add(stayAliveHost);
					}
					break;
				case 1: 
					result = String.valueOf(stayAliveHost.getInclude());
					break;
				case 2: 
					result = String.valueOf(isMonitored(host.getAliasName()));
					break;
				case 3: 
					result = stayAliveHost.getSourceLibrary();
					break;
				case 4: 
					result = stayAliveHost.getSourceFile();
					break;
				case 5: 
					result = stayAliveHost.getSourceMember();
					break;
				default:
					//should not reach here
					result = "";
				}
			}
			return result;
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			if (index == 0) {
				Host host = (Host)obj;
				RSESystemTypeAdapter adapter = (RSESystemTypeAdapter) host.getSystemType().getAdapter(RSESystemTypeAdapter.class);
				ImageDescriptor id = adapter.getImageDescriptor(host.getSystemType());
				return id.createImage();
			} 
			return null;
		}
		@Override
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/*
	 * Sorting - not yet implemented
	 */
	class NameSorter extends ViewerSorter {
	}

	/*
	 * Create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		stayAliveHosts.clear();
		viewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		setContentDescription("Stay Alive View");
		createColumns(parent, viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setToolTipText("Stay Alive Monitor");

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	/*
	 * various actions that can be done
	 */
	private void makeActions() {
		// toggle the selected items activation
		toggleActivation = new Action() {
			@Override
			public void run() {
				ISelection selection = viewer.getSelection();
				Iterator selectionIterator = ((IStructuredSelection)selection).iterator();
				while (selectionIterator.hasNext()) {
					Object obj = selectionIterator.next();
					if (obj instanceof Host) {
						Host selectedHost = (Host) obj;
						toggleInclude(selectedHost.getAliasName());
					}
				}
				refreshView();
			}
		};
		toggleActivation.setText("Toggle Activation");
		toggleActivation.setToolTipText("Toggle Activation");
		toggleActivation.setImageDescriptor(Activator.getImageDescriptor(Activator.IMAGE_TOGGLE));

		// double clicked an item
		doubleClickAction = new Action() {
			@Override
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				if (obj instanceof Host) {
					Host selectedHost = (Host) obj;
					StayAliveHost stayAliveHost = new StayAliveHost(selectedHost);
					StayAliveHostPropertiesDialog hostPropertiesDialog = new StayAliveHostPropertiesDialog(SystemBasePlugin.getActiveWorkbenchShell(), stayAliveHost);
					hostPropertiesDialog.open();
					refreshView();
				}
			}
		};
		doubleClickAction.setText("Connection Properties");
		doubleClickAction.setToolTipText("Connection Properties");

		// refresh the table
		refresh = new Action() {
			@Override
			public void run() {
				refreshView();
			}
		};
		refresh.setText("Refresh");
		refresh.setToolTipText("Refresh");
		refresh.setImageDescriptor(Activator.getImageDescriptor(Activator.IMAGE_REFRESH));

		// Start the monitor
		startMonitor = new Action() {
			@Override
			public void run() {
				if (stayAliveHosts.size() == 0) {
					MessageDialog.openWarning(SystemBasePlugin.getActiveWorkbenchShell(), "Start", "There are no hosts included.");
				} else {
					if (!isStayAliveJobActive()) {
						stayAliveJob = new StayAliveJob();
						stayAliveJob.setHosts(stayAliveHosts);
						stayAliveJob.run();
					}
				}
				refreshView();
			}
		};
		startMonitor.setText("Start Monitor");
		startMonitor.setToolTipText("Start Monitor");
		startMonitor.setEnabled(!isStayAliveJobActive());
		startMonitor.setImageDescriptor(Activator.getImageDescriptor(Activator.IMAGE_RUN));

		// Stop the monitor
		stopMonitor = new Action() {
			@Override
			public void run() {
				if (isStayAliveJobActive()) {
					getStayAliveJob().sleep();
					getStayAliveJob().cancel();
				}
				refreshView();
			}
		};
		stopMonitor.setText("Stop Monitor");
		stopMonitor.setToolTipText("Stop Monitor");
		stopMonitor.setEnabled(isStayAliveJobActive());
		stopMonitor.setImageDescriptor(Activator.getImageDescriptor(Activator.IMAGE_STOP));

		// Set properties
		setProperties = new Action() {
			@Override
			public void run() {
				StayAlivePropertiesDialog stayAlivePropertiesDialog = new StayAlivePropertiesDialog(SystemBasePlugin.getActiveWorkbenchShell());
				stayAlivePropertiesDialog.open();
				refreshView();
			}
		};
		setProperties.setText("Stay Alive Properties");
		setProperties.setToolTipText("Stay Alive Properties");
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				StayAliveView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(toggleActivation);
		manager.add(refresh);
		manager.add(startMonitor);
		manager.add(stopMonitor);
		manager.add(new Separator());
		manager.add(setProperties);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(toggleActivation);
		manager.add(doubleClickAction);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(toggleActivation);
		manager.add(refresh);
		manager.add(startMonitor);
		manager.add(stopMonitor);
	}


	/*
	 * Is there a stayAliveJob active?
	 */
	private boolean isStayAliveJobActive() {
		return !(getStayAliveJob() == null);
	}

	/*
	 * Finds the stayAliveJob 
	 */
	private Job getStayAliveJob() {
		Job stayAliveJob = null;
		Job[] jobs = Job.getJobManager().find(null);
		for (int i = 0; i < jobs.length; i++) {
			if (jobs[i].getName().equals("StayAliveJob")) {
				stayAliveJob = jobs[i];
				break;
			}
		}
		return stayAliveJob;
	}

	/*
	 * Read the active job to see if a particular system is being monitored.
	 */
	private boolean isMonitored(String hostName) {
		if (getStayAliveJob() == null) return false;
		
		QualifiedName jobHosts = new QualifiedName("com.dugga.StayAlive", "activeHosts");
		Object jobProps = getStayAliveJob().getProperty(jobHosts);
		ArrayList<StayAliveHost> jobActiveHosts = (ArrayList<StayAliveHost>)getStayAliveJob().getProperty(jobHosts);
		if (jobActiveHosts == null || jobActiveHosts.size() == 0) return(false);
		Iterator listIter = jobActiveHosts.iterator();
		while (listIter.hasNext()) {
			StayAliveHost activeHost = (StayAliveHost) listIter.next();
			if (activeHost.getHost().getAliasName().equals(hostName)) {
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * Toggles the include flag in the system properties file
	 */
	private void toggleInclude(String hostName) {
		StayAliveProperties hostProperties = new StayAliveProperties(hostName);
		if (hostProperties.getProperty("include", "false").equals("true")) {
			hostProperties.setProperty("include", "false");
		} else {
			hostProperties.setProperty("include", "true");
		}
	}

	/*
	 * refresh the view
	 */
	private void refreshView() {
		stayAliveHosts.clear();
		if (!isStayAliveJobActive()) {
			startMonitor.setEnabled(true);
			stopMonitor.setEnabled(false);
		} else {
			startMonitor.setEnabled(false);
			stopMonitor.setEnabled(true);
		}
		viewer.refresh();
	}

}