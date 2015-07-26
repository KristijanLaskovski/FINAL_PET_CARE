package com.example.patcareteam2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements View.OnClickListener {
	
	EditText etUserName,etPassword;
	Button btnLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		etUserName=(EditText)findViewById(R.id.etUsername);
		etPassword=(EditText)findViewById(R.id.etPassword);
		btnLogin=(Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnLogin :
			//autntication 
			//autentication("url");
			Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
			Intent i=new Intent("com.example.patcareteam2.MAINACTIVITY");
			startActivity(i);
			finish();
			break;
		}
	}
	public String  autentication(String url)
	{
		    System.out.println(url);
		    StringBuilder sb;
		    String username = "admin";
		    String password = "admin";
		    InputStream is = null;
		    String result = null;

		    String unp = username+":"+password;

		    try {           
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost(url);
		        String encoded_login = Base64.encodeToString(unp.getBytes(), Base64.NO_WRAP);
		        httppost.setHeader(new BasicHeader("Authorization", "Basic "+encoded_login));

		        System.out.println("URL="+httppost.toString());

		        // HttpPost httppost = new HttpPost("http://quantumr.info/qrms/call_service/wse_getitem.php?compkey=astres1312");
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		    } catch (Exception e) {
		        Log.e("log_tag", "Error in http connection" + e.toString());
		    }
		    // convert response to string
		    try {
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
		        sb = new StringBuilder();
		        sb.append(reader.readLine() + "\n");

		        String line = "0";
		        while ((line = reader.readLine()) != null) {
		            sb.append(line + "\n");
		        }
		        is.close();
		        result = sb.toString();
		    } catch (Exception e) {
		        Log.e("log_tag", "Error converting result " + e.toString());
		    }
		    return result;
		}
	}
