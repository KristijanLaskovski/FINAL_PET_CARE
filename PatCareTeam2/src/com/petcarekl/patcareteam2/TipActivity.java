package com.petcarekl.patcareteam2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TipActivity extends ActionBarActivity {
	
	String tip_content;
	TextView tvFor_tip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip);
		tvFor_tip=(TextView)findViewById(R.id.tipTextView);
		Intent is= getIntent();
        Bundle b = is.getExtras();
        if(b!=null)
        {
        	tip_content =(String) b.get("POST_ID");
        }
		
        setTipView(tip_content);
		
	}


	
	
	public void setTipView(String title_tip){
		 switch (title_tip) {
	        case "Regular Exams are Vital":
	        	tvFor_tip.setText(getString(R.string.tip1));
	            break;
	        case "Spay and Neuter Your Pets":
	        	tvFor_tip.setText(getString(R.string.tip2));
	            break;
	        	
	        case "Prevent Parasites":
	        	tvFor_tip.setText(getString(R.string.tip3));
	            break;
	        case "Maintain a Healthy Weight":
	        	tvFor_tip.setText(getString(R.string.tip4));
	            break;
	        case "Get Regular Vaccinations":
	        	tvFor_tip.setText(getString(R.string.tip5));
	            break;
	        case "Provide an Enriched Environment":
	        	tvFor_tip.setText(getString(R.string.tip6));
	        	break;
	        case "ID Microchip Your Pet":
	        	tvFor_tip.setText(getString(R.string.tip7));
	            break;
	        case "Pets Need Dental Care, Too":
	        	tvFor_tip.setText(getString(R.string.tip9));
	            break;
	        case "Never Give Pets People Medication":
	        	tvFor_tip.setText(getString(R.string.tip9));
	        	break;
	        case "Proper Restraint in a Vehicle":
	        	tvFor_tip.setText(getString(R.string.tip10));
	        	break;
	        default:
	            break;
	        }
	}
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tip, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
