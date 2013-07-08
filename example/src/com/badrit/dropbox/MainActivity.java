package com.badrit.dropbox;

import org.apache.cordova.DroidGap;

import com.badrit.dropbox.R;

import android.os.Bundle;

public class MainActivity extends DroidGap {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		super.loadUrl("file:///android_asset/www/index.html", 5000);
	}
}

