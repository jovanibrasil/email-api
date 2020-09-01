package com.email.model;

public class Email {
	
	private String text;
	private String textType; 
	private String to;
	private String from; 
	private String title;
		
	public String getText() {
		return text;
	}
	public void setText(String simpleText) {
		this.text = simpleText;
	}
	public String getTextType() {
		return textType;
	}
	public void setTextType(String simpleTextType) {
		this.textType = simpleTextType;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
