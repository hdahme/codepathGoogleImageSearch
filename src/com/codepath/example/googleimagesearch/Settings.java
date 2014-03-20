package com.codepath.example.googleimagesearch;

import java.io.Serializable;

public class Settings implements Serializable{
	
	private static final long serialVersionUID = 5563561364990911856L;
	private String size;
	private String colour;
	private String type;
	private String site;
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	
	

}
