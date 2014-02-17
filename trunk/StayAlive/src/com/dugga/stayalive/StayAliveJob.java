package com.dugga.stayalive;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.ui.PlatformUI;

import com.ibm.as400.access.SystemValue;
import com.ibm.etools.iseries.rse.ui.resources.QSYSEditableRemoteSourceFileMember;
import com.ibm.etools.iseries.services.qsys.api.IQSYSMember;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;

public class StayAliveJob {
	private ArrayList<StayAliveHost> hosts = new ArrayList<StayAliveHost>();
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
	 * @param stayAliveHosts Host[]
	 */
	public void setHosts(ArrayList<StayAliveHost> stayAliveHosts) {
		this.hosts.addAll(stayAliveHosts);
	}

	protected void run() {
		Job job = new Job("StayAliveJob") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				if (!PlatformUI.isWorkbenchRunning()) {
					System.out.println("StayAlive - workbench is not active.");
					messageConsole.write("StayAlive - workbench is not active.");
					cancel();
					return Status.CANCEL_STATUS;
				}
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
					
					Iterator<StayAliveHost> hostIterator = hosts.iterator();
					while (hostIterator.hasNext()) {
						StayAliveHost stayAliveHost = hostIterator.next();
						IBMiConnection ibmi = IBMiConnection.getConnection(stayAliveHost.getHost());
						if (ibmi.isConnected()) {
							messageConsole.write("System: " + ibmi.getHostName() + " connected at system time: " + getSystemTime(ibmi));
							readAndWriteSourceMember(stayAliveHost);
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
	
	private void readAndWriteSourceMember(StayAliveHost stayAliveHost) {
		IBMiConnection ibmi = IBMiConnection.getConnection(stayAliveHost.getHost());
		IQSYSMember member;
		QSYSEditableRemoteSourceFileMember sourceMember = null;
		try {
			member = ibmi.getMember(stayAliveHost.getSourceLibrary(), stayAliveHost.getSourceFile(), stayAliveHost.getSourceMember(), new NullProgressMonitor());
			sourceMember = new QSYSEditableRemoteSourceFileMember(member);
		} catch(Exception e) {
			 messageConsole.write(e.getMessage());
			return;
		}
		
		messageConsole.write("Retreiving work member " + stayAliveHost.getSourceMember() + " from " + stayAliveHost.getSourceLibrary() + "/" + stayAliveHost.getSourceFile() + ".");
		try {
			sourceMember.download(new NullProgressMonitor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		appendDateTime(sourceMember);
		messageConsole.write("Updating work member.");
		sourceMember.doImmediateSaveAndUpload();
		messageConsole.write("done");
	}


	private void appendDateTime(QSYSEditableRemoteSourceFileMember sourceMember) {
		IFile file = sourceMember.getAndCreateLocalResource();
		String filename= (ResourcesPlugin.getWorkspace().getRoot().getLocation().toString()) + (file.getFullPath().toString());
		String time = getSystemTime(sourceMember.getISeriesConnection());
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		
		try {
			String newLine = nextSequenceNumber(filename) + sdf.format(now) + " Last updated at " + time + "\r\n";
			FileWriter fw = new FileWriter(filename,true); //the true will append the new data
			fw.write(newLine);//appends the string to the file
			fw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}

	private String nextSequenceNumber(String filename) {
		RandomAccessFile fileHandler = null;
		File file = new File(filename);
		String lastLine = "";
		try {
			fileHandler = new RandomAccessFile( file, "r" );
			long fileLength = fileHandler.length() - 1;
			StringBuilder sb = new StringBuilder();

			for(long filePointer = fileLength; filePointer != -1; filePointer--){
				fileHandler.seek( filePointer );
				int readByte = fileHandler.readByte();

				if( readByte == 0xA ) {
					if( filePointer == fileLength ) {
						continue;
					} else {
						break;
					}
				} else if( readByte == 0xD ) {
					if( filePointer == fileLength - 1 ) {
						continue;
					} else {
						break;
					}
				}

				sb.append( ( char ) readByte );
			}

			lastLine = sb.reverse().toString();
		} catch( java.io.FileNotFoundException e ) {
			e.printStackTrace();
		} catch( java.io.IOException e ) {
			e.printStackTrace();
		} finally {
			if (fileHandler != null )
				try {
					fileHandler.close();
				} catch (IOException e) {
					/* ignore */
				}
		}
		if (lastLine.equals("")) {
			lastLine = "000000";
		}
		
		int seq = Integer.parseInt(lastLine.substring(0, 6)) + 1;
		
		String longString = "000000" + String.valueOf(seq);
		return longString.substring(longString.length()-6);
	}
}
