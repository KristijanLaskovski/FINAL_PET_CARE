package com.example.patcareteam2;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TipsForPetCare extends android.support.v4.app.ListFragment {

	 private ListView listforTips;
	
  public TipsForPetCare(){
    	
    }
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        listforTips = (ListView) inflater.inflate(
                R.layout.frgment_tips_pet_care, container, false);
        listforTips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // selectItem(position);
            }
        });
        
        
       RelativeLayout rl=(RelativeLayout)inflater.inflate(R.layout.header_tips_for_pet_care, null);
 
       listforTips.addHeaderView(rl);
        
        List dataList = new ArrayList();
        dataList.add(new DrawerItem( getString(R.string.petcare1), R.drawable.pet1));
        dataList.add(new DrawerItem(getString(R.string.petcare2),R.drawable.pet2));
        dataList.add(new DrawerItem(getString(R.string.petcare3), R.drawable.pet3));  
        dataList.add(new DrawerItem(getString(R.string.petcare4), R.drawable.pet4));
        dataList.add(new DrawerItem(getString(R.string.petcare5), R.drawable.pet5));
        dataList.add(new DrawerItem(getString(R.string.petcare6), R.drawable.pet6));
        dataList.add(new DrawerItem(getString(R.string.petcare7), R.drawable.pet7));  
        dataList.add(new DrawerItem(getString(R.string.petcare8), R.drawable.pet8));
        dataList.add(new DrawerItem(getString(R.string.petcare9), R.drawable.pet9));
        dataList.add(new DrawerItem(getString(R.string.petcare10),R.drawable.pet10));
        
        CustomDrawerAdapter  mDrawerAdapter = new CustomDrawerAdapter(getActivity(), R.layout.navdrawer_costum, dataList);
        listforTips.setAdapter(mDrawerAdapter);
        return listforTips;
       
	   
	}	
	
	
}
