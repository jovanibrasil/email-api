package com.email.service;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.email.util.EmailFormatter;

import javax.mail.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

	private EmailFormatter emailFormatter;
	private Session session;

	public EmailServiceImpl(EmailFormatter emailFormatter, Session session) {
		this.emailFormatter = emailFormatter;
		this.session = session;
	}

	@Override
	public void send(Message message) {
		try {
			log.info("Sending email...");
			Transport.send(message);
			log.info("Sent message successfully!");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void reply() {
		throw new NotImplementedException();
	}

	@Override
	public void forward() {
		throw new NotImplementedException();
	}

	@Override
	public void delete() {
		throw new NotImplementedException();
	}

	@Override
	public List<Message> getAll() {
		// TODO Fix implementation
		try {

			// Create the POP2 store object and connect with the pop server
			Store store = session.getStore("pop3s");
			//store.connect(host, user, password);

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
		return null;
	}

}
