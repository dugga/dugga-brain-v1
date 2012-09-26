package com.csdainc.gfi;

//Import relevant Google Classes
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.client.media.ResumableGDataFileUploader;
import com.google.gdata.data.Link;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.util.ServiceException;
import com.google.gdata.client.uploader.FileUploadData;
import com.google.gdata.client.uploader.ProgressListener;
import com.google.gdata.client.uploader.ResumableHttpFileUploader;

//Import required Java Classes
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GA4BSendJ {

	// Define variables
	private static final int MAX_CONCURRENT_UPLOADS = 10;
	private static final int PROGRESS_UPDATE_INTERVAL = 1000;
	private static final int DEFAULT_CHUNK_SIZE = 10000000;
	private static DocsService docsService;
	static PrintStream output;

	/**
	 * Constructor.
	 * 
	 * @param DocsService
	 *            {@link DocsService} for interface to DocsService API service.
	 * @param out
	 *            PrintStream to output status messages to.
	 */
	GA4BSendJ(DocsService docsService, PrintStream out) {
		GA4BSendJ.docsService = docsService;
		GA4BSendJ.output = out;
	}

	public static void main(String[] args) throws IOException,
			ServiceException, InterruptedException {

		// Set passed Argument variables with passed values
		String email = args[0];
		String password = args[1];
		String document = args[2];
		String directory = args[3];

		// Concatenate directory and document name
		if (!directory.trim().endsWith("/")) {
			directory = directory.trim() + "/";
		}
		String fromFile = directory.trim() + document.trim();

		// authenticate
		DocsService docsService = new DocsService("com.csdainc-GFi-v1");
		docsService.setUserCredentials(email, password);

		// process
		GA4BSendJ GA4BSendJ = new GA4BSendJ(docsService, System.out);
		executeUpload(fromFile);
	}

	// Prepare file for upload. Our example only uploads 1 file, but could
	// easily be changed to upload multiple files.
	// Here, we add just the one file to a List, then we process that List.
	private static void executeUpload(String fromFIle) throws IOException,
			ServiceException, InterruptedException {
		int chunkSize = DEFAULT_CHUNK_SIZE;
		List<String> files = Lists.newArrayList();
		files.add(fromFIle);
		String url = "https://docs.google.com/feeds/upload/create-session/default/private/full";
		uploadFiles(url, files, chunkSize);
		output.println("Finished upload");
	}

	/**
	 * Uploads given collection of files. The call blocks until all uploads are
	 * done.
	 * 
	 * @param url
	 *            create-session url for initiating resumable uploads for
	 *            documents API.
	 * @param files
	 *            list of absolute filepaths to files to upload.
	 * @param chunkSize
	 *            max size of each upload chunk.
	 */
	public static Collection<DocumentListEntry> uploadFiles(String url,
			List<String> files, int chunkSize) throws IOException,
			ServiceException, InterruptedException {
		GA4BSendJ.FileUploadProgressListener listener = new GA4BSendJ(
				docsService, output).new FileUploadProgressListener();
		ExecutorService executor = Executors
				.newFixedThreadPool(MAX_CONCURRENT_UPLOADS);
		List<ResumableGDataFileUploader> uploaders = Lists.newArrayList();
		for (String fileName : files) {
			File file = new File(fileName);
			try {
				DocumentListEntry.MediaType.fromFileName(file.getName()).getMimeType();
			} catch (Exception e) {
				output.println(" Failed to upload: Unsupported file type: " + fileName);
				break;
			}
			String contentType = DocumentListEntry.MediaType.fromFileName(file.getName()).getMimeType();
			MediaFileSource mediaFile = new MediaFileSource(file, contentType);
			ResumableGDataFileUploader uploader = new ResumableGDataFileUploader.Builder(
						docsService, new URL(url), mediaFile, null)
						.title(mediaFile.getName()).chunkSize(chunkSize)
						.executor(executor)
						.trackProgress(listener, PROGRESS_UPDATE_INTERVAL)
						.build();
			uploaders.add(uploader);
		}
		listener.listenTo(uploaders);

		// Start the upload
		for (ResumableGDataFileUploader uploader : uploaders) {
			uploader.start();
		}

		// wait for uploads to complete
		while (!listener.isDone()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ie) {
				listener.printResults();
				throw ie; // rethrow
			}
		}

		// print upload results
		listener.printResults();

		// return list of uploaded entries
		return listener.getUploaded();
	}

	// Obtain information about the file
	private static MediaFileSource getMediaFileSource(String fileName) {
		File file = new File(fileName);
		MediaFileSource mediaFile = new MediaFileSource(file,
				DocumentListEntry.MediaType.fromFileName(file.getName())
						.getMimeType());
		return mediaFile;
	}

	/**
	 * A {@link ProgressListener} implementation to track upload progress. The
	 * listener can track multiple uploads at the same time. Use {@link #isDone}
	 * to check if all uploads are completed and use {@link #getUploaded} to
	 * access results of successful uploads.
	 */
	private class FileUploadProgressListener implements ProgressListener {

		private Collection<ResumableGDataFileUploader> trackedUploaders = Lists
				.newArrayList();
		private int pendingRequests;
		Map<String, DocumentListEntry> uploaded = Maps.newHashMap();
		Map<String, String> failed = Maps.newHashMap();

		boolean processed;

		// initialize
		public FileUploadProgressListener() {
			this.pendingRequests = 0;
		}

		public void listenTo(Collection<ResumableGDataFileUploader> uploaders) {
			this.trackedUploaders.addAll(uploaders);
			this.pendingRequests = trackedUploaders.size();
		}

		// Monitor
		public synchronized void progressChanged(
				ResumableHttpFileUploader uploader) {
			String fileId = ((FileUploadData) uploader.getData()).getFileName();
			switch (uploader.getUploadState()) {
			case COMPLETE:
			case CLIENT_ERROR:
				pendingRequests -= 1;
				output.println(fileId + ": Completed");
				break;
			case IN_PROGRESS:
				output.println(fileId + ":"
						+ String.format("%3.0f", uploader.getProgress() * 100)
						+ "%");
				break;
			case NOT_STARTED:
				output.println(fileId + ":" + "Not Started");
				break;
			}
		}

		// Determine if upload completed
		public synchronized boolean isDone() {
			// not done if there are any pending requests.
			if (pendingRequests > 0) {
				return false;
			}
			// if all responses are processed., nothing to do.
			if (processed) {
				return true;
			}
			// check if all response streams are available.
			for (ResumableGDataFileUploader uploader : trackedUploaders) {
				if (!uploader.isDone()) {
					return false;
				}
			}
			// process all responses
			for (ResumableGDataFileUploader uploader : trackedUploaders) {
				String fileId = ((FileUploadData) uploader.getData())
						.getFileName();
				switch (uploader.getUploadState()) {
				case COMPLETE:
					try {
						DocumentListEntry entry = uploader
								.getResponse(DocumentListEntry.class);
						uploaded.put(fileId, entry);
					} catch (IOException e) {
						failed.put(fileId,
								"Upload completed, but unexpected error reading server response");
					} catch (ServiceException e) {
						failed.put(fileId,
								"Upload completed, but failed to parse server response");
					}
					break;
				case CLIENT_ERROR:
					failed.put(fileId, "Failed at " + uploader.getProgress());
					break;
				}
			}
			processed = true;
			output.println("All requests done");
			return true;
		}

		// Get list of documents uploaded
		public synchronized Collection<DocumentListEntry> getUploaded() {
			if (!isDone()) {
				return null;
			}
			return uploaded.values();
		}

		// Print results
		public synchronized void printResults() {
			if (!isDone()) {
				return;
			}
			output.println("Result: " + uploaded.size() + ", " + failed.size());
			if (uploaded.size() > 0) {
				output.println(" Successfully Uploaded:");
				for (Map.Entry<String, DocumentListEntry> entry : uploaded
						.entrySet()) {
					printDocumentEntry(entry.getValue());
				}
			}
			if (failed.size() > 0) {
				output.println(" Failed to upload:");
				for (Map.Entry entry : failed.entrySet()) {
					output.println("  " + entry.getKey() + ":"
							+ entry.getValue());
				}
			}
		}

		/**
		 * Prints out the specified document entry.
		 * 
		 * @param doc
		 *            the document entry to print.
		 */
		public void printDocumentEntry(DocumentListEntry doc) {
			StringBuffer buffer = new StringBuffer();

			buffer.append(" -- " + doc.getTitle().getPlainText() + " ");
			if (!doc.getParentLinks().isEmpty()) {
				for (Link link : doc.getParentLinks()) {
					buffer.append("[" + link.getTitle() + "] ");
				}
			}
			buffer.append(doc.getResourceId());

			output.println(buffer);
		}

	}

}