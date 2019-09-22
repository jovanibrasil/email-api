package com.email.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.email.models.CustomMessage;
import com.email.services.EmailService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/emails")
public class EmailController {
	
	@Value("${smtp.user}")
	private String smtpUser;
	@Value("${smtp.password}")
	private String smtpPassword;
	@Value("${smtp.host}")
	private String smtpHost;
	@Value("${pop3.user}")
	private String pop3User;
	@Value("${pop3.password}")
	private String pop3Password;
	@Value("${pop3.host}")
	private String pop3Host;
	
	@Autowired
	private EmailService email;
	
	@PostMapping
	public ResponseEntity<?> sendEmail(@RequestBody CustomMessage customMessage){
	try {
			email.sendEmail(customMessage, smtpUser, smtpPassword, smtpHost);

			//String htmlText = "<H1>Hello</H1><img src=\"cid:image\">";
			//String htmlType = "text/html";
			//email = formatEmailContent(htmlText, htmlType, "logo.png", "image");
			//sendEmail(email);
			
			//getEmails();
			
			// reply email
			// forward email
			// delete email
			
			// templates
			
			return ResponseEntity.ok().body(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.badRequest().body(null);
	}

}
