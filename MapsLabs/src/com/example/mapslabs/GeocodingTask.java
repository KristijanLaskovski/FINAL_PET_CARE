package com.example.mapslabs;

import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

public class GeocodingTask extends AsyncTask<Object, Void, Address> {

	/* GEOCODER is a class for handling geocoding and reverse geocoding. Geocoding
	   is a process of transforming a street addres or other description of a location
	   into a (latitude, longitude) coordinate. Reverse geocoding is the process of 
	   transforming (latitude, longitude) coordinate into a (partial) address.
	*/
	private Geocoder gCoder;
	private GeocoderAddressListener listener;

	public GeocodingTask(Context context, GeocoderAddressListener listener) {
		gCoder = new Geocoder(context);
		this.listener = listener;
	}

	@Override
	protected Address doInBackground(Object... params) {

		List<Address> addresses = null;
		Address clickedAddress = null;
		try {

			if (params.length > 0) {
				if (params[0] instanceof LatLng) {
					// reverse geocoding - vnesuvam (latitude, longitude) coordinate.
					LatLng point = (LatLng) params[0];
					addresses = gCoder.getFromLocation(point.latitude,
							point.longitude, 1);
					// 1 - max results : onaa adresa koja smeta GoogleMaps deka e najtochna.
				} else if (params[0] instanceof String) {
					// geocoding - vnesuvam adresa primerot so Skopje.
					addresses = gCoder.getFromLocationName((String) params[0],
							1);
				} else {
					System.err.println("Invalid paramethers");
				}
			}
			if (addresses != null && addresses.size() > 0) {
				clickedAddress = addresses.get(0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return clickedAddress;
	}

	@Override
	protected void onPostExecute(Address result) {
		super.onPostExecute(result);
		listener.onAddressObtained(result);
	}

}
