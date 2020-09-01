package com.email.factory.strategy;

import java.util.Map;

import com.email.model.Email;
import com.security.web.domain.dto.UserDTO;

public interface EmailCreationStrategy {
	
	Email createEmail(UserDTO userDTO, Map<String, Object> params);

}
