package com.wings.jwt.demo.model;

public class LoginResponseDTO {

	private String status;
//	private String message;
	private String token; // Authentication token
//	private String userId; // User identifier (if applicable)

	public LoginResponseDTO(String status, String token) {
		this.status = status;
		this.token = token;
	}

	public String isStatus() {
		return status;
	}

	public void setstatus(String status) {
		this.status = status;
	}

//	public String getMessage() {
//		return message;
//	}
//
//	public void setMessage(String message) {
//		this.message = message;
//	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
}
