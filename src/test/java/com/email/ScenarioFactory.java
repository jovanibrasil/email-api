package com.email;

import com.email.model.Email;
import com.security.web.domain.ApplicationType;
import com.security.web.domain.dto.UserDTO;

public class ScenarioFactory {

	public static Email getEmail() {
		Email email = new Email();
		email.setFrom("from");
		email.setText("text");
		email.setTextType("type");
		email.setTitle("tytle");
		email.setTo("to");
		return email;
	}

	public static UserDTO getUserDTO() {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(1L);
		userDTO.setEmail("email@email");
		userDTO.setApplication(ApplicationType.AUTH_APP);
		userDTO.setUserName("username");
		return userDTO;
	}
	
}
