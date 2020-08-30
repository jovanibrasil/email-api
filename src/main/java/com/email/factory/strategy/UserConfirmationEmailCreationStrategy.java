package com.email.factory.strategy;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.email.model.Email;
import com.security.web.domain.dto.UserDTO;

@Component
public class UserConfirmationEmailCreationStrategy implements EmailCreationStrategy {

	@Value("${url.userconfirmation}")
	private String confirmationUrl;
	
	@Override
	public Email createEmail(UserDTO userDTO, Map<String, Object> params) {
		Email email = new Email();
		String url = confirmationUrl + "?token=" + params.get("registrationToken");
		email.setText("Hello " + userDTO.getUserName() + "!. Please, click the confirmation link to " + 
				"confirm your email and sign into your account. " + url);
		email.setFrom("noreply@notes.jovanibrasil.com");
		email.setTo(userDTO.getEmail());
		email.setTextType("text/plain");
		email.setTitle(userDTO.getApplication() + " - Email Confirmation");
		return email;
	}

}
