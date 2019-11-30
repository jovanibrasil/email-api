package com.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class EmailSession {

    private static final Logger log = LoggerFactory.getLogger(EmailSession.class);

    @Value("${server.user}")
    private static String user;
    @Value("${server.password}")
    private static String password;
    @Value("${server.host}")
    private static String host;

    private static Session session;

    public static synchronized Session getDefaultSession(){
        if(session == null) session = createSmtpSession();
        return session;
    }

    private static Session createSmtpSession() {

        log.info("Creating a smtp session ...");

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.host", "smtp.sendgrid.net");
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.enable", "false");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        return session;
    }

    private EmailSession(){}

}
