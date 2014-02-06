package com.dugga.stayalive;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.rse.core.model.Host;

import com.ibm.as400.access.SystemValue;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;

public class StayAliveJob {
	private ArrayList<Host> hosts = new ArrayList<Host>();
	private boolean running = false;
	private int wait = 300000;
	private String refreshInterval = "";
	private StayAliveConsole messageConsole = new StayAliveConsole();

	/*
	 * Constructor
	 */
	public StayAliveJob() {
		StayAliveProperties defaultStayAliveProperties = new StayAliveProperties("StayAliveDefaults");
		refreshInterval = defaultStayAliveProperties.getProperty("refreshInterval", "30");
		wait = Integer.parseInt(refreshInterval) * 60000; 
	}

	/**
	 * A list of the Hosts that will be monitored
	 * @param activeHosts Host[]
	 */
	public void setHosts(ArrayList<Host> activeHosts) {
		this.hosts.addAll(activeHosts);
	}

	protected void run() {
		Job job = new Job("StayAliveJob") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				QualifiedName qualifiedName = new QualifiedName("com.dugga.StayAlive", "activeHosts");
				setProperty(qualifiedName, hosts);
				Date now = new Date();
				
				if (!running) {
					messageConsole.write("Stay alive monitor started on " + now.toString() + " with a wait time of " + refreshInterval + " minutes.");
					running = true;
				}
				try {
					if (monitor.isCanceled()) {
						messageConsole.write("Stay alive monitor ended.");
						return Status.CANCEL_STATUS;
					}
					
					Iterator<Host> hostIterator = hosts.iterator();
					while (hostIterator.hasNext()) {
						IBMiConnection ibmi = IBMiConnection.getConnection(hostIterator.next());
						if (ibmi.isConnected()) {
							messageConsole.write("System: " + ibmi.getHostName() + " connected at system time: " + getSystemTime(ibmi));
						} else {
							messageConsole.write("System: " + ibmi.getHostName() + " not connected at " + now.toString());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					schedule(wait);
				}
				return Status.OK_STATUS;
			}
		};

		/* Listen to Job's Lifecycle */
		job.addJobChangeListener(new JobChangeAdapter() {

			@Override
			public void done(IJobChangeEvent event) {
				if (event.getResult().getSeverity() == IStatus.CANCEL) {
					messageConsole.write("Stay alive monitor ended.");
				}
			}
		});

		job.setSystem(true);
		job.schedule();
	}

	/*
	 * Retrieves the system value QTIME from the specified system
	 * in HH:mm:ss format
	 */
	private String getSystemTime(IBMiConnection ibmi) {
		SystemValue sysVal = null;
		String qtime = "";
		if (ibmi.isConnected()) {
			try {
				sysVal = new SystemValue(ibmi.getAS400ToolboxObject(), "QTIME");
				qtime = sysVal.getValue().toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return qtime;
	}
}
