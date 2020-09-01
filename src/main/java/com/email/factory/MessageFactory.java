package com.email.factory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

import com.email.model.Email;
import com.email.util.EmailFormatter;

@Component
public class MessageFactory {

    private final Session session;

    public MessageFactory(Session session) {
        this.session = session;
    }

    public Message createMessage(Email email) throws MessagingException {

        Multipart multipartEmail = EmailFormatter.formatEmailContent(email.getText(),
                email.getTextType(), null, null);

        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(email.getFrom()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getTo()));
        message.setSubject(email.getTitle());
        message.setContent(multipartEmail);

        return message;
    }

}
