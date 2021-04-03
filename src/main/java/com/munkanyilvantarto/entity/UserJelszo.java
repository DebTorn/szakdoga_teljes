package com.munkanyilvantarto.entity;

public class UserJelszo {

	private String password;
	
	private String email;
	
	private String token;

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "UserJelszo [password=" + password + ", email=" + email + ", token=" + token + "]";
	}
	
}
