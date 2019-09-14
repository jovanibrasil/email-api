package com.email.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.email.models.CustomMessage;

@Service
public class EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

	public Session smtpConnect(final String user, final String password, String host) {
		log.info("Creating smtp session ...");

		// Setup SMTP email properties
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
		return session;
	}

	public void sendEmail(CustomMessage customMessage, final String user, final String password, String host) {

		try {
			
			Multipart multipartEmail = formatEmailContent(customMessage.getText(),
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
			message.setSubject("Testing Subject");

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

	public Multipart formatEmailContent(String content, String type, String attachmentName, String attachmentType) {

		try {

			// Create the message body with multiple parts
			Multipart multipart = new MimeMultipart();

			// Create the body part
			BodyPart messageBodyPart = new MimeBodyPart(); // Create the message part
			messageBodyPart.setContent(content, type);
			multipart.addBodyPart(messageBodyPart);

			// Create the part two that is an attachment
			messageBodyPart = new MimeBodyPart();
			String filename = attachmentName;
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);

			if (attachmentType.equals("image"))
				messageBodyPart.setHeader("Content-ID", "<image>");

			return multipart;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

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
				writePart(message);
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

	/*
	 * This method checks for content-type based on which, it processes and fetches
	 * the content of the message
	 */
	public void writePart(Part p) throws Exception {
		if (p instanceof Message)
			// Call methos writeEnvelope
			writeEnvelope((Message) p);

		System.out.println("----------------------------");
		System.out.println("CONTENT-TYPE: " + p.getContentType());

		// check if the content is plain text
		if (p.isMimeType("text/plain")) {
			System.out.println("This is plain text");
			System.out.println("---------------------------");
			System.out.println((String) p.getContent());
		}
		// check if the content has attachment
		else if (p.isMimeType("multipart/*")) {
			System.out.println("This is a Multipart");
			System.out.println("---------------------------");
			Multipart mp = (Multipart) p.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				writePart(mp.getBodyPart(i));
		}
		// check if the content is a nested message
		else if (p.isMimeType("message/rfc822")) {
			System.out.println("This is a Nested Message");
			System.out.println("---------------------------");
			writePart((Part) p.getContent());
		}
		// check if the content is an inline image
		else if (p.isMimeType("image/jpeg")) {
			System.out.println("--------> image/jpeg");
			Object o = p.getContent();

			InputStream x = (InputStream) o;
			// Construct the required byte array
			System.out.println("x.length = " + x.available());

			int i = 0;
			byte[] bArray = new byte[x.available()];

			while ((i = (int) ((InputStream) x).available()) > 0) {
				int result = (int) (((InputStream) x).read(bArray));
				if (result == -1) {
					break;
				}
			}
			FileOutputStream f2 = new FileOutputStream("/tmp/image.jpg");
			f2.write(bArray);
		} else if (p.getContentType().contains("image/")) {
			System.out.println("content type" + p.getContentType());
			File f = new File("image" + new Date().getTime() + ".jpg");
			DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
			com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) p.getContent();
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = test.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
		} else {
			Object o = p.getContent();
			if (o instanceof String) {
				System.out.println("This is a string");
				System.out.println("---------------------------");
				System.out.println((String) o);
			} else if (o instanceof InputStream) {
				System.out.println("This is just an input stream");
				System.out.println("---------------------------");
				InputStream is = (InputStream) o;
				is = (InputStream) o;
				int c;
				while ((c = is.read()) != -1)
					System.out.write(c);
			} else {
				System.out.println("This is an unknown type");
				System.out.println("---------------------------");
				System.out.println(o.toString());
			}
		}

	}

	/*
	 * This method would print FROM,TO and SUBJECT of the message
	 */
	public void writeEnvelope(Message m) throws Exception {
		System.out.println("This is the message envelope");
		System.out.println("---------------------------");
		Address[] a;

		// FROM
		if ((a = m.getFrom()) != null) {
			for (int j = 0; j < a.length; j++)
				System.out.println("FROM: " + a[j].toString());
		}

		// TO
		if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < a.length; j++)
				System.out.println("TO: " + a[j].toString());
		}

		// SUBJECT
		if (m.getSubject() != null)
			System.out.println("SUBJECT: " + m.getSubject());

	}

}
