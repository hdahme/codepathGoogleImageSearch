package com.codepath.example.googleimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	
	private Spinner sizeSpinner;
	private Spinner colourSpinner;
	private Spinner typeSpinner;
	private EditText siteField;
	private Settings settings;
	
	private ArrayAdapter<String> colourAdapter;
	
	// Translate to Google Images-safe arguments
	private String[] sizeMap = {null, "small", "medium", "large", "xlarge"};
	private String[] typeMap = {null, "face", "photo", "clipart", "lineart"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		sizeSpinner = (Spinner) findViewById(R.id.spSize);
		colourSpinner = (Spinner) findViewById(R.id.spColour);
		typeSpinner = (Spinner) findViewById(R.id.spType);
		siteField = (EditText) findViewById(R.id.etSite);
		
		colourAdapter = (ArrayAdapter<String>) colourSpinner.getAdapter();
		settings = (Settings) getIntent().getSerializableExtra(SearchActivity.SETTINGS_KEY);		
		if (settings.getSize() != null) {
			int pos = java.util.Arrays.asList(sizeMap).indexOf(settings.getSize());
			sizeSpinner.setSelection(pos);
		}
		if (settings.getColour() != null) {
			int pos = colourAdapter.getPosition(settings.getColour());
			colourSpinner.setSelection(pos);
		}
		if (settings.getType() != null) {
			int pos = java.util.Arrays.asList(typeMap).indexOf(settings.getType());
			typeSpinner.setSelection(pos);
		}
		if (settings.getSite() != null) {
			siteField.setText(settings.getSite());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	public void onSave(View view) {
		settings.setSize(sizeMap[sizeSpinner.getSelectedItemPosition()]);
		settings.setColour(colourSpinner.getSelectedItem().toString().toLowerCase());
		if (settings.getColour() == " ") {
			settings.setColour(null);
		}
		settings.setType(typeMap[typeSpinner.getSelectedItemPosition()]);
		settings.setSite(siteField.getText().toString());
		
		Intent data = new Intent();		
		data.putExtra(SearchActivity.SETTINGS_KEY, settings);
		setResult(RESULT_OK, data);
		finish();
	}

}
