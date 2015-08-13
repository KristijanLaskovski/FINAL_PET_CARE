package com.example.mapslabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class CityActivity extends ActionBarActivity{

	private TextView name;
	private TextView countryName;
	private TextView wikipedia;
	private TextView population;
	private TextView fcodename;
	private Intent intent;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		
		name = (TextView) findViewById(R.id.name);
		countryName = (TextView) findViewById(R.id.countryCode);
		wikipedia = (TextView) findViewById(R.id.wikipedia);
		wikipedia.setMovementMethod(LinkMovementMethod.getInstance());
		population = (TextView) findViewById(R.id.population);
		fcodename = (TextView) findViewById(R.id.fcodename);
		
		intent = this.getIntent();
		name.setText("1. NAME: " + intent.getStringExtra("name"));
		countryName.setText("2. COUNTRY CODE: " + intent.getStringExtra("countryCode"));
		wikipedia.setText("3. WIKI: " + intent.getStringExtra("wikipedia"));
		population.setText("4. POPULATION: " + intent.getIntExtra("population", 0));
		fcodename.setText("5. " + intent.getStringExtra("fcodename"));
		
		
		
	}
	
	
}
