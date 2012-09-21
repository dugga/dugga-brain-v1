package com.csdainc.gfi;

//Import required Java Classes
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

//Import relevant Google Classes
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;

//Import required IBM i IFS Classes
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileOutputStream;

public class GA4BListJ {
	
	// Define variables
	private static String email;
	private static String password;
	private static String document;
	private static String directory;
	private static String ibmiName;
	private static IFSFileOutputStream writer;

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {

		// Set passed Argument variables with passed values
		email = args[0];
		password = args[1];
		document = args[2];
		directory = args[3];
		ibmiName = args[4];

		// Prepare IFS directory structure and output file
		createOutputFile();

		// Connect to Google's DocService with DocumentListFeed URL
		try {
			DocsService docsService = new DocsService("Document List");
			docsService.setUserCredentials(email, password);
			
			URL documentListFeedUrl = new URL("https://docs.google.com/feeds/default/private/full");

			// Get the list of documents from Google
			DocumentListFeed documentListFeed = (DocumentListFeed) docsService.getFeed(documentListFeedUrl, DocumentListFeed.class);

			// Iterate through the list returned writing to an output file in
			// the IFS
			List<DocumentListEntry> entries = documentListFeed.getEntries();
			Iterator<DocumentListEntry> iterator = entries.iterator();
			while (iterator.hasNext()) {
				DocumentListEntry entry = (DocumentListEntry) iterator.next();
				if (writer != null) {
					writer.write(entry.getTitle().getPlainText().getBytes());
					writer.write("\n".getBytes());
				} else {
					System.out.println(entry.getTitle().getPlainText());
				}
			}
			writer.close();

		} catch (Exception ex) {
			// Auto-generated catch block
			System.err.println("Exception: " + ex.getMessage());
		}
		
	}

	// Method to prepare directory structure and output file
	private static IFSFileOutputStream createOutputFile() {

		// Set work variables
		AS400 as400 = new AS400(ibmiName);
		if (!directory.trim().endsWith("/")) {
			directory = directory.trim() + "/"; 
		}
		
		// Create folder structure (Ignore errors as folder may already exist)
		IFSFile ifsFile = new IFSFile(as400,directory.trim() + document.trim());
		try {
			ifsFile.mkdirs();
		} catch (IOException e1) {
		}
		
		// Delete previous output (Ignore errors if it doesn't exist)
		try {
			ifsFile.delete();
		} catch (IOException e) {
		}

		// Create a new empty file and Writer for that file
		try {
			ifsFile.createNewFile();
			writer = new IFSFileOutputStream(ifsFile);
		} catch (Exception e) {
			System.out.println("Error in create of output file: " + e.getMessage());
		}
		
		return writer;
	}

	
}
