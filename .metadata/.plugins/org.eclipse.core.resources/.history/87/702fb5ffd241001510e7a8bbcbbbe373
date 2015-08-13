package com.example.mapslabs;

import java.util.ArrayList;

import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends ActionBarActivity {

	private GoogleMap mMap;

	// Declaring a Location Manager
	// provides access to the system location services.
	protected LocationManager locationManager;

	private Location mobileLocation;

	private static ArrayList<City> cities;

	private ArrayList<Marker> markers;

	private String provider;

	private boolean drewOnce;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cities = new ArrayList<City>();
		markers = new ArrayList<Marker>();

		loadGoogleMap();

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

		// Getting the name of the provider that meets the criteria
		provider = locationManager.getBestProvider(criteria, false);
		
		if (provider == null || provider.equals("")) {

			Toast.makeText(getBaseContext(), "No Provider Found",
					Toast.LENGTH_LONG).show();
		}

		
	}

	public ArrayList<City> getCities() {
		return cities;
	}

	public void setCities(ArrayList<City> cities) {
		this.cities = cities;
	}

	@Override
	protected void onStart() {

		drewOnce = false;
		// locationManager.requestLocationUpdates(provider, 10000000, 10,
		// MyLocationListener );

		super.onStart();
	}

	@Override
	protected void onStop() {
		// locationManager.removeUpdates(MyLocationListener);
		super.onStop();
	}

	private void loadGoogleMap() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			mMap.setMyLocationEnabled(true);
			// Check if we were successful in obtaining the map.
			if (mMap != null) {

				setUpMap();
			
			}
		}
	}

	private void setUpMap() {

		GeocodingTask task = new GeocodingTask(this,
				new GeocoderAddressListener() {

					@Override
					public void onAddressObtained(Address address) {
						if (address != null) {
							/*
							 * newLatLngZoom returns CameraUpdate that moves the
							 * center of the screen to a latitude and longitude
							 * specified by LatLng object and moves it to a
							 * specified zoom level (in this example 10).
							 */
							mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
									new LatLng(address.getLatitude(), address
											.getLongitude()), 7));
						} else {
							mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
									new LatLng(42.0062403, 21.3559413), 7));
						}

					}
				});

		task.execute("Skopje"); //* znachi ako go najde skopje, kje slushne listener-ot
		// GeocoderAddressListener()

		setUpCities();

		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {

				for (int j = 0; j < markers.size(); j++) {

					if (markers.get(j).getSnippet()
							.equalsIgnoreCase(marker.getSnippet())) {
						Intent intent = new Intent(MainActivity.this,
								CityActivity.class);
						intent.putExtra("name", cities.get(j).getName());
						intent.putExtra("countryCode", cities.get(j)
								.getCountrycode());
						intent.putExtra("wikipedia", cities.get(j)
								.getWikipedia());
						intent.putExtra("population", cities.get(j)
								.getPopulation());
						intent.putExtra("fcodename", cities.get(j)
								.getFcodeName());
						startActivity(intent);
					}
				}

				return false;
			}
		});
		
		mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {

			@Override
			public void onMyLocationChange(Location location) {

				
					drawMarkerAtCurrentLocation(location);
				
			}
		});
	}

	private void setUpCities() {

		ObtainingCityDataTask task = new ObtainingCityDataTask(
				new ObtainingDataListener() {

					@Override
					public void onDataObtained(ArrayList<City> cities) {

						MainActivity.cities = cities;

						for (City city : cities) {

							markers.add(

							mMap.addMarker(new MarkerOptions().position(
									new LatLng(city.getLat(), city.getLng()))
									.snippet(city.getName())

							)

							);
						}
					}
				});

		task.execute();
	}

	/*
	 * LocationListener MyLocationListener = new LocationListener() {
	 * 
	 * @Override public void onStatusChanged(String provider, int status, Bundle
	 * extras) { // TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onProviderEnabled(String provider) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onProviderDisabled(String provider) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onLocationChanged(Location location) {
	 * drawMarkerAtCurrentLocation(location); } };
	 */
	public void drawMarkerAtCurrentLocation(Location location) {

		TextView tv = (TextView) findViewById(R.id.txt);

		tv.setText("Latitude: " + location.getLatitude() + " Longitude: "
				+ location.getLongitude());
		if (drewOnce == false) {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(location.getLatitude(), location.getLongitude()), 10));
			drewOnce = true;
		}
		mMap.addMarker(new MarkerOptions()
				.position(
						new LatLng(location.getLatitude(), location
								.getLongitude()))
				.title(location.getProvider())
				.snippet(provider)
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	}

}
