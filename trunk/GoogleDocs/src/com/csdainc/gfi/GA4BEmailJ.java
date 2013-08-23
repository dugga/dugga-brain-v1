package com.csdainc.gfi;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

public class GA4BEmailJ {

	public static void main(String[] args) {

		// Set passed Argument variables with passed values
		final String email     = args[0];
		final String password  = args[1];
		String toemail   = args[2];
		String subject   = args[3];
		String body      = args[4];
		String document  = args[5];
		String directory = args[6];

		// Setup properties for smtp
		Properties props = new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, password);
			}
		});

		// Concatenate directory and document name
		if (!directory.trim().equals("") && !directory.trim().endsWith("/")) {
			directory = directory.trim() + "/";
		}
				
		String fromFile = directory.trim() + document.trim();
		
		try {

			// create the message
			Message message = new MimeMessage(session);
			Multipart multipart = new MimeMultipart();
			
			//set message header info
			message.setFrom(new InternetAddress(email));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toemail));
			message.setSubject(subject);

			//set message body text
			MimeBodyPart mimeBodyPartBody = new MimeBodyPart();
			mimeBodyPartBody.setText(body);
			multipart.addBodyPart(mimeBodyPartBody, 0);
			
			//attach document
			if (!fromFile.equals("")) {
				MimeBodyPart mimeBodyPartDocument = new MimeBodyPart();
				File file = new File(fromFile);
				if (file.exists()) {
					System.out.println("Attaching document: " + fromFile);
					FileDataSource fileDataSource = new FileDataSource(file.getCanonicalPath());
					mimeBodyPartDocument.setDataHandler(new DataHandler(fileDataSource));
					mimeBodyPartDocument.setFileName(fileDataSource.getName());
					multipart.addBodyPart(mimeBodyPartDocument, 1);
				} else {
					System.out.println("Error - specified file not found: " + fromFile);
					System.out.println("        sending email without attachment");
				}
			}
			
			//sending message
	        message.setContent(multipart);
			System.out.println("Sending email to: " + toemail);
			Transport.send(message);
			System.out.println("Email sent.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}