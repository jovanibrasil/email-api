package com.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@EnableAutoConfiguration
@SpringBootApplication(exclude = {
	MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
	ThymeleafAutoConfiguration.class, EmbeddedLdapAutoConfiguration.class,
	MailSenderValidatorAutoConfiguration.class, MustacheAutoConfiguration.class,
	GroovyTemplateAutoConfiguration.class, LiquibaseAutoConfiguration.class,
	MongoReactiveAutoConfiguration.class
})
public class EmailApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		
		SpringApplication.run(EmailApplication.class, args);
	
	}
	
}
