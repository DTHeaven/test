package com.example.testapp1.bean;

public class Content {
	enum ContentType {
		TYPE_TITLE, 
		TYPE_CONTENT, 
		TYPE_IMAGE, 
		TYPE_COMMENT_TITLE, 
		TYPE_COMMENT
		};
		
	private String content;
	private int imageResID;
	private Comment comment;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getImageResID() {
		return imageResID;
	}
	public void setImageResID(int imageResID) {
		this.imageResID = imageResID;
	}
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	
}
