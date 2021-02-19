package com.usermanagment.dao;

import javax.persistence.Entity;

@Entity
public class RegisterUser extends AbstractDomainClass {
	private String username;
	private String password;
	private String email;
	private String isRegisterationCompleted;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIsRegisterationCompleted() {
		return isRegisterationCompleted;
	}

	public void setIsRegisterationCompleted(String isRegisterationCompleted) {
		this.isRegisterationCompleted = isRegisterationCompleted;
	}

}