package com.dugga.sampleRSEHostSelection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.core.model.Host;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.ui.SystemBasePlugin;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * The SampleRSEHostSelection class controls the plug-in life cycle
 */
public class SampleRSEHostSelection extends SystemBasePlugin implements IObjectActionDelegate {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.dugga.sampleRSEHostSelection.SampleRSEHostSelection"; //$NON-NLS-1$

	// Selected hosts
	private List<IHost> selectedHosts;
	
	/**
	 * The constructor
	 */
	public SampleRSEHostSelection() {
		selectedHosts = new ArrayList<IHost>();
	}

	@Override
	protected void initializeImageRegistry() {
	}

	@Override
	public void run(IAction arg0) {
		Iterator<IHost> it = selectedHosts.iterator();
		while (it.hasNext()) {
			Host host = (Host)it.next();
			MessageDialog.openInformation(getActiveWorkbenchShell(), "SampleRSEHostSelection", "Selected IBM i host is " + host.getName());
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		selectedHosts.clear();
		// store the selected hosts
		Iterator theSet = ((IStructuredSelection) selection).iterator();
		while (theSet.hasNext()) {
			Object obj = theSet.next();
			if (obj instanceof IHost) {
					selectedHosts.add((IHost) obj);
				}
			}
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
	}
}
