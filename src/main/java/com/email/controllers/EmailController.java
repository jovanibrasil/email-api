package com.email.controllers;

import com.email.services.EmailService;
import com.email.factories.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.email.models.Email;

import javax.mail.Message;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/emails")
public class EmailController {

	private static final Logger log = LoggerFactory.getLogger(EmailController.class);

	private EmailService email;
	private MessageFactory messageFactory;

	public EmailController(EmailService email, MessageFactory messageFactory) {
		this.email = email;
		this.messageFactory = messageFactory;
	}

	@PostMapping
	public ResponseEntity<?> sendEmail(@RequestBody Email email){
		log.info("Received request to send email from {} to {}", email.getFrom(), email.getTo());
		try {
			Message message = this.messageFactory.getMessage(email);
			this.email.send(message);
			return ResponseEntity.ok().body(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.badRequest().body(null);
	}

}
