package com.email.service;

import java.util.Map;

import com.email.factory.EmailType;
import com.security.web.domain.dto.UserDTO;

public interface EmailService {

	public void sendEmail(EmailType emailType, UserDTO userDTO, Map<String, Object> params);

}
