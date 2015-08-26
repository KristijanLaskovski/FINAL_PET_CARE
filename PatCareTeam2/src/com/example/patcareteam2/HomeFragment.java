package com.example.patcareteam2;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.patcareteam2.CustumAdapterForComments.ReplayOnCommentInterface;



import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
 
public class HomeFragment extends  android.support.v4.app.ListFragment implements ReplayOnCommentInterface {

    public HomeFragment(){
    	
    }
    
    ImageView imslika;
    ArrayAdapter<CommentItem> arrayAdapterofComments;
    private ArrayList<CommentItem> NEW_Comments ;
    ListView listViewOnHome;
    Button btnAddAcommnet;
    // Progress Dialog
	private ProgressDialog pDialog;
	private static final String READ_COMMENTS_URL = "http://www.petcarekl.com/webservice/commentspc.php";

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

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
	    listViewOnHome=(ListView)rootView .findViewById(android.R.id.list);
		btnAddAcommnet=(Button)rootView.findViewById(R.id.addComentBTN);
		btnAddAcommnet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(getActivity(),CommentActivity.class);
				startActivity(i);
			}
		});
		
		
        return rootView;
    }
	
	@Override
	public void startActivityForReplayComent(String post_id) {
		// TODO Auto-generated method stub
		ReplayOnComment(post_id);
	}

	public void ReplayOnComment(String post_id){
		Intent i = new Intent(getActivity(),CommentsOnPost.class);
		i.putExtra("POST_ID", post_id);
		startActivity(i);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// loading the comments via AsyncTask
		new LoadComments().execute();
	}

	
	
	
	
	
	
	//KIKO_COMMENTS ___________________ZA_DIDI_TUKA_PRI_CLICK_NA_GLOBUSOT_SHOW_MAP__________________________________________________________________________________________________________________________
	
	@Override
	public void startActivityForShowingLocation(String longitude,
			String latitude) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), longitude+" "+latitude, Toast.LENGTH_SHORT).show();
	}
	//KIKO_COMMENTS ___________________ZA_DIDI_TUKA_PRI_CLICK_NA_GLOBUSOT_SHOW_MAP__________________________________________________________________________________________________________________________
	
	
	
	
	
	
	/**
	 * Retrieves recent post data from the server.
	 */
	public void updateJSONdata() {

		NEW_Comments=new ArrayList<CommentItem>();
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
				CommentItem comitem=new CommentItem(content, firstname, lastname, title, image_p, image_c, langitude, longitude, contact, type_c,post_id);
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
		CustumAdapterForComments adapter=new CustumAdapterForComments(getActivity(), NEW_Comments);
        
		adapter.setCallback(this);
        
        
		ListView lv = getListView();	
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

		
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
                //NE RABOTIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
				Toast.makeText(getActivity(), position,Toast.LENGTH_SHORT).show();

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





	
	
}