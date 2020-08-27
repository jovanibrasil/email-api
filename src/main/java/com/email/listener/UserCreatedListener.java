package com.email.listener;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.email.factory.MessageFactory;
import com.email.model.Email;
import com.email.service.EmailService;
import com.security.web.domain.dto.UserDTO;

@Component
@RabbitListener(queues = "user-created")
public class UserCreatedListener {
	
	private static final Logger log = LoggerFactory.getLogger(UserCreatedListener.class);
	
	private final EmailService emailService;
	private final MessageFactory messageFactory;

	@Value("${url.userconfirmation}")
	private String confirmationUrl;
	
	public UserCreatedListener(EmailService emailService, MessageFactory messageFactory) {
		this.emailService = emailService;
		this.messageFactory = messageFactory;
	}
	
	@RabbitHandler
	public void receive(UserDTO userDTO, Message message) {
		log.info("Sending confirmation email to email {} from user {}.", userDTO.getEmail(), userDTO.getUserName());
		
		Email em = new Email();
		String url = confirmationUrl + "?token=" + message.getMessageProperties().getHeaders().get("registrationToken");
		em.setText("Hello " + userDTO.getUserName() + "!. Please, click the confirmation link to " + 
				"confirm your email and sign into your account. " + url);
		em.setFrom("noreply@notes.jovanibrasil.com");
		em.setTo(userDTO.getEmail());
		em.setTextType("text/plain");
		em.setTitle(userDTO.getApplication() + " - Email Confirmation");
		
		try {
			emailService.send(messageFactory.getMessage(em));
		} catch (MessagingException e) {
			log.info("Error sending the email to user {}. {}", userDTO.getUserName(), e.getMessage());
		}
		
	}

}
