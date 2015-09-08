package com.petcarekl.patcareteam2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.petcare.teamiki.R;

public class TipActivity extends ActionBarActivity {
	
	String tip_content;
	TextView tvFor_tip;
	ActionBar  actionBar;
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
        setTitle(tip_content);
       // getActionBar().setHomeButtonEnabled(true);
       // getActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar = getSupportActionBar();
       // actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // TODO: Remove the redundant calls to getSupportActionBar()
        //       and use variable actionBar instead
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTipView(tip_content);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
	        switch (item.getItemId()) {
	        case android.R.id.home: 
	            onBackPressed();
	            return true;
	        }

	    return super.onOptionsItemSelected(item);
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
	
	
	
	
	
}
