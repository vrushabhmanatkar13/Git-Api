package com.payloads;

import java.util.Base64;

public class FileUpdatePOJO {
	
	private String message;
	private String content;
	private String sha;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = Base64.getEncoder().encodeToString(content.getBytes());
	}
	public String getSha() {
		return sha;
	}
	public void setSha(String sha) {
		this.sha = sha;
	}
	

}
