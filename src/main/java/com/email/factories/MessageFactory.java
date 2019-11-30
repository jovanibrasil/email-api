package com.email.factories;

import com.email.models.Email;
import com.email.utils.EmailFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class MessageFactory {

    private static final Logger log = LoggerFactory.getLogger(MessageFactory.class);

    private Session session;

    public MessageFactory(Session session) {
        this.session = session;
    }

    public Message getMessage(Email email) throws MessagingException {

        Multipart multipartEmail = EmailFormatter.formatEmailContent(email.getText(),
                email.getTextType(), null, null);

        // Create a default MimeMessage object.
        Message message = new MimeMessage(session);

        // Set sender email (From)
        message.setFrom(new InternetAddress(email.getFrom()));
        // Set receiver email (To)
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getTo()));
        // Set Subject
        message.setSubject(email.getTitle());

        // Set the message body
        message.setContent(multipartEmail);

        return message;
    }

}
