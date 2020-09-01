package com.email.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.email.ScenarioFactory;
import com.email.model.Email;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageFactoryTest {

	@Autowired
	private MessageFactory messageFactory;
	
	@Test
	public void testCreateMessage() throws MessagingException, IOException {
		
		Email email = ScenarioFactory.getEmail();
		Message message = messageFactory.createMessage(email);
		
		assertNotNull(message);
		assertEquals(email.getTitle(), message.getSubject());
		assertNotNull(message.getContent());
		assertNotNull(message.getFrom());
		assertNotNull(message.getRecipients(Message.RecipientType.TO));
	}
	
}
