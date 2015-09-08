package com.petcarekl.patcareteam2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.petcare.teamiki.R;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
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

public class Register extends Activity implements OnClickListener {

	private EditText user, pass, fname, lname;
	private Button mRegister;
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
		user = (EditText) findViewById(R.id.usernameRegister);
		pass = (EditText) findViewById(R.id.passwordRegister);
		fname = (EditText) findViewById(R.id.firstname);
		lname = (EditText) findViewById(R.id.lastName);
		profileimageview = (ImageView) findViewById(R.id.commentimage);
		profileimageview.setClickable(true);

		
		ByteArrayOutputStream bytedata = new ByteArrayOutputStream();
		Bitmap NOimageadded= BitmapFactory.decodeResource((Register.this).getResources(), R.drawable.defaultimageonprofile);
		NOimageadded.compress(CompressFormat.JPEG, 100, bytedata);

		byte[] dataa = bytedata.toByteArray();
		textofimage = Base64.encodeToString(dataa, Base64.DEFAULT);
		
		
		profileimageview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				openImageIntent();

			}
		});

		mRegister = (Button) findViewById(R.id.btnRegister);
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
		String username = user.getText().toString();
		String password = pass.getText().toString();
		String firstname = fname.getText().toString();
		String lastname = lname.getText().toString();

		if (firstname.length() == 0) {
			fname.setError("Firstname can't be null!");
		} else if (lastname.length() == 0) {
			lname.setError("Lastname can't be null!");
		} else if ((username.length() < 5) || (username.length() > 12)) {
			user.setError("Username must be length form 5 to 12 characters !");
		} else if ((password.length() < 6) || (password.length() > 12)) {
			pass.setError("Password must be length form 6 to 12 characters !");
		} else {

			new CreateUser().execute();
		}

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
			String firstname = fname.getText().toString();
			String lastname = lname.getText().toString();

			byte[] data_firstname = null;
			byte[] data_lastname = null;
			byte[] data_password = null;
			byte[] data_username = null;
			try {
				data_firstname = firstname.getBytes("UTF-8");
				data_lastname = lastname.getBytes("UTF-8");
				data_password = password.getBytes("UTF-8");
				data_username = username.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String firstname_base64 = Base64.encodeToString(data_firstname,
					Base64.DEFAULT);
			String lastname_base64 = Base64.encodeToString(data_lastname,
					Base64.DEFAULT);
			String password_base64 = Base64.encodeToString(data_password,
					Base64.DEFAULT);
			String username_base64 = Base64.encodeToString(data_username,
					Base64.DEFAULT);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String nameoffile_image = sdf.format(new Date());

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username_base64));
				params.add(new BasicNameValuePair("password", password_base64));
				params.add(new BasicNameValuePair("firstname", firstname_base64));
				params.add(new BasicNameValuePair("lastname", lastname_base64));
				params.add(new BasicNameValuePair("profileimage", textofimage));
				params.add(new BasicNameValuePair("filename_profileimage",
						nameoffile_image));

				Log.d("request!", "starting");

				// Posting user data to script
				JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL,
						"POST", params);

				// full json response
				Log.d("Registering attempt", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("User Created!", json.toString());
					finish();
					return json.getString(TAG_MESSAGE);
				} else {
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
			if (file_url != null) {
				Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG)
						.show();
			}

		}

	}

	// _____________KIKO_COMMENTS___________________TAKE_AN_PROFILE_PICTURE_______________________

	private Uri outputFileUri;
	static final int YOUR_SELECT_PICTURE_REQUEST_CODE = 1;

	private void openImageIntent() {

		// Determine Uri of camera image to save.
		final File root = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "MyDir" + File.separator);
		root.mkdirs();
		final String fname = "img_" + System.currentTimeMillis() + ".jpg";
		final File sdImageMainDirectory = new File(root, fname);
		outputFileUri = Uri.fromFile(sdImageMainDirectory);

		// Camera.
		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		final PackageManager packageManager = getPackageManager();
		final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
				captureIntent, 0);
		for (ResolveInfo res : listCam) {
			final String packageName = res.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName,
					res.activityInfo.name));
			intent.setPackage(packageName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			cameraIntents.add(intent);
		}

		// Filesystem.
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("image/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

		// Chooser of filesystem options.
		final Intent chooserIntent = Intent.createChooser(galleryIntent,
				"Select Source");

		// Add the camera options.
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

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
						isCamera = action
								.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					}
				}

				Uri selectedImageUri;
				if (isCamera) {
					selectedImageUri = outputFileUri;
				} else {
					selectedImageUri = data == null ? null : data.getData();
				}
				Bitmap bitmap = null;
				try {
					bitmap = MediaStore.Images.Media.getBitmap(
							this.getContentResolver(), selectedImageUri);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Bitmap resized;

				int kolku_da_skrati_w = 0;
				int kolku_da_skrati_h = 0;
				if (bitmap.getWidth() > bitmap.getHeight()) {
					kolku_da_skrati_w = bitmap.getWidth() - bitmap.getHeight();
				} else {
					kolku_da_skrati_h = bitmap.getHeight() - bitmap.getWidth();
				}

				ByteArrayOutputStream bytedata = new ByteArrayOutputStream();

				// Bitmap croppedBmp = Bitmap.createBitmap(bitmap, 0, 0,
				// bitmap.getWidth()-kolku_da_skrati_w,bitmap.getHeight()-kolku_da_skrati_h);

				// croppedBmp = Bitmap.createScaledBitmap(croppedBmp, 256, 256,
				// false);
				// croppedBmp.compress(CompressFormat.JPEG, 100, bytedata);

				// byte[] dataa = bytedata.toByteArray();
				// textofimage= Base64.encodeToString(dataa, Base64.DEFAULT);

				// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

				int rotate = 0;
				try {
					File imageFile = new File(getPath(selectedImageUri));

					ExifInterface exif = new ExifInterface(
							imageFile.getAbsolutePath());
					int orientation = exif.getAttributeInt(
							ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_NORMAL);

					switch (orientation) {
					case ExifInterface.ORIENTATION_ROTATE_270:
						rotate = 270;
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						rotate = 180;
						break;
					case ExifInterface.ORIENTATION_ROTATE_90:
						rotate = 90;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Matrix matrix = new Matrix();
				matrix.postRotate(rotate);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), matrix, true);

				bitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, false);
				bitmap.compress(CompressFormat.JPEG, 100, bytedata);

				byte[] dataa = bytedata.toByteArray();
				textofimage = Base64.encodeToString(dataa, Base64.DEFAULT);

				// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

				Picasso.with(Register.this)
						.load(getImageUri(Register.this, bitmap))
						.resize(100, 100).centerCrop().into(profileimageview);

				// resized = Bitmap.createScaledBitmap(bitmap, 1280, 720, true);
				// profileimageview.setImageBitmap(bitmap);

			}
		}
	}

	// _____________KIKO_COMMENTS___________________TAKE_AN_PROFILE_PICTURE_______________________

	@SuppressLint("NewApi")
	public String getPath(Uri contentUri) {// Will return "image:x*"
		String wholeID = DocumentsContract.getDocumentId(contentUri);

		// Split at colon, use second item in the array
		String id = wholeID.split(":")[1];

		String[] column = { MediaStore.Images.Media.DATA };

		// where id is equal to
		String sel = MediaStore.Images.Media._ID + "=?";

		Cursor cursor = getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
				new String[] { id }, null);

		String filePath = "";

		int columnIndex = cursor.getColumnIndex(column[0]);

		if (cursor.moveToFirst()) {
			filePath = cursor.getString(columnIndex);
		}

		cursor.close();
		return filePath;
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

}
