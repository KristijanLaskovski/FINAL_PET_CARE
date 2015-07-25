package com.example.patcareteam2;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class HomeFragment extends  android.support.v4.app.Fragment {
     
    public HomeFragment(){}
     


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
          
        return rootView;
    }
}