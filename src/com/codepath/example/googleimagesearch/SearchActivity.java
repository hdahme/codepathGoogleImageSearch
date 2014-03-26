package com.codepath.example.googleimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity  {
	
	public static final String SETTINGS_KEY = "settings_key";
	public static final String IMAGE_URL = "url";
	public static final int SETTINGS_REQUEST = 100;
	public static final int IMAGE_REQUEST = 101;
	
	private AsyncHttpClient client = new AsyncHttpClient();
	private Settings settings;
	private EditText searchBar;
	private GridView grid;
	private ArrayList<Image> images = new ArrayList<Image>();
	private ImageArrayAdapter imageAdapter;
	private String searchStr;
	private int pageNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		grid = (GridView)findViewById(R.id.gvImages);
		imageAdapter = new ImageArrayAdapter(this, images);
		grid.setAdapter(imageAdapter);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View parent, int position,
					long arg3) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				Image image = images.get(position);
				i.putExtra(IMAGE_URL, image.getUrl());
				startActivity(i);
			}
			
		});
		
		grid.setOnScrollListener(new EndlessScrollListener(3) {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadImages();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.action_bar);
		
		settings = new Settings();
		searchBar = (EditText) findViewById(R.id.etSearch);
		
		return true;
	}
	
	public void onSettingsClick(MenuItem mi) {
		Intent i = new Intent(this, SettingsActivity.class);
		i.putExtra(SETTINGS_KEY, settings);
		startActivityForResult(i, SETTINGS_REQUEST);
	}
	
	public void onSearchClick(MenuItem mi) {
		searchStr = searchBar.getText().toString();
		if (searchStr.length() == 0) {
			return;
		}
		pageNum = 0;
		images.clear();
		loadImages();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (resultCode == RESULT_OK && requestCode == SETTINGS_REQUEST) {
	     settings = (Settings) data.getExtras().getSerializable(SETTINGS_KEY);
	     //Toast.makeText(this, settings.getSize(), Toast.LENGTH_SHORT).show();
	  }
	}
	
	public void loadImages() {
		// Artificially limiting the number of results, as per the spec
		if (pageNum > 7) {
			return;
		}
		String url = "https://ajax.googleapis.com/ajax/services/search/images?rsz=8&v=1.0&q="+
				Uri.encode(searchStr) +"&start="+pageNum*8;
		if (settings.getColour() != null) {
			url += "&imgcolor="+settings.getColour();
		}
		if (settings.getSize() != null) {
			url += "&imgsz="+settings.getSize();
		}
		if (settings.getType() != null) {
			url += "&imgtype="+settings.getType();
		}
		if (settings.getSite() != null) {
			url += "&as_sitesearch="+settings.getSite();
		}
		System.out.println(url);
		
		client.get(url, null, new JsonHttpResponseHandler() {
			public void onSuccess(JSONObject json) {
				JSONArray JSONImages = null;
				try {
					JSONImages = json.getJSONObject("responseData").getJSONArray("results");
					imageAdapter.addAll(Image.fromJSONArray(JSONImages));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			public void onFailure(Throwable e, JSONObject json) {
				Toast.makeText(SearchActivity.this, "This app requires a network connection", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		});
		
		pageNum++;
	}
}
