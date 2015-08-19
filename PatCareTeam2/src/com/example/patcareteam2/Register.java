package com.example.patcareteam2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Register extends Activity  implements OnClickListener{
	
	private EditText user, pass,fname,lname;
	private Button  mRegister;
	private ImageView profileimageview;
	private String textofimage;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String REGISTER_URL = "http://www.petcarekl.com/webservice/registernew.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		user = (EditText)findViewById(R.id.usernameRegister);
		pass = (EditText)findViewById(R.id.passwordRegister);
		fname = (EditText)findViewById(R.id.firstname);
		lname = (EditText)findViewById(R.id.lastName);
		profileimageview=(ImageView)findViewById(R.id.commentimage);
		profileimageview.setClickable(true);
		profileimageview.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	
		    	openImageIntent();
		    }
		});

		mRegister = (Button)findViewById(R.id.btnRegister);
		mRegister.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		new CreateUser().execute();
	}
	
class CreateUser extends AsyncTask<String, String, String> {

		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();
            String firstname= fname.getText().toString();
            String lastname = lname.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("firstname", firstname));
                params.add(new BasicNameValuePair("lastname", lastname));
                params.add(new BasicNameValuePair("profileimage", textofimage));
 
                Log.d("request!", "starting");
                
                //Posting user data to script 
                JSONObject json = jsonParser.makeHttpRequest(
                       REGISTER_URL, "POST", params);
 
                // full json response
                Log.d("Registering attempt", json.toString());
 
                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("User Created!", json.toString());              	
                	finish();
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
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
            	Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}





//_____________KIKO_COMMENTS___________________TAKE_AN_PROFILE_PICTURE_______________________

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
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

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
				 bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            //Bitmap resized;
           
            int kolku_da_skrati_w=0;
            int kolku_da_skrati_h=0;
            if(bitmap.getWidth()>bitmap.getHeight()){
            	  kolku_da_skrati_w=bitmap.getWidth()-bitmap.getHeight();
            }else{
                  kolku_da_skrati_h=bitmap.getHeight()-bitmap.getWidth();
            }
            
            
            
            ByteArrayOutputStream bytedata = new ByteArrayOutputStream();
            
            Bitmap croppedBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth()-kolku_da_skrati_w,bitmap.getHeight()-kolku_da_skrati_h);
            
            croppedBmp = Bitmap.createScaledBitmap(croppedBmp, 256, 256, false);
            croppedBmp.compress(CompressFormat.JPEG, 100, bytedata);
           
            byte[] dataa = bytedata.toByteArray();
            textofimage= Base64.encodeToString(dataa, Base64.DEFAULT);
            
            
         // resized = Bitmap.createScaledBitmap(bitmap, 1280, 720, true); 
            profileimageview.setImageBitmap(croppedBmp);
       
           
            
            
            
        }
    }
}


//_____________KIKO_COMMENTS___________________TAKE_AN_PROFILE_PICTURE_______________________























	
	
}
