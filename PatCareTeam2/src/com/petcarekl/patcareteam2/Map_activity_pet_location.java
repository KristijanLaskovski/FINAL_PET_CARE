package com.petcarekl.patcareteam2;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.petcare.teamiki.R;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;
public class Map_activity_pet_location extends FragmentActivity{
	GoogleMap googleMap;
	private String provider;
	public String longitude_s;
	public String latitude_s;
	protected LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_activity_pet_location);
		Intent i= getIntent();
        Bundle b = i.getExtras();
        if(b!=null)
        {
        	longitude_s =(String) b.get("LONGITUDE");
        	latitude_s =(String) b.get("LATITUDE");
        }
        
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		boolean gpsEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean netEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		if (!gpsEnabled && !netEnabled) {

			/*
			 * show settings to allow configuration of current location sources
			 */

			startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

		}
        
        
		// Creating an empty criteria object
				Criteria criteria = new Criteria();
        
        
provider = locationManager.getBestProvider(criteria, false);
		
		if (provider == null || provider.equals("")) {

		Toast.makeText(getBaseContext(), "No Provider Found",Toast.LENGTH_LONG).show();
		}
        
        
        
		loadGoogleMap();
	}

	private void loadGoogleMap() {
		if (googleMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			googleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			googleMap.setMyLocationEnabled(true);
			// Check if we were successful in obtaining the map.
			if (googleMap != null) {

			setUpMap();
			
			}
		}
	}
	
	private void setUpMap() {
		Double la=Double.parseDouble(latitude_s);
		Double lo=Double.parseDouble(longitude_s);
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
				new LatLng(la, lo ), 7));
		googleMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
			@Override
			public void onMyLocationChange(Location arg0) {
				// TODO Auto-generated method stub
				drawMarkerAtCurrentLocation(arg0);
			}
		});
		
	
	}
	
	
	public void drawMarkerAtCurrentLocation(Location location) {

	
		Double la=Double.parseDouble(latitude_s);
		Double lo=Double.parseDouble(longitude_s);
		googleMap.addMarker(new MarkerOptions()
				.position(new LatLng(la,lo))
				.title(location.getProvider())
				.snippet(provider)
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_activity_pet_location, menu);
		return true;
	}


}
