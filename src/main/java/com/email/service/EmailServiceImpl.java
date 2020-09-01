package com.email.service;

import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.email.factory.EmailFactory;
import com.email.factory.EmailType;
import com.email.factory.MessageFactory;
import com.security.web.domain.dto.UserDTO;

@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
	private final MessageFactory messageFactory;
	private final EmailFactory emailFactory;

	public EmailServiceImpl(MessageFactory messageFactory, EmailFactory emailFactory) {
		this.messageFactory = messageFactory;
		this.emailFactory = emailFactory;
	}	

	@Override
	public void sendEmail(EmailType emailType, UserDTO userDTO, Map<String, Object> params) {
		try {
			Message message = messageFactory.createMessage(emailFactory
					.createEmail(emailType, userDTO, params));
			Transport.send(message);
			log.info("Sent message successfully to {}!", userDTO.getEmail());
		} catch (MessagingException e) {
			log.info("Failed to send email to {}. {}", emailType, e.getMessage());
		}
	}

}
