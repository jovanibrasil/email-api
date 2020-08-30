package com.email.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Configuration
public class SmtpSessionConfig {

    private static final Logger log = LoggerFactory.getLogger(SmtpSessionConfig.class);

    @Value("${server.user}")
    private String user;
    @Value("${server.password}")
    private String password;
    @Value("${server.host}")
    private String host;

    @Bean
    public Session createSmtpSession() {

        log.info("Creating a smtp session ...");

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.auth", "true");
        //props.put("mail.debug", "true");
        Session session = Session.getDefaultInstance(props,  new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        return session;
    }

}
