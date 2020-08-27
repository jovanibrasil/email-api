package com.security.web.domain.dto;

import java.io.Serializable;

import com.security.web.domain.ApplicationType;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = -4139753881305795908L;

	private Long id;
	private String email;
	private String userName;
	private ApplicationType application;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public ApplicationType getApplication() {
		return application;
	}
	public void setApplication(ApplicationType application) {
		this.application = application;
	}
	
}
