package com.example.patcareteam2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.patcareteam2.HomeFragment.LoadComments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class ProfileFragment  extends android.support.v4.app.ListFragment implements OnClickListener{
	
	    TextView firstname,lastname;
	    ListView listViewOnHome;
	    Button btnAddAcommnet;
		JSONParser jsonParser = new JSONParser();
		private ProgressDialog pDialog;
		private static final String READ_COMMENTS_PROFILE_URL = "http://www.petcarekl.com/webservice/profilecomments.php";
		// JSON IDS:
		private static final String TAG_SUCCESS = "success";
		private static final String TAG_TIME = "time_post";
		private static final String TAG_POSTS = "posts";
		private static final String TAG_POST_ID = "post_id";
		private static final String TAG_USERNAME = "username";
		private static final String TAG_MESSAGE = "message";
		private static final String TAG_FIRST_NAME = "firstname";
		private static final String TAG_LAST_NAME = "lastname";
		private JSONArray mComments = null;
		// manages all of our comments in a list.
		private ArrayList<HashMap<String, String>> mCommentList;
		SharedPreferences sp;
	    


	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_comment, container, false);
      firstname=(TextView)rootView.findViewById(R.id.profilFirstName);
      lastname=(TextView)rootView.findViewById(R.id.profileLastName);
      sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
      String post_fname = sp.getString("firstname", "anon");
      String post_lname= sp.getString("lastname", "anon");
      firstname.setText(post_fname);
      lastname.setText(post_lname);
      listViewOnHome=(ListView)rootView .findViewById(android.R.id.list);
    	return rootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	//	new LoadComments().execute();
	}

	@Override
	public void onClick(View v) {
	
     }
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// loading the comments via AsyncTask
	new LoadComments().execute();
	}

	public void updateJSONdata() {
/*
		mCommentList = new ArrayList<HashMap<String, String>>();
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);
		try {
			mComments = json.getJSONArray(TAG_POSTS);
			for (int i = 0; i < mComments.length(); i++) {
				JSONObject c = mComments.getJSONObject(i);
				String title = c.getString(TAG_TIME);
				String content = c.getString(TAG_MESSAGE);
				String username = c.getString(TAG_USERNAME);
				String firstname = c.getString(TAG_FIRST_NAME);
				String lastname = c.getString(TAG_LAST_NAME);
				Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&",firstname+ lastname+ title+"    "+content+"     "+username);
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(TAG_TIME, title);
				map.put(TAG_MESSAGE, content);
				map.put(TAG_USERNAME, username);
				map.put(TAG_FIRST_NAME, firstname);
				map.put(TAG_LAST_NAME, lastname);
				// adding HashList to ArrayList
				mCommentList.add(map);
				// annndddd, our JSON data is up to date same with our array
				// list
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}*/
	}
	


	private void updateList() {
		ListAdapter adapter = new SimpleAdapter(getActivity(), mCommentList,
				R.layout.custum_view, new String[] { TAG_MESSAGE, TAG_LAST_NAME,TAG_FIRST_NAME ,TAG_TIME},
				                         new int[] { R.id.TVComment, R.id.etLastName,R.id.tvName ,R.id.TVtime});
		// I shouldn't have to comment on this one:
		setListAdapter(adapter);
		ListView lv = getListView();	
	}
	
	
	
	
	
	public class LoadComments extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading Profile...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {

			  SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
	            String post_username = sp.getString("username", "anon");
	            int success;
	            mCommentList = new ArrayList<HashMap<String, String>>();
	            
				try {
					// Building Parameters
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username", post_username));

					Log.d("request!", "starting");
					// getting product details by making HTTP request
					JSONObject json = jsonParser.makeHttpRequest(READ_COMMENTS_PROFILE_URL, "POST",
							params);

					// check your log for json response
					Log.d("Login attempt", json.toString());

					// json success tag
					success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
						Log.d("Login Successful!", json.toString());
						// save user data
						
						mComments = json.getJSONArray(TAG_POSTS);
						for (int i = 0; i < mComments.length(); i++) {
							JSONObject c = mComments.getJSONObject(i);
							String title = c.getString(TAG_TIME);
							String content = c.getString(TAG_MESSAGE);
							String username = c.getString(TAG_USERNAME);
							String firstname = c.getString(TAG_FIRST_NAME);
							String lastname = c.getString(TAG_LAST_NAME);
							HashMap<String, String> map = new HashMap<String, String>();
							map.put(TAG_TIME, title);
							map.put(TAG_MESSAGE, content);
							map.put(TAG_USERNAME, username);
							map.put(TAG_FIRST_NAME, firstname);
							map.put(TAG_LAST_NAME, lastname);
							mCommentList.add(map);
						}

						
					} else {
						Log.d("Login Failure!", json.getString(TAG_MESSAGE));
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}  
	            
	            
	            
			
			
			
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			updateList();
		}
	}
	
	
	
	
}