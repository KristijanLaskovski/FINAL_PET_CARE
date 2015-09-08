package com.petcarekl.patcareteam2;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.petcare.teamiki.R;

public class CommentsOnPost extends ListActivity {



	Button btnAddReplayComment;
	EditText enteredReplay;
	String post_id ;
	ListView listViewOnReplayComment;
	private ArrayList<ReplayedItem> Replayed_items;
	
	
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String POST_COMMENT_URL = "http://www.petcarekl.com/webservice/addreplaycomment.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_POSTS = "posts";
    private JSONArray mComments = null;
    
    // loadreplaycomments.php
    private static final String READ_COMMENT_URL = "http://www.petcarekl.com/webservice/loadreplaycomments.php";
    
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments_on_post);
		Intent is= getIntent();
        Bundle b = is.getExtras();
        if(b!=null)
        {
          post_id =(String) b.get("POST_ID");
          Log.d("POST_ID*******************8", post_id);
        }
        
        enteredReplay=(EditText)findViewById(R.id.enteredReplaytoPost);
        listViewOnReplayComment=(ListView)findViewById(android.R.id.list);
      //  new LoadComments().execute();
	}
	
	
	public void replayOnComment(View v){
		new PostComment().execute();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// loading the comments via AsyncTask
	new LoadComments().execute();
		 // updateList();
	}
	
	
	
	public class LoadComments extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(CommentsOnPost.this);
			pDialog.setMessage("Loading comments...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {

	            int success;
	            Replayed_items=new ArrayList<ReplayedItem>();
				try {
					// Building Parameters
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("post_id", post_id));

					Log.d("request!", "starting");
					// getting product details by making HTTP request
					JSONObject json = jsonParser.makeHttpRequest(READ_COMMENT_URL, "POST",
							params);

					// check your log for json response
					//Log.d("Login attempt", json.toString());

					// json success tag
					success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
						Log.d("Login Successful!", json.toString());
						// save user data
						
						mComments = json.getJSONArray(TAG_POSTS);
						for (int i = 0; i < mComments.length(); i++) {
							JSONObject c = mComments.getJSONObject(i);
							String time = c.getString("time_post");
							String message = c.getString("message");
							String firstname = c.getString("firstname");
							String lastname = c.getString("lastname");
							String image=c.getString("image");
							
							String message_text="";
							String firstname_text="";
							String lastname_text="";
							byte[] data_firstname = Base64.decode(firstname, Base64.DEFAULT);
							byte[] data_lastname = Base64.decode(lastname, Base64.DEFAULT);
							byte[] data_message = Base64.decode(message, Base64.DEFAULT);
							
							try {
								firstname_text = new String(data_firstname, "UTF-8");
								lastname_text = new String(data_lastname, "UTF-8");
								message_text = new String(data_message, "UTF-8");
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							
							ReplayedItem ri=new ReplayedItem(time, message_text, firstname_text , lastname_text, image);
							Replayed_items.add(ri);
							
							Log.d("Vratn replaycommentar", firstname+" "+lastname+" "+time+" "+ image+" "+message);
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
	
	
	
	
	
	private void updateList() {
		CustumReplayAdapter adapter=new CustumReplayAdapter(CommentsOnPost.this, Replayed_items);
		ListView lv = getListView();	
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

		
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
                //NE RABOTIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
				//Toast.makeText(getActivity(), position,Toast.LENGTH_SHORT).show();

			}
		});
		
	
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
class PostComment extends AsyncTask<String, String, String> {
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CommentsOnPost.this);
            pDialog.setMessage("Posting Comment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
            int success;
            
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm  MMM dd,yyyy");
	        String currentDateandTime = sdf.format(new Date());
            String post_time =currentDateandTime ;
            String post_message="";
            post_message = enteredReplay.getText().toString();
            
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(CommentsOnPost.this);
            String post_username = sp.getString("username", "anon");
            
            byte[] data_message = null;
			try {
				data_message =post_message.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            String post_message_base64 = Base64.encodeToString(data_message, Base64.DEFAULT);
        

            try {
            	
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
               // params.add(new BasicNameValuePair("username", post_username));
                params.add(new BasicNameValuePair("post_id", post_id));
                params.add(new BasicNameValuePair("time_post", post_time));
                params.add(new BasicNameValuePair("message", post_message_base64));
                params.add(new BasicNameValuePair("username", post_username ));
               

                
                
                Log.d("request!", "starting");
                
                //Posting user data to script 
                JSONObject json = jsonParser.makeHttpRequest(
                		POST_COMMENT_URL, "POST", params);
 
                // full json response
                Log.d("Post Comment attempt", json.toString());
 
                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Comment Added!", json.toString());   
                	//  updateList();
                //	finish();
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Comment Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);
                	
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
			
		}
		
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(CommentsOnPost.this, file_url, Toast.LENGTH_LONG).show();
            }
            
            new LoadComments().execute();
 
        }
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
