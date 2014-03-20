package com.codepath.example.googleimagesearch;

import java.security.spec.EncodedKeySpec;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.*;

public class SearchActivity extends Activity  {
	
	public static final String SETTINGS_KEY = "settings_key";
	public static final int SETTINGS_REQUEST = 100;
	
	private AsyncHttpClient client = new AsyncHttpClient();
	private Settings settings;
	private EditText searchBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
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
		String searchStr = searchBar.getText().toString().replace(" ", "%20");
		client.get("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=".concat(searchStr), null, new JsonHttpResponseHandler() {
			public void onSuccess(JSONObject json) {
				try {
					System.out.println(json.getJSONObject("responseData").getJSONArray("results"));
				} catch (Exception e) {
					Log.e("json", "JSON parse error");
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
