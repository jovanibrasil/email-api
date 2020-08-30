package com.email.util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailFormatter {

    private static Logger log = LoggerFactory.getLogger(EmailFormatter.class);

    public static Multipart formatEmailContent(String content, String type, String attachmentName, String attachmentType) {
        log.info("Formatting email content.");
        try {
            // Create the message body with multiple parts
            Multipart multipart = new MimeMultipart();

            // Create the body part
            BodyPart messageBodyPart = new MimeBodyPart(); // Create the message part
            messageBodyPart.setContent(content, type);
            multipart.addBodyPart(messageBodyPart);

            if(attachmentName != null){
                // Create the part two that is an attachment
                log.info("Creating attachment.");
                messageBodyPart = new MimeBodyPart();
                String filename = attachmentName;
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);

                if ("image".equals(attachmentType))
                    messageBodyPart.setHeader("Content-ID", "<image>");

            }
            return multipart;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
