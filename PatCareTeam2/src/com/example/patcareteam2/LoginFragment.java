package com.example.patcareteam2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment implements View.OnClickListener {
	
	EditText etUserName,etPassword;
	Button btnLogin;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_login, container,
				false);

		return rootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		etUserName=(EditText)view.findViewById(R.id.etUsername);
		etPassword=(EditText)view.findViewById(R.id.etPassword);
		btnLogin=(Button)view.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		}

	@Override
	public void onClick(View v) {
		
		// TODO Auto-generated method stub

		switch(v.getId()){
		case R.id.btnLogin :
			Toast.makeText(getActivity(), "Login", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}