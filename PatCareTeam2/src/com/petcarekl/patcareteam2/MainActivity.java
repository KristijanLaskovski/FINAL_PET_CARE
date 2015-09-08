package com.petcarekl.patcareteam2;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.petcare.teamiki.R;


@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	static final int REQUEST_COMMENT=1;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 //AppEventsLogger.deactivateApp(this);
	}

	@Override
    public void onBackPressed() {

       return;
    }

	
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int position) {
		  	Fragment fragment = null;
	        switch (position) {
	        case 1:
	        	mTitle = getString(R.string.title_section1);
	        	fragment = new HomeFragment();
	            break;
	        case 2:
	        	mTitle = getString(R.string.title_section2);
	        	fragment=new ProfileFragment();
	            break;
	        	
	        case 3:
	        	mTitle = getString(R.string.title_section3);
	            break;
	        case 4:
	        	mTitle = getString(R.string.title_section4);
	            break;
	        case 5:
	        	mTitle = getString(R.string.title_section5);
	        	fragment=new BuyFragment();
	            break;
	        case 6:
	        	mTitle = getString(R.string.title_section6);
	        	fragment=new TipsForPetCare();
	            break;
	        default:
	            break;
	        }
	 
	        if (fragment != null) {
	            FragmentManager fragmentManager = getSupportFragmentManager();
	            fragmentManager.beginTransaction()
	                    .replace(R.id.container, fragment).commit();
	 
	            // update selected item and title, then close the drawer
	           
	        } else {
	            // error in creating fragment
	            Log.e("MainActivity", "Error in creating fragment");
	        }
	    }
		
		
	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_about_us) {
			Intent i=new Intent("com.example.patcareteam2.ABOUT");
			startActivity(i);
		}
		if (id == R.id.action_preferences) {
			Intent p=new Intent("com.example.patcareteam2.PREFERENCES");
			startActivity(p);
		}
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_exit) {
			SharedPreferences	sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
			Editor edit = sp.edit();
			edit.putBoolean("logininformation", false);
			edit.commit();
			Intent i=new Intent(MainActivity.this, LoginActivity.class);
			startActivity(i);
			//finish();

		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
