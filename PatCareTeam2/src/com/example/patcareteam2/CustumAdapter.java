package com.example.patcareteam2;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CustumAdapter  extends ArrayAdapter<Comment>{

	public CustumAdapter(Context context, List<Comment>  lista) {
		super(context,R.layout.custum_view, lista);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		LayoutInflater linflater=LayoutInflater.from(getContext());
		View custumView=linflater.inflate(R.layout.custum_view,parent,false);
		Comment comment=getItem(position);
		TextView name=(TextView)custumView.findViewById(R.id.tvName);
		TextView lastname=(TextView)custumView.findViewById(R.id.TVLastName);
		TextView time=(TextView)custumView.findViewById(R.id.TVtime);
		TextView commentar=(TextView)custumView.findViewById(R.id.TVComment);
		
		ImageView profilePhoto=(ImageView)custumView.findViewById(R.id.imageView1);
		profilePhoto.setImageResource(R.drawable.kikko);
		
		 name.setText(comment.getFirstName());
		 lastname.setText(comment.getLastName());
		 time.setText(comment.getTime());
		 commentar.setText(comment.getComment());
		 
		
		
		return custumView;
	
	}

	
	
	
}