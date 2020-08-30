package com.email.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.email.factory.EmailType;
import com.email.service.EmailService;
import com.security.web.domain.dto.UserDTO;

@Component
@RabbitListener(queues = "user-created")
public class UserCreatedListener {
	
	private static final Logger log = LoggerFactory.getLogger(UserCreatedListener.class);
	
	private final EmailService emailService;
	
	public UserCreatedListener(EmailService emailService) {
		this.emailService = emailService;
	}
	
	@RabbitHandler
	public void receiveCreatedUser(UserDTO userDTO, Message message) {
		log.info("Sending confirmation email to email {} from user {}.", userDTO.getEmail(), userDTO.getUserName());
		emailService.sendEmail(EmailType.USER_CREATION_CONFIRMATION, 
				userDTO, message.getMessageProperties().getHeaders());
	}

}
