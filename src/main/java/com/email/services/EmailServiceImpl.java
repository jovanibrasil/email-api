package com.email.services;

import com.email.models.CustomMessage;
import com.email.utils.EmailFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Value("${server.user}")
	private String user;
	@Value("${server.password}")
	private String password;
	@Value("${server.host}")
	private String host;

	private EmailFormatter emailFormatter;

	public EmailServiceImpl(EmailFormatter emailFormatter) {
		this.emailFormatter = emailFormatter;
	}

	@Override
	public void send(CustomMessage customMessage) {
		try {
			Multipart multipartEmail = emailFormatter.formatEmailContent(customMessage.getText(),
					customMessage.getTextType(), "file.txt", "file");

			Session session = smtpConnect(user, password, host);

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

			// Set simple text
			// message.setText("Hello, this is sample for to check send " + "email using
			// JavaMailAPI ");
			// Set html
			// message.setContent("<h1>This is actual message embedded in HTML tags</h1>",
			// "text/html");

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

	}

	@Override
	public void forward() {

	}

	public Session smtpConnect(final String user, final String password, String host) {
		log.info("Creating smtp session ...");

		log.info("User: {}", user);
		log.info("Password: {}", password);
		log.info("Host: {}", host);
		
		// Setup SMTP email properties
		Properties props = new Properties();

		// SSL
//		props.put("mail.smtp.host", "smtp.gmail.com");
//		props.put("mail.smtp.socketFactory.port", "465");
//		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.port", "465");
//		
		// TLS
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", "smtp.gmail.com");
//		props.put("mail.smtp.port", "587");
		
		//props.put("mail.host", "smtp.gmail.com");
//		props.put("mail.port", "587");
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.socketFactory.port", "587");
//		props.put("mail.smtp.socketFactory.fallback", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.starttls.required", "true");
//		props.put("mail.smtp.ssl.enable", "false");

		props.put("mail.transport.protocol", "smtps");
	    props.put("mail.smtp.host", "smtp.sendgrid.net");
	    props.put("mail.smtp.port", 587);
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.starttls.required", "true");
	    props.put("mail.smtp.auth", "true");
		
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
		return session;
	}

	public void getEmails(final String user, final String password, String host) {

		Session session = smtpConnect(user, password, host);

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
