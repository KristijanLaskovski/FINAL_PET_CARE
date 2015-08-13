package com.example.mapslabs;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

public class ObtainingCityDataTask  extends AsyncTask<Void, Void, Boolean> {

	
	
	private ArrayList<City> cities;
	private String geodata = "http://194.149.138.20/geodata.json";
	private ObtainingDataListener listener;
	
	public ObtainingCityDataTask(ObtainingDataListener listener) {
		cities = new ArrayList<City>();
		this.listener = listener;
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(geodata);
			HttpResponse response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == 200) {

				
				HttpEntity entity = response.getEntity();
				String responseData = EntityUtils.toString(entity);
				JSONObject jsonObj = new JSONObject(responseData);
				JSONArray jsonArr = jsonObj.getJSONArray("geonames");
				
				for (int i = 0; i < jsonArr.length(); i++) {

					
					JSONObject jsonCity = jsonArr.getJSONObject(i);
					City newCity = new City();
					newCity.setFcodeName(jsonCity.getString("fcodeName"));
					newCity.setToponymName(jsonCity
							.getString("toponymName"));
					newCity.setCountrycode(jsonCity
							.getString("countrycode"));
					newCity.setFcl(jsonCity.getString("fcl"));
					newCity.setFclName(jsonCity.getString("fclName"));
					newCity.setName(jsonCity.getString("name"));
					newCity.setWikipedia(jsonCity.getString("wikipedia"));
					newCity.setLng(jsonCity.getDouble("lng"));	
					newCity.setFcode(jsonCity.getString("fcode"));
					newCity.setGeonameId(jsonCity.getInt("geonameId"));
					newCity.setLat(jsonCity.getDouble("lat"));
					newCity.setPopulation(jsonCity.getInt("population"));
					cities.add(newCity);
				}

				return true;

			}

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (JSONException e) {

			e.printStackTrace();

		}

		return false;

	}

	@Override
	protected void onPostExecute(Boolean result) {
		
		if (result) {
			listener.onDataObtained(cities);
		}
		
		super.onPostExecute(result);
	}
}
