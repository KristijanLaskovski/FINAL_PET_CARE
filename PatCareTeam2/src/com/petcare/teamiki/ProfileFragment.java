package com.petcare.teamiki;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.petcare.teamiki.CustumAdapterForComments.ReplayOnCommentInterface;
import com.petcare.teamiki.R;
import com.squareup.picasso.Picasso;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ProfileFragment extends android.support.v4.app.ListFragment
		implements ReplayOnCommentInterface {

	TextView firstname, lastname;
	ListView listViewOnHome;
	Button btnAddAcommnet;
	ImageView profileview;
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
	ArrayAdapter<CommentItem> arrayAdapterofComments;
	private ArrayList<CommentItem> NEW_Comments;
	String post_image;
	private ConnectivityManager connMgr;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		
		
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		
		boolean isWifiConn = networkInfo.isConnected();

		NetworkInfo networkInfo2 = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		boolean isMobileConn = networkInfo2.isConnected();
		
		if(isWifiConn==true || isMobileConn==true){
		//---------------------------------------------------------------------------------------------------------------
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String post_fname = sp.getString("firstname", "anon");
		String post_lname = sp.getString("lastname", "anon");
		post_image = sp.getString("pimage", "anon");
		// byte[] decodedByte = Base64.decode(post_image, Base64.DEFAULT);
		// Bitmap b = BitmapFactory.decodeByteArray(decodedByte, 0,
		// decodedByte.length);
		listViewOnHome = (ListView) inflater.inflate(R.layout.fragment_comment,
				container, false);

		listViewOnHome
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// selectItem(position);
					}
				});

		RelativeLayout rl = (RelativeLayout) inflater.inflate(
				R.layout.header_profile, null);
		// proba za na profile
		TextView firstname = (TextView) rl.findViewById(R.id.profilFirstName);
		firstname.setText(post_fname);
		TextView lastname = (TextView) rl.findViewById(R.id.profileLastName);
		lastname.setText(post_lname);
		TextView numberofComments = (TextView) rl
				.findViewById(R.id.tvNumberOfComments);
		numberofComments.setText("");
		ImageView profileimage = (ImageView) rl.findViewById(R.id.replayImage);
		// profileimage.setImageBitmap(b);
		// proba za na profile

		Picasso.with(getActivity()).load(post_image).into(profileimage);

		profileimage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			//	Toast.makeText(getActivity(),"edit profile", Toast.LENGTH_LONG).show();
				
			}
		});
		
		
		
		rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		
		listViewOnHome.addHeaderView(rl);
		new LoadComments().execute();
		return listViewOnHome;
		}
		else
		{
			View rootView = inflater.inflate(R.layout.error_network, container,
					false);
			return rootView ;
			
		}
		
		
		
		
		
		
		
		//----------------------------------------------------------------------------------------------------------------
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		// new LoadComments().execute();
	}

	@Override
	public void startActivityForShowingLocation(String langitude,
			String latitude) {
		if(langitude.equals("0.0")&&latitude.equals("0.0")){}else{

			Intent i = new Intent(getActivity(), Map_activity_pet_location.class);
			i.putExtra("LONGITUDE", langitude);
			i.putExtra("LATITUDE", latitude);
			startActivity(i);
			}

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
	}

	public void updateJSONdata() {

		NEW_Comments = new ArrayList<CommentItem>();

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		String post_username = sp.getString("username", "anon");

		int success;
		// mCommentList = new ArrayList<HashMap<String, String>>();

		try {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", post_username));

			Log.d("request!", "starting");
			// getting product details by making HTTP request
			JSONObject json = jsonParser.makeHttpRequest(
					READ_COMMENTS_PROFILE_URL, "POST", params);

			// check your log for json response
			Log.d("Login attempt", json.toString());

			// json success tag
			success = json.getInt(TAG_SUCCESS);
			if (success == 1) {

				mComments = json.getJSONArray(TAG_POSTS);

				// looping through all posts according to the json object
				// returned
				for (int i = 0; i < mComments.length(); i++) {

					JSONObject c = mComments.getJSONObject(i);
					// gets the content of each tag
					String title = c.getString(TAG_TIME);
					String content = c.getString(TAG_MESSAGE);
					String username = c.getString(TAG_USERNAME);
					String firstname = c.getString(TAG_FIRST_NAME);
					String lastname = c.getString(TAG_LAST_NAME);
					String image_c = c.getString("image_c");
					// String image_p = c.getString("image_p");
					String contact = c.getString("contact");
					String langitude = c.getString("latitude");
					String longitude = c.getString("longitude");
					String type_c = c.getString("typecomment");
					String post_id = c.getString("post_id");
					String post_address = c.getString("address");
					String post_hasimage = c.getString("hasimage");
					String content_text = "";
					String firstname_text = "";
					String lastname_text = "";
					byte[] data = Base64.decode(content, Base64.DEFAULT);
					byte[] data_firstname = Base64.decode(firstname,
							Base64.DEFAULT);
					byte[] data_lastname = Base64.decode(lastname,
							Base64.DEFAULT);
					try {
						content_text = new String(data, "UTF-8");
						firstname_text = new String(data_firstname, "UTF-8");
						lastname_text = new String(data_lastname, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					CommentItem comitem = new CommentItem(username,
							content_text, firstname_text, lastname_text, title,
							post_image, image_c, langitude, longitude, contact,
							type_c, post_id, post_address, post_hasimage);
					NEW_Comments.add(comitem);

				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void updateList() {
		CustumAdapterForComments adapter = new CustumAdapterForComments(
				getActivity(), NEW_Comments);

		adapter.setCallback(this);
		ListView lv = getListView();
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// NE RABOTIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
				Toast.makeText(getActivity(), position, Toast.LENGTH_SHORT)
						.show();

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
		}

	}

	@Override
	public void startActivityForCallingPerson(String phone) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startActivityVisitUser(String username) {
		// TODO Auto-generated method stub

	}

}