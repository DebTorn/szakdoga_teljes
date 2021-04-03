package com.munkanyilvantarto.entity;

import java.security.SecureRandom;
import java.util.Base64;


public class TokenGenerator {

	private String token;
	private static final SecureRandom secureRandom = new SecureRandom();
	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

	public String getToken() {
		return token;
	}

	public void setToken() {
		byte[] tomb = new byte[32];
		secureRandom.nextBytes(tomb);
		this.token = base64Encoder.encodeToString(tomb);
	}

	@Override
	public String toString() {
		return "TokenGenerator [token=" + token + "]";
	}
	
}
