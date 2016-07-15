package com.gmail.brianbridge.notificationapprouting.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gmail.brianbridge.notificationapprouting.R;

public class ThirdActivity extends AppCompatActivity {
	public static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
	}
}
