package com.example.patcareteam2;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.patcareteam2.LoginActivity.AttemptLogin;
import com.google.android.gms.location.LocationListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CommentActivity extends Activity implements OnClickListener {

	EditText insertedcomment;
	private Button  mSubmit;
	private Button  mTakePhoto;
	
	/* location */
	private Button  mCheckLocation;
	private AlertDialog.Builder dialogBuilder;
	private LocationManager locationManager; /* provides access to the system location services */
	private Location deviceLocation; /* my location */
	private String provider;
	private final long LOCATION_REFRESH_TIME = 1000;
	private final float LOCATION_REFRESH_DISTANCE = 1;
	
	/* ONLY FOR DEBUGGING */
	TextView latlng;
	/* ONLY FOR DEBUGGING */
	
	CommentActivity context = this;
	
	/* location */
	
	 // Progress Dialog
    private ProgressDialog pDialog;
 
    ImageView mImageView;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    //php add a comment script
    
    //localhost :  
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
   // private static final String POST_COMMENT_URL = "http://xxx.xxx.x.x:1234/webservice/addcomment.php";
    
    //testing on Emulator:
   // private static final String POST_COMMENT_URL = "http://192.168.1.130/webservice/addcomment.php";
    
  //testing from a real server:
    private static final String POST_COMMENT_URL = "http://www.petcarekl.com/webservice/addcomment.php";
    
    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
	
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		insertedcomment=(EditText)findViewById(R.id.ETforEnteringCommnets);
		
		mSubmit = (Button)findViewById(R.id.btnPostComment);
		mSubmit.setOnClickListener(this);
		
		mTakePhoto = (Button)findViewById(R.id.BtnTakeAPhoto);
		mTakePhoto.setOnClickListener(this);
		
		mImageView=(ImageView)findViewById(R.id.takenImage1);
		
		/* location */
		mCheckLocation = (Button)findViewById(R.id.checkLocation);
		mCheckLocation.setOnClickListener(this);
		latlng = (TextView) findViewById(R.id.latlng); /* ONLY FOR DEBUGGING */
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); /* i am getting location services from location manager */
		
		boolean gpsEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean netEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		if (!gpsEnabled && !netEnabled){
			/* starting settings so the user enables them, TODO what if the user, doesn't enables them, stop everything.
			 * TODO make safeguard against - matching Activity may not exist - read more: hover ACTION_NETWORK_OPERATOR_SETTINGS */
			Toast.makeText(this, "gps and net are desabled", Toast.LENGTH_LONG);
			startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));
			Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","gps and net are enabled");	
		}
		
		if(gpsEnabled){
			Toast.makeText(this, "gps is enabled", Toast.LENGTH_LONG);
			Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","gps is enabled");	
			
		}
		
		if(netEnabled){
			Toast.makeText(this, "net is enabled", Toast.LENGTH_LONG);
			Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","net is enabled");	
		}
		
		Criteria criteria = new Criteria(); /* kriterium spored koj kje izbereme network or gps */
		// Getting the name of the provider that meets the criteria
		provider = locationManager.getBestProvider(criteria, false);
				
		if (provider == null || provider.equals("")) {
			Toast.makeText(getBaseContext(), "No Provider Found",Toast.LENGTH_LONG).show(); /* shouldn't happen. */
		}
		/* AKO IMA PROBLEM mozhno e da e tuka so castiranjeto na lokacijata */
		 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
				 LOCATION_REFRESH_DISTANCE ,  new android.location.LocationListener() {
					
					@Override
					public void onStatusChanged(String provider, int status, Bundle extras) {
						// TODO handle
						
					}
					
					@Override
					public void onProviderEnabled(String provider) {
						// TODO handle
						
					}
					
					@Override
					public void onProviderDisabled(String provider) {
						
						Toast.makeText(context, "Your provider" + provider + " is disabled ", Toast.LENGTH_LONG).show();
						
						/* TODO handle this */
					}
					
					@Override
					public void onLocationChanged(Location location) {
						// TODO Auto-generated method stub
						Toast.makeText(context, "onLocationChanged", Toast.LENGTH_LONG).show();
						deviceLocation = location;
					}
				});
	}

	static final int REQUEST_IMAGE_CAPTURE = 1;
/*
	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
	}*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  /*  if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        Bundle extras = data.getExtras();
	        Bitmap imageBitmap = (Bitmap) extras.get("data");
	        mImageView.setImageBitmap(imageBitmap);
	    }*/
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			
			Log.d("++++++++++++++++++++++++++++POMINAVME NA requestCode == REQUEST_TAKE_PHOTO ","Tuka  photoFile = createImageFile();");
		      
		if (mCurrentPhotoPath != null) {
			
			
			Log.d("***************************"," Vo IF sme vlezeni ");
			
			setPic();
			galleryAddPic();
			mCurrentPhotoPath = null;
		}
		}
	}
	int locationChoise = -1;
	public void chooseLocationDialog(){
		
		/* Variables */
		dialogBuilder = new AlertDialog.Builder(this);
		final String[] types = {"Obtain my location", "Add location"};
		
		/* Process */
		dialogBuilder.setTitle("Enter your location");
		
		dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (locationChoise) {
				case 0:{
					Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","Obtain your location");	
					if (deviceLocation != null) {
						double lat = deviceLocation.getLatitude();
						double lng = deviceLocation.getLongitude();
						latlng.setText("Latitude is: " + lat + " and longitude is " + lng);
						/* save lat and lng in our database */
						
					} else {
						Toast.makeText(context, "deviceLocation is null ", Toast.LENGTH_LONG).show();
					}
					dialog.cancel();
					break;
				}
				case 1:{
					Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","Add new location");	
					
					/* SE OTVARA NOV DIALOG SO INPUT TEXT - JA PISHUVASH ADRESATA I TAA ADRESA SE BARA :) */
					/* SAMO NE RABOTI SEGA RADI TOA SHTO NASHEVO AKTIVITY IMPLEMENTIRA ONLCLICKLISTENER, KJE GO SREDAM
					 * POSLE RABOTA DENESKA, NA RABOTA :D */
				/*	GeocodingTask task = new GeocodingTask(this,
							new GeocoderAddressListener() {

								@Override
								public void onAddressObtained(Address address) {
									if (address != null) {
										
									} else {
									
									}

								}
							});

					task.execute("Skopje");*/
					
					break;
				}
				default:
					break;
				}
			}
		});
		
		dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		});
		/* plus positive i negative OK i cancel */
		
		
		dialogBuilder.setSingleChoiceItems(types, -1, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				locationChoise = which;

			}
		});
		
		/* Output */
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btnPostComment:
			new PostComment().execute();
			break;
		case R.id.BtnTakeAPhoto:
			dispatchTakePictureIntent();
			Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","Tuka se povikuva kamerata");	
			break;
		case R.id.checkLocation:
			chooseLocationDialog();
			/* dialog box */
			
			break;
		}
		
		
		
		
		
		
	}
	
	//hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh
	
	
	String mCurrentPhotoPath;

	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = image.getAbsolutePath();
	    return image;
	}
	
	
	static final int REQUEST_TAKE_PHOTO = 1;

	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        	Log.d("YESSSSSSSSSSSSSSSSSSSSS","USPESNO  photoFile = createImageFile();");
	  	      
	            
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	        	Log.d("ERORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR","Tuka  photoFile = createImageFile();");
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	                    Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	        }
	    }
	}
	
	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}
	
	
	private void setPic() {
	    // Get the dimensions of the View
	    int targetW = mImageView.getWidth();
	    int targetH = mImageView.getHeight();

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Log.d(" ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"," postvivame image View ");
	    
	    
	    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    mImageView.setImageBitmap(bitmap);
	}
	
	

	
	//hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh
	
class PostComment extends AsyncTask<String, String, String> {
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CommentActivity.this);
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
            
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	           String currentDateandTime = sdf.format(new Date());
            
            String post_title =currentDateandTime ;
            String post_message = insertedcomment.getText().toString();
            
            //We need to change this:
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(CommentActivity.this);
            String post_username = sp.getString("username", "anon");
           
            //tuka dodadov promena
            String post_fname = sp.getString("firstname", "anon");
            String post_lname= sp.getString("lastname", "anon");
            
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", post_username));
                params.add(new BasicNameValuePair("title", post_title));
                params.add(new BasicNameValuePair("message", post_message));
               //tuka dodadov promena
                params.add(new BasicNameValuePair("firstname", post_fname));
                params.add(new BasicNameValuePair("lastname", post_lname));
                
                
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
                	finish();
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
            	Toast.makeText(CommentActivity.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
	

}
