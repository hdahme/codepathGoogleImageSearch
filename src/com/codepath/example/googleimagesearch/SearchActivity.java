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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity  {
	
	public static final String SETTINGS_KEY = "settings_key";
	public static final int SETTINGS_REQUEST = 100;
	
	private AsyncHttpClient client = new AsyncHttpClient();
	private Settings settings;
	private EditText searchBar;
	private GridView grid;
	private ArrayList<Image> images = new ArrayList<Image>();
	private ImageArrayAdapter imageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		grid = (GridView)findViewById(R.id.gvImages);
		imageAdapter = new ImageArrayAdapter(this, images);
		grid.setAdapter(imageAdapter);
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
		String searchStr = searchBar.getText().toString();
		client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&v=1.0&q="+
				Uri.encode(searchStr) +"start="+0, null, new JsonHttpResponseHandler() {
			public void onSuccess(JSONObject json) {
				JSONArray JSONImages = null;
				try {
					JSONImages = json.getJSONObject("responseData").getJSONArray("results");
					images.clear();
					imageAdapter.addAll(Image.fromJSONArray(JSONImages));
					Log.d("DEBUG", images.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			public void onFailure(Throwable e, JSONObject json) {
				Toast.makeText(SearchActivity.this, "This app requires a network connection", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (resultCode == RESULT_OK && requestCode == SETTINGS_REQUEST) {
	     settings = (Settings) data.getExtras().getSerializable(SETTINGS_KEY);
	     //Toast.makeText(this, settings.getSize(), Toast.LENGTH_SHORT).show();
	  }
	}
}
