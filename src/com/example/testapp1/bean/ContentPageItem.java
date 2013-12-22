package com.example.testapp1.bean;

import java.util.ArrayList;

public class ContentPageItem {
	private int bgColor;
	private String date;
	
	private String title;
	private String subTitle;
	private String shortContent;
	private String author;
	private String profession;
	
	private ArrayList<Content> contents;
	
	private int iconResID;
	private int imageResID;
	
	private String downloadUrl;
	private boolean isFree;
	
	public int getBgColor() {
		return bgColor;
	}
	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getShortContent() {
		return shortContent;
	}
	public void setShortContent(String shortContent) {
		this.shortContent = shortContent;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public ArrayList<Content> getContents() {
		return contents;
	}
	public void setContents(ArrayList<Content> contents) {
		this.contents = contents;
	}
	public int getIconResID() {
		return iconResID;
	}
	public void setIconResID(int iconResID) {
		this.iconResID = iconResID;
	}
	public int getImageResID() {
		return imageResID;
	}
	public void setImageResID(int imageResID) {
		this.imageResID = imageResID;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public boolean isFree() {
		return isFree;
	}
	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	
}
