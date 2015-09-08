package com.petcarekl.patcareteam2;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.petcare.teamiki.R;
import com.petcarekl.patcareteam2.CustumAdapterForComments.ReplayOnCommentInterface;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class User_Profile_Visit extends ListActivity   implements ReplayOnCommentInterface{
	
	
	  TextView firstname,lastname;
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
		private ArrayList<CommentItem> NEW_Comments ;
		String post_image;
		String profileOf_USER;
	    String Firstname;
	    String Lastname;
	    String ProfilePicture;
		
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user__profile__visit);
		Intent i= getIntent();
        Bundle b = i.getExtras();
        if(b!=null)
        {
        	profileOf_USER =(String) b.get("POST_USERNAME");
        }
        new LoadComments().execute();
	}

	
	
	public class LoadComments extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(User_Profile_Visit.this);
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
	
	
	
	
        }//loadcomments
	
	
	
	private void updateList() {
		CustumAdapterForComments adapter=new CustumAdapterForComments(User_Profile_Visit.this, NEW_Comments);
        
		adapter.setCallback(this);
		ListView lv = getListView();	
		
		LayoutInflater inflater = LayoutInflater.from(User_Profile_Visit.this);
		  RelativeLayout rl=(RelativeLayout)inflater.inflate(R.layout.header_profile, null);
	      //proba za na profile
	      TextView firstname=(TextView)rl.findViewById(R.id.profilFirstName);
	      firstname.setText(Firstname);
	      TextView lastname=(TextView)rl.findViewById(R.id.profileLastName);
	      lastname.setText(Lastname);
	      TextView numberofComments=(TextView)rl.findViewById(R.id.tvNumberOfComments);
	      numberofComments.setText("54 comments");
	      ImageView profileimage=(ImageView)rl.findViewById(R.id.replayImage);
	     // profileimage.setImageBitmap(b);
	      //proba za na profile
	      
	      Picasso.with(User_Profile_Visit.this).load(ProfilePicture)
	        .into(profileimage);
		
		
		
		lv.addHeaderView(rl);
		
		
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

		
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
                //NE RABOTIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
			
			}
		});
	}//updatelist
	
	
	
public void updateJSONdata() {

	NEW_Comments=new ArrayList<CommentItem>();

         int success;

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username",profileOf_USER));

				Log.d("request!", "starting");
				// getting product details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(READ_COMMENTS_PROFILE_URL, "POST",
						params);

				// check your log for json response
				Log.d("Login attempt", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					
				
						mComments = json.getJSONArray(TAG_POSTS);

						// looping through all posts according to the json object returned
						for (int i = 0; i < mComments.length(); i++) {
							JSONObject c = mComments.getJSONObject(i);
							// gets the content of each tag
							String title = c.getString(TAG_TIME);
							String content = c.getString(TAG_MESSAGE);
							String username = c.getString(TAG_USERNAME);
							String firstname = c.getString(TAG_FIRST_NAME);
							
							String lastname = c.getString(TAG_LAST_NAME);
							
			             	String image_c = c.getString("image_c");
							String image_p = c.getString("image_p");
							
							String contact = c.getString("contact");
							String langitude = c.getString("latitude");
							String longitude = c.getString("longitude");
							String type_c = c.getString("typecomment");
							String post_id=c.getString("post_id");
							String post_address=c.getString("address");
							String post_hasimage=c.getString("hasimage");
							String content_text="";
							String firstname_text="";
							String lastname_text="";
							byte[] data = Base64.decode(content, Base64.DEFAULT);
							byte[] data_firstname = Base64.decode(firstname, Base64.DEFAULT);
							byte[] data_lastname = Base64.decode(lastname, Base64.DEFAULT);
							try {
								content_text = new String(data, "UTF-8");
								firstname_text = new String(data_firstname, "UTF-8");
								lastname_text = new String(data_lastname, "UTF-8");
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							Firstname=firstname_text ;
							Lastname=lastname_text;
							ProfilePicture=image_p;
							
							
							CommentItem comitem=new CommentItem(username,content_text, firstname_text,lastname_text, title, image_p, image_c, langitude, longitude, contact, type_c,post_id,post_address,post_hasimage);
							NEW_Comments.add(comitem);
							
						}

										
				}
					
				}catch (JSONException e) {
					e.printStackTrace();
				}  
		
	
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user__profile__visit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	public void startActivityForReplayComent(String post_id) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void startActivityForShowingLocation(String langitude,
			String latitude) {
		// TODO Auto-generated method stub
		
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
