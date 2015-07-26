package com.example.patcareteam2;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
 
public class HomeFragment extends  android.support.v4.app.Fragment {
     
	
	
	
    public HomeFragment(){}
     
    ArrayAdapter<Comment> arrAdapterForComents;
    List<Comment> listForComments;
    ListView listViewOnHome;
    Button btnAddAcommnet;
    
    


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        
        listForComments=new ArrayList<Comment>();
		arrAdapterForComents=new CustumAdapter(getActivity().getApplicationContext(), listForComments);	
		listViewOnHome=(ListView)rootView .findViewById(R.id.listView1);
		listViewOnHome.setAdapter(arrAdapterForComents);
        
		btnAddAcommnet=(Button)rootView.findViewById(R.id.addComentBTN);
  
		btnAddAcommnet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i=new Intent(getActivity(),CommentActivity.class);
				startActivityForResult(i,0);
			}
		});
		
		
        return rootView;
    }
	


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		   if(resultCode==getActivity().RESULT_OK)  
	        {  
	           Bundle backpack=data.getExtras();
	           String s=backpack.getString("answer");
	           
	           SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	           String currentDateandTime = sdf.format(new Date());
	           
	           Comment k =new   Comment("Kristijan", "Laskovsi", s,  currentDateandTime);
	           listForComments.add(k);
	           arrAdapterForComents.notifyDataSetChanged();
	        }  
		
		
	}
	
	
	
	
	
	
}