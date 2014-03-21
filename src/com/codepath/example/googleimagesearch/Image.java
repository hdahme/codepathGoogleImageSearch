package com.codepath.example.googleimagesearch;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

public class Image implements Serializable {
	
	private static final long serialVersionUID = 6016770966632102036L;
	private String url;
	private String tbUrl;
	
	
	public String getTbUrl() {
		return tbUrl;
	}
	public void setTbUrl(String tbUrl) {
		this.tbUrl = tbUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Image(JSONObject json) {
		try {
			this.url = json.getString("url");
			this.tbUrl = json.getString("tbUrl");
		} catch (JSONException e) {
			this.url = null;
			this.tbUrl = null;
		}
	}
	
	public String toString() {
		return this.url;
	}
	public static ArrayList<? extends Image> fromJSONArray(JSONArray JSONImages) {
		ArrayList<Image> images = new ArrayList<Image>();
		for (int i = 0; i<JSONImages.length(); i++) {
			try {
				images.add(new Image(JSONImages.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return images;
		
	}
	
}
