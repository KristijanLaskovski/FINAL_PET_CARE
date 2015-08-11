package com.example.patcareteam2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements View.OnClickListener {
	
	EditText etUserName,etPassword;
	Button btnLogin;
	Button btnRegister;
	
	
	// Progress Dialog
		private ProgressDialog pDialog;

		// JSON parser class
		JSONParser jsonParser = new JSONParser();

		// php login script location:

		// localhost :
		// testing on your device
		// put your local ip instead, on windows, run CMD > ipconfig
		// or in mac's terminal type ifconfig and look for the ip under en0 or en1
		// private static final String LOGIN_URL =
		// "http://xxx.xxx.x.x:1234/webservice/login.php";

		// testing on Emulator:
		//private static final String LOGIN_URL = "http://192.168.1.130/webservice/login.php";

		// testing from a real server:
		 private static final String LOGIN_URL = "http://www.petcarekl.com/webservice/login.php";

		// JSON element ids from repsonse of php script:
		private static final String TAG_SUCCESS = "success";
		private static final String TAG_MESSAGE = "message";
		private static final String TAG_FIRST_NAME = "firstname";
		private static final String TAG_LAST_NAME = "lastname";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		etUserName=(EditText)findViewById(R.id.etUsername);
		etPassword=(EditText)findViewById(R.id.etPassword);
		btnLogin=(Button)findViewById(R.id.btnLogin);
		btnRegister=(Button)findViewById(R.id.btnloginRegister);
		
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		//case R.id.btnLogin :
			//autntication 
			//autentication("url");
			//Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
			//Intent i=new Intent("com.example.patcareteam2.MAINACTIVITY");
			//startActivity(i);
			//finish();
			//break;
		case R.id.btnLogin:
			new AttemptLogin().execute();
			break;
		case R.id.btnloginRegister:
			Intent i = new Intent(this, Register.class);
			startActivity(i);
			break;
		}
	}
	
	
	
	class AttemptLogin extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Attempting login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			int success;
			String username = etUserName.getText().toString();
			String password = etPassword.getText().toString();
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", password));

				Log.d("request!", "starting");
				// getting product details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
						params);

				// check your log for json response
				Log.d("Login attempt", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Login Successful!", json.toString());
					// save user data
					
					String ime=json.getString(TAG_FIRST_NAME);
					String prezime=json.getString(TAG_LAST_NAME);
					
					Log.d("*****************************************************************************************************************", ime+prezime);
					//dotuka raboti i vraka ime i prezime tocno
					
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(LoginActivity.this);
					Editor edit = sp.edit();
					edit.putString("username", username);
					edit.putString("firstname", ime);
					edit.putString("lastname", prezime);
					edit.commit();
					
					
					Intent i=new Intent("com.example.patcareteam2.MAINACTIVITY");
					finish();
					startActivity(i);

					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
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
				Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG).show();
			}

		}

	}
	
	
	
	
	
	}
