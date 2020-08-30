package com.email.factory.strategy;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.email.ScenarioFactory;
import com.email.model.Email;
import com.security.web.domain.dto.UserDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserConfirmationEmailCreationStrategyTest {

	@Autowired
	private UserConfirmationEmailCreationStrategy creationStrategy;
	
	@Test
	public void testCreateEmail() {
		
		UserDTO userDTO = ScenarioFactory.getUserDTO();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registrationToken", "registrationToken");
		
		Email createdEmail = creationStrategy.createEmail(userDTO, params);
		
		assertEquals("noreply@notes.jovanibrasil.com", createdEmail.getFrom());
		assertEquals(userDTO.getEmail(), createdEmail.getTo());
		assertEquals("text/plain", createdEmail.getTextType());
		String title = userDTO.getApplication() + " - Email Confirmation";
		assertEquals(title, createdEmail.getTitle());
		
	}
	
}
