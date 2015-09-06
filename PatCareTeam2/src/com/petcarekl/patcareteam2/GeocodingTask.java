package com.petcarekl.patcareteam2;
/* za zemanjeto na adresi pri baranje na izgubeni milenichinja na odredeno rastojanie.  */
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class GeocodingTask extends AsyncTask<Object, Void, Address> {

	/* geocoding: transformig a street address into (latitude, longitude) coordinate. Reverse geocoding is the vice versa. */
	private Geocoder gCoder;
	private GeocoderAddressListener listener;
	
	public GeocodingTask(Context context, GeocoderAddressListener listener) {
		gCoder = new Geocoder(context);
		this.listener = listener;
	}
	
	@Override
	protected Address doInBackground(Object... params) {
		
		List <Address> addresses = null;
		Address address = null;
		
		try {

			if (params.length > 0) {	
				if (params[0] instanceof LatLng) { 
					// reverse geocoding - vnesuvam (latitude, longitude) coordinate.
					Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","PARAMS[0] IS INSTANCE OF LATLNG");	
					LatLng point = (LatLng) params[0];
					addresses = gCoder.getFromLocation(point.latitude,
							point.longitude, 1);
					// 1 - max results : onaa adresa koja smeta GoogleMaps deka e najtochna.
				} else if (params[0] instanceof String) {
					// geocoding - vnesuvam adresa primerot so Skopje.
					Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","PARAMS[0] IS INSTANCE OF STRING - ADDRESS ");	
					addresses = gCoder.getFromLocationName((String) params[0],
							1);
				} else {
					Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","Invalid paramethars.");	
				}
			}
			
			if (addresses != null) {
				address = addresses.get(0);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return address;

	}

	@Override /* after doInBackground() */
	protected void onPostExecute(Address result) {
		super.onPostExecute(result);
		listener.onAddressObtained(result);
	}
	
}
