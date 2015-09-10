package com.petcare.teamiki;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.petcare.teamiki.CustumAdapterForComments.ReplayOnCommentInterface;
import com.petcare.teamiki.LoginActivity.AttemptLogin;
import com.petcare.teamiki.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserManager;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class HomeFragment extends android.support.v4.app.ListFragment implements
		ReplayOnCommentInterface {

	public HomeFragment() {

	}

	boolean loadingMore = true;
	//int pageCount = 1;
	ImageView imslika;
	CustumAdapterForComments adapter;
	ArrayAdapter<CommentItem> arrayAdapterofComments;
	private ArrayList<CommentItem> NEW_Comments;
	ListView listViewOnHome;
	Button btnAddAcommnet;
	View footer;

	// Progress Dialog
	private ProgressDialog pDialog;
	private static final String READ_COMMENTS_URL = "http://www.petcarekl.com/webservice/commentspc.php";
	private static final String READ_REMINDING_COMMENTS_URL = "http://www.petcarekl.com/webservice/remidingcomments.php";
	private ConnectivityManager connMgr;
	// webservice/remidingcomments.php

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
			View rootView = inflater.inflate(R.layout.fragment_home, container,
					false);
			
			btnAddAcommnet = (Button) rootView.findViewById(R.id.addComentBTN);
			btnAddAcommnet.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent i = new Intent(getActivity(), CommentActivity.class);
					// startActivity(i);
					startActivityForResult(i, 1);
					// new LoadComments().execute();

				}
			});
			listViewOnHome = (ListView) rootView.findViewById(android.R.id.list);
			// Add footer view
			LinearLayout ll = (LinearLayout) inflater.inflate(
					R.layout.progresbar_in_footer, null, false);
			listViewOnHome.addFooterView(ll);
			listViewOnHome.setOnScrollListener(new AbsListView.OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView absListView, int i) {
					loadingMore = false;

				}

				@Override
				public void onScroll(AbsListView absListView, int firstItem,
						int visibleItemCount, final int totalItems) {
					// Log.e("Get position", "--firstItem:" + firstItem +
					// "  visibleItemCount:" + visibleItemCount + "  totalItems:" +
					// totalItems + "  pageCount:" + pageCount);
					int lastInScreen = firstItem + visibleItemCount;
					if ((lastInScreen == totalItems) && loadingMore == false   ) {

						new LoadREMINDINGComments().execute();
						// Toast.makeText(getActivity(),
						// "on scroll",Toast.LENGTH_SHORT ).show();
						loadingMore = true;
					} else {
						listViewOnHome.removeFooterView(footer);
					}

				}
			});

		//	new LoadComments().execute();
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		new LoadComments().execute();
	}

	@Override
	public void startActivityForReplayComent(String post_id) {
		// TODO Auto-generated method stub
		ReplayOnComment(post_id);
	}

	public void ReplayOnComment(String post_id) {
		Intent i = new Intent(getActivity(), CommentsOnPost.class);
		i.putExtra("POST_ID", post_id);
		startActivity(i);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// loading the comments via AsyncTask
		//new LoadComments().execute();
connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		
		
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		
		boolean isWifiConn = networkInfo.isConnected();

		NetworkInfo networkInfo2 = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		boolean isMobileConn = networkInfo2.isConnected();
		
		if(isWifiConn==true || isMobileConn==true){
			//******************************************************************************************************************************************************
		//	updateJSONdata();
			new LoadComments().execute();
			//******************************************************************************************************************************************************
			
			}
				
			else
			{
				//Toast.makeText(getActivity(), "You dont have internet connection", Toast.LENGTH_SHORT).show();
				//startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
				//NEW_Comments.clear();
			//	if(!adapter.isEmpty())
				//      adapter.clear();
				
				
			}
			
			
		
	}

	// KIKO_COMMENTS
	// ___________________ZA_DIDI_TUKA_PRI_CLICK_NA_GLOBUSOT_SHOW_MAP__________________________________________________________________________________________________________________________

	@Override
	public void startActivityForShowingLocation(String longitude,
			String latitude) {
		
		if(longitude.equals("0.0")&&latitude.equals("0.0")){}else{
		
		
		// TODO Auto-generated method stub
	//	Toast.makeText(getActivity(), longitude + " " + latitude,
			//	Toast.LENGTH_SHORT).show();
		Intent i = new Intent(getActivity(), Map_activity_pet_location.class);
		i.putExtra("LONGITUDE", longitude);
		i.putExtra("LATITUDE", latitude);
		startActivity(i);
		}
	}

	// KIKO_COMMENTS
	// ___________________ZA_DIDI_TUKA_PRI_CLICK_NA_GLOBUSOT_SHOW_MAP__________________________________________________________________________________________________________________________

	/**
	 * Retrieves recent post data from the server.
	 */
	public void updateJSONdata() {

		NEW_Comments = new ArrayList<CommentItem>();
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);
		try {
			// I know I said we would check if "Posts were Avail." (success==1)
			// before we tried to read the individual posts, but I lied...
			// mComments will tell us how many "posts" or comments are
			// available
			mComments = json.getJSONArray(TAG_POSTS);

			// looping through all posts according to the json object returned
			for (int i = 0; i < mComments.length(); i++) {
				JSONObject c = mComments.getJSONObject(i);
				// gets the content of each tag
				String title = c.getString(TAG_TIME);
				String content = c.getString(TAG_MESSAGE);

				String content_text = "";
				byte[] data = Base64.decode(content, Base64.DEFAULT);
				try {
					content_text = new String(data, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String username = c.getString(TAG_USERNAME);
				String firstname = c.getString(TAG_FIRST_NAME);
				String lastname = c.getString(TAG_LAST_NAME);

				String firstname_text = "";
				String lastname_text = "";
				byte[] data_firstname = Base64
						.decode(firstname, Base64.DEFAULT);
				byte[] data_lastname = Base64.decode(lastname, Base64.DEFAULT);
				try {
					firstname_text = new String(data_firstname, "UTF-8");
					lastname_text = new String(data_lastname, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String image_c = c.getString("image_c");
				String image_p = c.getString("image_p");
				String contact = c.getString("contact");
				String langitude = c.getString("latitude");
				String longitude = c.getString("longitude");
				String type_c = c.getString("typecomment");
				String post_id = c.getString("post_id");
				String post_address = c.getString("address");
				String post_hasimage = c.getString("hasimage");
				CommentItem comitem = new CommentItem(username, content_text,
						firstname_text, lastname_text, title, image_p, image_c,
						langitude, longitude, contact, type_c, post_id,
						post_address, post_hasimage);
				NEW_Comments.add(comitem);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserts the parsed data into the listview.
	 */
	private void updateList() {
		adapter = new CustumAdapterForComments(getActivity(), NEW_Comments);

		adapter.setCallback(this);

		ListView lv = getListView();
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// NE RABOTIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
				//Toast.makeText(getActivity(), position, Toast.LENGTH_SHORT)
					//	.show();

			}
		});

	}

	public class LoadComments extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading Comments...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			updateJSONdata();
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			updateList();
			loadingMore = false;
		}
	}

	@Override
	public void startActivityForCallingPerson(String phone) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_CALL,
				Uri.parse("tel:" + phone));
		startActivity(intent);

	}

	// _____________lodiranje_na_ostanati_komentari____________________________________________________________________________

	public class LoadREMINDINGComments extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// pDialog = new ProgressDialog(getActivity());
			// pDialog.setMessage("Loading Comments...");
			// pDialog.setIndeterminate(false);
			// pDialog.setCancelable(true);
			// pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			loadingMore = true;
			updateJSONdatareminding();
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			// pDialog.dismiss();
			updatereminfingList();
		}
	}

	private void updatereminfingList() {

		adapter.notifyDataSetChanged();

	}

	public void updateJSONdatareminding() {

		int success;

		try {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("loadedcomments", Integer
					.toString(NEW_Comments.size())));

			// Log.d("!@#$%^&*())(((((((((((((((((((((((((((((((((((((",
			// Integer.toString(NEW_Comments.size()));

			// Log.d("request!", "starting");
			// getting product details by making HTTP request
			JSONParser jsonParser = new JSONParser();
			JSONObject json = jsonParser.makeHttpRequest(
					READ_REMINDING_COMMENTS_URL, "POST", params);

			// check your log for json response
			// Log.d("Login attempt", json.toString());

			// json success tag
			success = json.getInt(TAG_SUCCESS);
			if (success == 1) {

				mComments = json.getJSONArray(TAG_POSTS);
				// Log.d("LENGTH NA MCOMMENTS-------------------------->",Integer.toString(mComments.length())
				// );
				

				// looping through all posts according to the json object
				// returned
				for (int i = 0; i < mComments.length(); i++) {

					JSONObject c = mComments.getJSONObject(i);
					// gets the content of each tag
					String title = c.getString(TAG_TIME);
					String content = c.getString(TAG_MESSAGE);
					String content_text = "";
					byte[] data = Base64.decode(content, Base64.DEFAULT);
					try {
						content_text = new String(data, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String username = c.getString(TAG_USERNAME);
					String firstname = c.getString(TAG_FIRST_NAME);
					String lastname = c.getString(TAG_LAST_NAME);
					String firstname_text = "";
					String lastname_text = "";
					byte[] data_firstname = Base64.decode(firstname,
							Base64.DEFAULT);
					byte[] data_lastname = Base64.decode(lastname,
							Base64.DEFAULT);
					try {
						firstname_text = new String(data_firstname, "UTF-8");
						lastname_text = new String(data_lastname, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String image_c = c.getString("image_c");
					String image_p = c.getString("image_p");
					String contact = c.getString("contact");
					String langitude = c.getString("latitude");
					String longitude = c.getString("longitude");
					String type_c = c.getString("typecomment");
					String post_id = c.getString("post_id");
					String post_address = c.getString("address");
					String post_hasimage = c.getString("hasimage");

					CommentItem comitem = new CommentItem(username,
							content_text, firstname_text, lastname_text, title,
							image_p, image_c, langitude, longitude, contact,
							type_c, post_id, post_address, post_hasimage);
					NEW_Comments.add(comitem);
					// Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
					// "loadedd coments on SCROLL   "+
					// mComments.length()+" "+content);

				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void startActivityVisitUser(String username) {
		// TODO Auto-generated method stub
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		String post_username = sp.getString("username", "anon");

		String clicked_user_text = "";
		String logedin_usename_text = "";
		byte[] data_clicked = Base64.decode(username, Base64.DEFAULT);
		byte[] data_loged = Base64.decode(post_username, Base64.DEFAULT);
		try {
			clicked_user_text = new String(data_clicked, "UTF-8");
			logedin_usename_text = new String(data_loged, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if ((clicked_user_text.equals(logedin_usename_text)) == false) {
			Intent i = new Intent(getActivity(), User_Profile_Visit.class);
			i.putExtra("POST_USERNAME", username);
			startActivity(i);
		}

	}

	// _____________lodiranje_na_ostanati_komentari____________________________________________________________________________

}