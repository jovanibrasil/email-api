import java.util.Properties;
import java.util.concurrent.Semaphore;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Main {

	public static void sendEmail(Multipart multipart) {
		String to = "jovanibrasil@gmail.com";
		// contact / email / reports / noreply
		String from = "email@jovanibrasil.com";
		final String userName = "";
		final String password = "";

		String host = "";
		// Setup smtp email properties
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set sender email (From)
			message.setFrom(new InternetAddress(from));
			// Set receiver email (To)
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			// Set Subject
			message.setSubject("Testing Subject");

			// Set the message body

			// Set simple text
			// message.setText("Hello, this is sample for to check send " + "email using
			// JavaMailAPI ");
			// Set html
			// message.setContent("<h1>This is actual message embedded in HTML tags</h1>",
			// "text/html");

			message.setContent(multipart);

			// Send the message
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static Multipart formatEmailContent(String content, String type, String attachmentName, String attachmentType) {

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

			if(attachmentType.equals("image"))
				messageBodyPart.setHeader("Content-ID", "<image>");
			
			return multipart;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static void main(String[] args) {

		try {
			
			String simpleText = "This is message body";
			String simpleTextType = "text/plain";			
			Multipart email = formatEmailContent(simpleText, simpleTextType, "file.txt", "file");
			sendEmail(email);
			
			String htmlText = "<H1>Hello</H1><img src=\"cid:image\">";
			String htmlType = "text/html";
			email = formatEmailContent(htmlText, htmlType, "logo.png", "image");
			sendEmail(email);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
