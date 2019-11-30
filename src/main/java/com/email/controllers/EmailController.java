package com.email.controllers;

import com.email.services.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.email.models.CustomMessage;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/emails")
public class EmailController {

	private EmailService email;

	public EmailController(EmailService email) {
		this.email = email;
	}

	@PostMapping
	public ResponseEntity<?> sendEmail(@RequestBody CustomMessage customMessage){
		try {

			email.send(customMessage);

			//String htmlText = "<H1>Hello</H1><img src=\"cid:image\">";
			//String htmlType = "text/html";
			//email = formatEmailContent(htmlText, htmlType, "logo.png", "image");
			//getEmails();
			// delete email
			// templates
			
			return ResponseEntity.ok().body(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.badRequest().body(null);
	}

}
