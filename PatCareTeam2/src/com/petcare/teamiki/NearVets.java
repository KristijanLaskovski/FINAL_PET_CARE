package com.petcare.teamiki;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearVets extends android.support.v4.app.Fragment implements
		LocationListener {
	// instance variables for Marker icon drawable resources
	private int userIcon, vetIcon, petStoreIcon, pharamIcon, otherIcon;

	// the map
	private GoogleMap theMap;

	// location manager
	private LocationManager locMan;

	// user marker
	private Marker userMarker;

	// places of interest
	private Marker[] placeMarkers;
	// max
	private final int MAX_PLACES = 20;// most returned from google
	// marker options
	private MarkerOptions[] places;
	private ConnectivityManager connMgr;

	public NearVets() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		
		
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		
		boolean isWifiConn = networkInfo.isConnected();

		NetworkInfo networkInfo2 = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		boolean isMobileConn = networkInfo2.isConnected();
		
		if(isWifiConn==true || isMobileConn==true){
			//******************************************************************************************************************************************************
		
		View rootView = inflater.inflate(R.layout.fragment_near_vets, container,
				false);
		
		
		
		
		//get drawable IDs
		userIcon = R.drawable.park;
		vetIcon = R.drawable.zelen;
		petStoreIcon = R.drawable.rozev;
		pharamIcon = R.drawable.plav;
		//otherIcon = R.drawable.purple_point;

		//find out if we already have it
		if(theMap==null){
			//get the map
			FragmentManager manager=getActivity().getFragmentManager();
			theMap = ((MapFragment)manager.findFragmentById(R.id.the_map)).getMap();
			//check in case map/ Google Play services not available
			if(theMap!=null){
				//ok - proceed
				theMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				//create marker array
				placeMarkers = new Marker[MAX_PLACES];
				//update location
				updatePlaces();
			}

		}

		return rootView;
		//******************************************************************************************************************************************************
		
		
		
		
		}
			
		else
		{
			//Toast.makeText(getActivity(), "You dont have internet connection", Toast.LENGTH_SHORT).show();
			//startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
			View rootView = inflater.inflate(R.layout.error_network, container,
					false);
			return rootView ;
			
		}
		

	}

	@Override
	public void onLocationChanged(Location location) {
		Log.v("MyMapActivity", "location changed");
		updatePlaces();

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.v("MyMapActivity", "status changed");

	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.v("MyMapActivity", "provider enabled");

	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.v("MyMapActivity", "provider disabled");

	}
	/*
	 * update the place markers
	 */
	private void updatePlaces(){
		//get location manager
		locMan =(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		
		//get last location
		Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		double lat = lastLoc.getLatitude();
		double lng = lastLoc.getLongitude();
		//create LatLng
		LatLng lastLatLng = new LatLng(lat, lng);

		//remove any existing marker
		if(userMarker!=null) userMarker.remove();
		//create and set marker properties
		userMarker = theMap.addMarker(new MarkerOptions()
		.position(lastLatLng)
		.title("You are here")
		.icon(BitmapDescriptorFactory.fromResource(userIcon))
		.snippet("Your last recorded location"));
		//move to location
		theMap.animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000, null);
		
		String types = "pet_store|veterinary_care|pharmacy";
		try {
		types = URLEncoder.encode(types, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		}
		
		
		
		//build places query string
		String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
				"json?location="+lat+","+lng+
				"&radius=10000&sensor=true" +
				"&types=" + types +
				"&key=AIzaSyAMiwKXcb8JS0gpYTREINWdb37nJ4yl2k8";//ADD SERVER KEY
		
		Log.d("KIKOOOOOOOOOOOOOOOOOOOOOOOOOOOOOo", placesSearchStr);
	//	Toast.makeText( getActivity(), placesSearchStr,Toast.LENGTH_LONG).show();
		
		//execute query
		new GetPlaces().execute(placesSearchStr);
		System.out.println(placesSearchStr);
		locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100000, this);
	}
	private class GetPlaces extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... placesURL) {
			//fetch places
			
			//build result as string
			StringBuilder placesBuilder = new StringBuilder();
			//process search parameter string(s)
			for (String placeSearchURL : placesURL) {
				HttpClient placesClient = new DefaultHttpClient();
				try {
					//try to fetch the data
					
					//HTTP Get receives URL string
					HttpGet placesGet = new HttpGet(placeSearchURL);
					//execute GET with Client - return response
					HttpResponse placesResponse = placesClient.execute(placesGet);
					//check response status
					StatusLine placeSearchStatus = placesResponse.getStatusLine();
					//only carry on if response is OK
					if (placeSearchStatus.getStatusCode() == 200) {
						//get response entity
						HttpEntity placesEntity = placesResponse.getEntity();
						//get input stream setup
						InputStream placesContent = placesEntity.getContent();
						//create reader
						InputStreamReader placesInput = new InputStreamReader(placesContent);
						//use buffered reader to process
						BufferedReader placesReader = new BufferedReader(placesInput);
						//read a line at a time, append to string builder
						String lineIn;
						while ((lineIn = placesReader.readLine()) != null) {
							placesBuilder.append(lineIn);
						}
					}
				}
				catch(Exception e){ 
					e.printStackTrace(); 
				}
			}
			return placesBuilder.toString();
		}
		//process data retrieved from doInBackground
		protected void onPostExecute(String result) {
			//parse place data returned from Google Places
			//remove existing markers
			if(placeMarkers!=null){
				Log.v("Place", "** placeMarkers != null ");
				for(int pm=0; pm<placeMarkers.length; pm++){
					if(placeMarkers[pm]!=null)
						placeMarkers[pm].remove();
				}
			} else {
				Log.v("Place", "** placeMarkers == null ");
			}
			try {
				//parse JSON
				Log.v("Place", "try to parse JSON ~~ ");
				//create JSONObject, pass stinrg returned from doInBackground
				JSONObject resultObject = new JSONObject(result);
				Log.v("Place", "-- RESULT OBJECT JSON " + resultObject.toString());
				//get "results" array
				JSONArray placesArray = resultObject.getJSONArray("results");
				
				//marker options for each place returned
				places = new MarkerOptions[placesArray.length()];
				if(places == null){
					Log.v("Place", "00 Places is null before try");
					Log.v("Place", "00 Places length is " + places.length);
				} else {
					Log.v("Place", "Places is not before try");
					Log.v("Place", "Places length is " + places.length);
				}
				//loop through places
				for (int p=0; p<placesArray.length(); p++) {
					//parse each place
					//if any values are missing we won't show the marker
					boolean missingValue=false;
					LatLng placeLL=null;
					String placeName="";
					String vicinity="";
					int currIcon = otherIcon;
					try{
						//attempt to retrieve place data values
						missingValue=false;
						//get place at this index
						JSONObject placeObject = placesArray.getJSONObject(p);
						
						Log.v("Place","Ova e place Object"+ placeObject.toString());
						
						//get location section
						JSONObject loc = placeObject.getJSONObject("geometry")
								.getJSONObject("location");
						//read lat lng
						placeLL = new LatLng(Double.valueOf(loc.getString("lat")), 
								Double.valueOf(loc.getString("lng")));	
						//get types
						JSONArray types = placeObject.getJSONArray("types");
						//loop through types
						for(int t=0; t<types.length(); t++){
							//what type is it
							String thisType=types.get(t).toString();
							//check for particular types - set icons
							if(thisType.contains("veterinary_care")){
								currIcon = vetIcon;
								break;
							}
							else if(thisType.contains("pet_store")){
								currIcon = petStoreIcon;
								break;
							}
							else if(thisType.contains("pharmacy")){
								currIcon = pharamIcon;
								break;
							}
						}
						//vicinity
						vicinity = placeObject.getString("vicinity");
						//name
						placeName = placeObject.getString("name");
					}
					catch(JSONException jse){
						Log.v("Place", "missing value");
						missingValue=true;
						jse.printStackTrace();
					}
					//if values missing we don't display
					if(missingValue)	places[p]=null;
					else
						places[p]=new MarkerOptions()
					.position(placeLL)
					.title(placeName)
					.icon(BitmapDescriptorFactory.fromResource(currIcon))
					.snippet(vicinity);
					
					Log.v("Place", "Places length is " + places.length);
				}
			}
			catch (Exception e) {
				Log.v("Place", "cannot parse JSON ~~ ");
				e.printStackTrace();
			}
			Log.v("Place", "I am here after catch!!");
			if(places!=null && placeMarkers!=null){
				Log.v("Place", "Placess length is " + places.length);
				Log.v("Place", "Places markers length is " + placeMarkers.length);
				for(int p=0; p<places.length && p<placeMarkers.length; p++){
					//will be null if a value was missing
					if(places[p]!=null){
						Log.v("Place", "Place[p] is not null");
						placeMarkers[p]=theMap.addMarker(places[p]);
					} else {
						Log.v("Place", places[p].getTitle());
					}
						
				}
			} else {
				if(places == null){
					Log.v("Place", "place is null");
				} else {
					Log.v("Place", "marker is null");
				}
			}
			
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(theMap!=null){
			locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(theMap!=null){
			locMan.removeUpdates(this);
		}
	}

}
