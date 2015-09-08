package com.petcarekl.patcareteam2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.petcare.teamiki.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;



public class CommentActivity extends AppCompatActivity implements OnClickListener {

	EditText insertedcomment;
    ImageView mImageView;
	private Button  mSubmit;
	private Button  mTakePhoto;
	private Button  mTypeComment;
	private Button  mPhoneNumber;
	private String image_comment_encoded;
	Bitmap resized;
	RadioButton rb;
	private String rb_text;
	private String phonnumbercontact="";

	  String AdresaNaPostot="";
	String Has_image_Comment="false";
	
	
	/* location */
	private Button  mCheckLocation;
	private AlertDialog.Builder dialogBuilder; // reused
	private LocationManager locationManager; /* provides access to the system location services */
	private Location deviceLocation; /* my location */
	private String provider;
	private final long LOCATION_REFRESH_TIME = 1000;
	private final float LOCATION_REFRESH_DISTANCE = 1;
	
	/* ONLY FOR DEBUGGING */
	TextView latlng;
	/* ONLY FOR DEBUGGING */
	
	TextView locationAddress; /* this is for our post, so it can be shown the actual street address */
	double lat, lng;
	private EditText addLocation;
	private EditText phoneNumber;
	ImageView imv2;
	Drawable myDrawable;
	TextView tv2;
	ImageView imv1;
	Drawable myDrawable1;
	TextView tv1;
	ImageView imv3;
	Drawable myDrawable3;
	TextView tv3;
	
	
	CommentActivity context = this;
	
	/* location */
	
	 // Progress Dialog
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String POST_COMMENT_URL = "http://www.petcarekl.com/webservice/addcommentsimage.php";
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
		mTypeComment= (Button)findViewById(R.id.typecomentBTN);
		mTypeComment.setOnClickListener(this);
		
		imv1=(ImageView)findViewById(R.id.smallIMGtypeComment);
		 myDrawable1 = getResources().getDrawable(R.drawable.t);
		tv1=(TextView)findViewById(R.id.tvsmallTypeComment);
		imv2=(ImageView)findViewById(R.id.smallIMGContact);
		 myDrawable = getResources().getDrawable(R.drawable.c);
		tv2=(TextView)findViewById(R.id.tvsmallContactPhone);
		imv3=(ImageView)findViewById(R.id.smallIMGlocation);
		 myDrawable3 = getResources().getDrawable(R.drawable.l);
		tv3=(TextView)findViewById(R.id.tvsmalllocation);
		
		
		mPhoneNumber= (Button)findViewById(R.id.callbtnInfo);
		mPhoneNumber.setOnClickListener(this);
		/*KIKO_COMMENTS   proba so radio button */
		rb_text="Lost pet";
	
		/* location */
		mCheckLocation = (Button)findViewById(R.id.checkLocation);
		mCheckLocation.setOnClickListener(this);
	//	latlng = (TextView) findViewById(R.id.latlng); /* ONLY FOR DEBUGGING */
		//locationAddress = (TextView) findViewById(R.id.locationAddress);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); /* i am getting location services 

from location manager */
		
		boolean gpsEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean netEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		if (!gpsEnabled && !netEnabled){
			/* starting settings so the user enables them, TODO what if the user, doesn't enables them, stop 

everything.
			 * TODO make safeguard against - matching Activity may not exist - read more: hover 

ACTION_NETWORK_OPERATOR_SETTINGS */
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
			Toast.makeText(getBaseContext(), "No Provider Found",Toast.LENGTH_LONG).show(); /* shouldn't 

happen. */
			/* TODO no toast - only for debugging */
			provider = "no";
			
			
		} else {
			
			Toast.makeText(getBaseContext(), "Provider is " + provider ,Toast.LENGTH_LONG).show(); /* shouldn't 

happen. */
			
			deviceLocation = locationManager.getLastKnownLocation(provider);
		if (locationManager.getLastKnownLocation(provider) == null) {
			/* get location based on some other app's knowledge for your app */

			Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","LOCATION FROM PASSIVE PROVIDER");	
			deviceLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		}else {

			Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","LOCATION FROM YOUR PROVIDER");	
			deviceLocation = locationManager.getLastKnownLocation(provider);
		}
			 locationManager.requestLocationUpdates(provider, LOCATION_REFRESH_TIME,
					 LOCATION_REFRESH_DISTANCE ,  new android.location.LocationListener() {
						
						@Override
						public void onStatusChanged(String provider, int status, Bundle extras) {
							// TODO handle
							
						}
						
						@Override
						public void onProviderEnabled(String provider) {
							// TODO handle
							
							StringBuilder message = new StringBuilder("Your provider " + provider + " is disalbed ");
							if(provider.equals("gps")){
									if(locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
										message.append(" but network provider is still enabled");
									} else {
										message.append(" and network priver is disabled too.");
									}
								} else if (provider.equals("network")){
									if(locationManager
											.isProviderEnabled

(LocationManager.GPS_PROVIDER)){
																

message.append(" but gps provider is still enabled");
															} 

else {
																

message.append(" and gps priver is disabled too.");
															}
									
								} else {
									message.append("what");
								}
								/* TODO handle this */
							
							Toast.makeText(context, message.toString(), 

Toast.LENGTH_LONG).show();
							
						}
						
						@Override
						public void onProviderDisabled(String provider) {
							StringBuilder message = new StringBuilder("Your provider " + 

provider + " is disalbed ");
						if(provider.equals("gps")){
								if(locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
									message.append(" but network provider is still enabled");
								} else {
									message.append(" and network priver is disabled too.");
									Intent intent = new Intent

(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									startActivity(intent);
								}
							} else if (provider.equals("network")){
								if(locationManager
										.isProviderEnabled

(LocationManager.GPS_PROVIDER)){
															

message.append(" but gps provider is still enabled");
														} else {
															

message.append(" and gps priver is disabled too.");
															

Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
															

startActivity(intent);
														}
								
							} else {
								message.append("what");
							}
							/* TODO handle this */
						
						Toast.makeText(context, message.toString(), Toast.LENGTH_LONG).show();
						
						}
						
						@Override
						public void onLocationChanged(Location location) {
							// TODO Auto-generated method stub
						//	Toast.makeText(context, "onLocationChanged", Toast.LENGTH_LONG).show();
							deviceLocation = location;
							Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","Your location is changed");	
						}
					});
		}
		
	
	}
	int locationChoise = -1;
	public void chooseLocationDialog(){
		
		/* Variables */
		dialogBuilder = new AlertDialog.Builder(this);
		final String[] types = {"Obtain my location", "Add new location"};
		
		/* Process */
		dialogBuilder.setTitle("Choose location");
		
		dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (locationChoise) {
				case 0:{
					Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","Obtain your location");	
					if (deviceLocation != null) {
						 lat = deviceLocation.getLatitude();
						 lng = deviceLocation.getLongitude();
						 
						 Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","Device location is not null");	
						//latlng.setText("Latitude is: " + lat + " and longitude is " + lng);
						/* 001 HERE YOU GET THE LOCATION, SAVE IT IN YOUR EXTRAS */
						
						/* now we're using GeocoderTask again, to obtain */
						GeocodingTask task = new GeocodingTask(context, new GeocoderAddressListener() {
							
							@Override
							public void onAddressObtained(Address address) {
								if (address != null) {
									// locationAddress.setText("Location address is: " + address.getThoroughfare() + " " +  address.getFeatureName() + " "  + address.getPostalCode() + " " + address.getCountryName() );	
								
								String addresfull="";
								if(address.getThoroughfare()!=null)
									addresfull+=address.getThoroughfare()+" ";
								if(address.getFeatureName()!=null)
									addresfull+=address.getFeatureName()+" ";
								if( address.getPostalCode()!=null)
									addresfull+= address.getPostalCode()+" ";
								if(address.getCountryName()!=null)
									addresfull+=address.getCountryName()+" ";
								imv3.setImageDrawable(myDrawable3);
								tv3.setText(addresfull);
								AdresaNaPostot=addresfull;
								
								}
								
							}
						});
						
						task.execute(new LatLng(lat, lng));
						
					} else {
						Toast.makeText(context, "deviceLocation is null ", Toast.LENGTH_LONG).show();
						Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","Device location is null");	
					}
					dialog.cancel();
					break;
				}
				case 1:{
					Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","Add new location");	
					addNewLocationDialog();
					dialog.cancel();
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
	
	/* if the user doesn't want to use its own location, but wants to type it in */
	public void addNewLocationDialog(){
		
		/* Variables */
		dialogBuilder = new AlertDialog.Builder(this);
		
		
		/* Process */
		dialogBuilder.setTitle("Enter new location");
		addLocation = new EditText(this);
		dialogBuilder.setView(addLocation);
		dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/* so task manager-ot */
				
				Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","ON OKAY");	
				GeocodingTask task = new GeocodingTask(context, new GeocoderAddressListener(){

					@Override
					public void onAddressObtained(Address address) {
						Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","HALLO");	
						if (address != null) { /* if address is not null we know it's the correct 

one, since that's
						why we use GecodingTask.*/
							Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","Address is not null.");	
								 lat = address.getLatitude();
								 lng = address.getLongitude();
								 
								 
									String addresfull="";
									if(address.getThoroughfare()!=null)
										addresfull+=address.getThoroughfare()+" ";
									if(address.getFeatureName()!=null)
										addresfull+=address.getFeatureName()+" ";
									if( address.getPostalCode()!=null)
										addresfull+= address.getPostalCode()+" ";
									if(address.getCountryName()!=null)
										addresfull+=address.getCountryName()+" ";
									imv3.setImageDrawable(myDrawable3);
									tv3.setText(addresfull);
									AdresaNaPostot=addresfull;
								 
								 
								// latlng.setText("Latitude is: " + lat + " and longitude is " + lng);
								// locationAddress.setText("Location address is: " + address.getThoroughfare() + " " +  address.getFeatureName() + " "  + address.getPostalCode() + " " + address.getCountryName() );
								/* 001 HERE YOU GET THE LOCATION, SAVE IT IN YOUR EXTRAS 
								 * ti si go znaesh najdobro kodot, no mislam deka najubavo 

e na edno mesto
								 * site podatoci da gi stavash, znachi da ne gi stavash 

tuka odma vo baza, tuku 
								 * da apdejtirash ednash na kraj koga kje klikne POST, 

zasho mozhebi nekoj kje se
								 * predomisli i kje saka da editne neshto i na sekoe 

editnuvanje kje pravish ista rabota
								 * (valjda vaka ti e, jas samo pishuvam onaka :P zasho 

dosadnen mi e zhivotot pa i jas
								 * sum dosadna) :P
								 * */
						} else {
							/* 001 DALI MISLITE DEKA TREBA TUKA DA PRIMI OKEJ DA SE VRATI NAZAD 

I USTVARI DA NEMA
							 * SETIRANO LOKCIJA, ISTO KAKO CANCEL, ILI DA ISKOCHI TOAST, MORASH 

DA VNESESH NESHTO,
							 * - MENE MI E DA NE IMAME PREMNOGU TOAST-OVI NIZ CELATA 

APLIKACIJA, ZASHO GLEDAM DEKA
							 * NEMA MNOGU VO NAJNOVIVE, MNOGU RETKO, ILI PA IM ISKACHA NESHTO 

OD GORE, MOZHEME
							 * I TOA DA GO NAJDEME */
							
							imv3.setImageDrawable(myDrawable3);
							tv3.setText("address not found ");
						}
						
					}
					
				});
				
				task.execute(addLocation.getText().toString());
				dialog.cancel();
			}
		});
		
		dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.typecomentBTN:
			Log.d("##############################33","Tuka se povikuva ALERT DIALOGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");	
			
			chooseTypeOfComment();
			/* dialog box */
			break;
		case R.id.btnPostComment:
			new PostComment().execute();
			break;
		case R.id.callbtnInfo:
			addPhoneNumber();
			break;
		case R.id.BtnTakeAPhoto:
		//	dispatchTakePictureIntent();
			openImageIntent();
			Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&","Tuka se povikuva kamerata");	
			break;
		case R.id.checkLocation:
			chooseLocationDialog();
			/* dialog box */
			break;
	
		}

	}
	
	//KIKO_-------------------------------------------------------------------
	
	
	
	
	int typeofComentChoose = -1;
	public void chooseTypeOfComment(){
		Log.d("##############################33","Tuka se povikuva chooseTypeOfComment()");	
		
		/* Variables */
		dialogBuilder = new AlertDialog.Builder(this);
		final String[] types_comment = {"You lost/found a pet", "Searching for a new pet","Give or sell a pet"};
		
		/* Process */
		dialogBuilder.setTitle("Choose type of post");
		
		dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (typeofComentChoose) {
				case 0:{
					rb_text="Lost pet";
					imv1.setImageDrawable(myDrawable1);
					tv1.setText(rb_text);
					dialog.cancel();
					break;
				}
				case 1:{
					rb_text="Searching for pet";
					imv1.setImageDrawable(myDrawable1);
					tv1.setText(rb_text);
					dialog.cancel();
					break;
				}
				case 2:{
					rb_text="Sell a pet";
					imv1.setImageDrawable(myDrawable1);
					tv1.setText(rb_text);
					dialog.cancel();
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
				imv1.setImageDrawable(myDrawable1);
				tv1.setText("Just a post");
				dialog.cancel();
				
			}
		});
		/* plus positive i negative OK i cancel */
		
		
		dialogBuilder.setSingleChoiceItems(types_comment, -1, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				typeofComentChoose = which;

			}
		});
		
		/* Output */
		AlertDialog dialogtypecommnet = dialogBuilder.create();
		dialogtypecommnet.show();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//KIKO_-------------------------------------------------------------------
	
	public void addPhoneNumber(){
		
		/* Variables */
		dialogBuilder = new AlertDialog.Builder(this);
		
		
		/* Process */
		dialogBuilder.setTitle("Enter phone number");
		phoneNumber = new EditText(this);
		phoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
		phoneNumber.setFilters(new InputFilter[] {new InputFilter.LengthFilter(9)});
		
		dialogBuilder.setView(phoneNumber);
		dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(phoneNumber.getText().toString().length()==9){
					phonnumbercontact=phoneNumber.getText().toString();
				
					imv2.setImageDrawable(myDrawable);
					tv2.setText(phonnumbercontact);
				}else{
					Toast.makeText(CommentActivity.this, "Phone number must be from 9 characters!", 

Toast.LENGTH_SHORT).show();                   
					imv2.setImageDrawable(myDrawable);
					tv2.setText("No contact added !");
				}
				dialog.cancel();
			}
		});
		
		dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}
	
	
	

//hhhhhhhhhhhhhhhhhhhhhhhhh__________KIKO_COMMENT____POST___PHOTO_______________hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh
	
	private Uri outputFileUri;
	static final int YOUR_SELECT_PICTURE_REQUEST_CODE = 1;
	private void openImageIntent() {

	// Determine Uri of camera image to save.
	final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
	root.mkdirs();
	final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
	final File sdImageMainDirectory = new File(root, fname);
	outputFileUri = Uri.fromFile(sdImageMainDirectory);

	    // Camera.
	    final List<Intent> cameraIntents = new ArrayList<Intent>();
	    final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	    final PackageManager packageManager = getPackageManager();
	    final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
	    for(ResolveInfo res : listCam) {
	        final String packageName = res.activityInfo.packageName;
	        final Intent intent = new Intent(captureIntent);
	        intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
	        intent.setPackage(packageName);
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
	        cameraIntents.add(intent);
	    }

	    // Filesystem.
	    final Intent galleryIntent = new Intent();
	    galleryIntent.setType("image/*");
	    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

	    // Chooser of filesystem options.
	    final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

	    // Add the camera options.
	    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size

()]));

	    startActivityForResult(chooserIntent, YOUR_SELECT_PICTURE_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK) {
	        if (requestCode == YOUR_SELECT_PICTURE_REQUEST_CODE) {
	            final boolean isCamera;
	            if (data == null) {
	                isCamera = true;
	            } else {
	                final String action = data.getAction();
	                if (action == null) {
	                    isCamera = false;
	                } else {
	                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	                }
	            }

	            Uri selectedImageUri;
	            if (isCamera) {
	                selectedImageUri = outputFileUri;
	            } else {
	                selectedImageUri = data == null ? null : data.getData();
	            }
	            Bitmap bitmap=null;
	            try {
					 bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), 

selectedImageUri);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            ByteArrayOutputStream bytedata = new ByteArrayOutputStream();
	          //  bitmap = Bitmap.createScaledBitmap( bitmap, 512, 256, false);
	            
	            int newimage_w=300;
	            int newimage_h=300;
	            if(bitmap.getWidth()>bitmap.getHeight()){
	            	newimage_w=512;
	            	newimage_h=300;
	            }else{
	            	newimage_w=300;
	            	newimage_h=512;
	            }
	            
	            
	            bitmap = Bitmap.createScaledBitmap( bitmap, newimage_w, newimage_h, false);
	            bitmap.compress(CompressFormat.JPEG, 100, bytedata);
	            byte[] dataa = bytedata.toByteArray();
	            image_comment_encoded= Base64.encodeToString(dataa, Base64.DEFAULT);
	            Has_image_Comment="true";
	            mImageView.setImageBitmap(bitmap);
	            
	        }
	    }
	}
	
	

//hhhhhhhhhhhhhhhhhhhhhhhhh__________KIKO_COMMENT____POST___PHOTO_______________hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh
	
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
            
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm  MMM dd,yyyy");
	        String currentDateandTime = sdf.format(new Date());
            String post_title =currentDateandTime ;
            String post_message = insertedcomment.getText().toString();
            
            byte[] data = null;
			try {
				data = post_message.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            String post_message_base64 = Base64.encodeToString(data, Base64.DEFAULT);
            
            
            
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(CommentActivity.this);
            String post_username = sp.getString("username", "anon");
            /*
            byte[] data_usernme = null;
			try {
				data =post_username.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            String post_username_base64 = Base64.encodeToString(data_usernme, Base64.DEFAULT);
            
            */
            
            
            String post_fname = sp.getString("firstname", "anon");
            String post_lname= sp.getString("lastname", "anon");
            String post_image_progile= sp.getString("pimage", "anon");

            
            SimpleDateFormat sdf_comment_name = new SimpleDateFormat("HHmmMMMddyyyy");
            String cfile_commnet_name = sdf_comment_name.format(new Date());
            
            
            try {
            	
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", post_username));
                params.add(new BasicNameValuePair("title", post_title));
                params.add(new BasicNameValuePair("message", post_message_base64));
                params.add(new BasicNameValuePair("firstname", post_fname));
                params.add(new BasicNameValuePair("lastname", post_lname));
                String post_long = Double.toString(lng);
                String post_lat = Double.toString(lat);
                params.add(new BasicNameValuePair("longitude",post_long));
                params.add(new BasicNameValuePair("latitude", post_lat));
                params.add(new BasicNameValuePair("typecomment", rb_text));
                params.add(new BasicNameValuePair("image_c", image_comment_encoded));
                params.add(new BasicNameValuePair("image_p", post_image_progile));
                params.add(new BasicNameValuePair("contact", phonnumbercontact));
                params.add(new BasicNameValuePair("comment_image_name", cfile_commnet_name));
                params.add(new BasicNameValuePair("address", AdresaNaPostot));
                params.add(new BasicNameValuePair("hasimage", Has_image_Comment));
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
