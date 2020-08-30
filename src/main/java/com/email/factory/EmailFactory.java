package com.email.factory;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.email.factory.strategy.UserConfirmationEmailCreationStrategy;
import com.email.model.Email;
import com.security.web.domain.dto.UserDTO;

@Component
public class EmailFactory {
	
	private final UserConfirmationEmailCreationStrategy userConfirmationStrategy;

	public EmailFactory(UserConfirmationEmailCreationStrategy userConfirmationStrategy) {
		this.userConfirmationStrategy = userConfirmationStrategy;
	}
	
	public Email createEmail(EmailType emailType, UserDTO userDTO, Map<String, Object> params) {
		switch (emailType) {
			case USER_CREATION_CONFIRMATION:
				return userConfirmationStrategy.createEmail(userDTO, params);
			default:
				return null;
		}		
	}
	
}
