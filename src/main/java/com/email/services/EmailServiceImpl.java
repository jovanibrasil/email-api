package com.email.services;

import com.email.EmailSession;
import com.email.models.CustomMessage;
import com.email.utils.EmailFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

	private EmailFormatter emailFormatter;

	public EmailServiceImpl(EmailFormatter emailFormatter) {
		this.emailFormatter = emailFormatter;
	}

	@Override
	public void send(CustomMessage customMessage) {
		try {
			Multipart multipartEmail = emailFormatter.formatEmailContent(customMessage.getText(),
					customMessage.getTextType(), null, null);

			Session session = EmailSession.getDefaultSession();

			if (session == null) {
				log.info("Session creation error.");
			}

			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set sender email (From)
			message.setFrom(new InternetAddress(customMessage.getFrom()));
			// Set receiver email (To)
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(customMessage.getTo()));
			// Set Subject
			message.setSubject(customMessage.getTitle());

			// Set the message body
			message.setContent(multipartEmail);
			// Send the message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void reply() {
		// TODO Not implemented yet
	}

	@Override
	public void forward() {
		// TODO Not implemented yet
	}

	public void getEmails(final String user, final String password, String host) {

		Session session = EmailSession.getDefaultSession();

		try {

			// Create the POP2 store object and connect with the pop server
			Store store = session.getStore("pop3s");
			store.connect(host, user, password);

			// Create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// Retrieve the messages
			Message[] messages = emailFolder.getMessages();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			// Checking emails - open the mail box and check the header of each message
//			for (Message message : messages) {
//				System.out.println("---------------------------------");
//				System.out.println("Subject: " + message.getSubject());
//				System.out.println("From: " + message.getFrom()[0]);
//				System.out.println("Text: " + message.getContent().toString());
//			}

			// Fetching emails - open the mail box and check the header and the content of
			// each message
			for (Message message : messages) {
				emailFormatter.writePart(message);
				String line = reader.readLine();
				if ("YES".equals(line)) {
					message.writeTo(System.out);
				} else if ("QUIT".equals(line)) {
					break;
				}
			}

			emailFolder.close(false);
			store.close();

			System.out.println("Checked emails successfully....");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}
