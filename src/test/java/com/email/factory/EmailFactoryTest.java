package com.email.factory;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.email.ScenarioFactory;
import com.email.factory.strategy.UserConfirmationEmailCreationStrategy;
import com.email.model.Email;
import com.security.web.domain.dto.UserDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailFactoryTest {
	
	@Mock
	private UserConfirmationEmailCreationStrategy userConfirmationStrategy;

	@Autowired
	private EmailFactory emailFactory;
	
	@Test
	public void createEmailWithUserCreationInformationType() {
		UserDTO userDTO = ScenarioFactory.getUserDTO();
		
		Email createdEmail = emailFactory.createEmail(EmailType.USER_CREATION_CONFIRMATION,
					userDTO, new HashMap<String, Object>());
		assertNotNull(createdEmail);
	}
	
}
