package com.payloads;

import java.util.Base64;

public class FileContentPOJO {

	private String message;
	private String content;
	private CommiterInfo committer;
	
	
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
	public CommiterInfo getCommitter() {
		return committer;
	}
	public void setCommitter(CommiterInfo committer) {
		this.committer = committer;
	}
	
}
 
	
