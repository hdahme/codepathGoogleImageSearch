package com.codepath.example.googleimagesearch;

import java.util.List;

import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ImageArrayAdapter extends ArrayAdapter<Image> {

	public ImageArrayAdapter(Context context, List<Image> images) {
		super(context, R.layout.image_item, images);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Image image = this.getItem(position);
		SmartImageView ivImage;
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			ivImage = (SmartImageView) inflator.inflate(R.layout.image_item, parent, false);
		} else {
			ivImage = (SmartImageView) convertView;
			ivImage.setImageResource(android.R.color.transparent);
		}
		ivImage.setImageUrl(image.getTbUrl());
		return ivImage;
	}

	
}
