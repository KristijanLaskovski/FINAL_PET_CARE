package com.example.patcareteam2;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustumAdapterForComments extends ArrayAdapter<CommentItem> {
	
    public CustumAdapterForComments(Context context, ArrayList<CommentItem> comments) {
        super(context,R.layout.custum_view, comments);
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
       
        // Check if an existing view is being reused, otherwise inflate the view
       
       LayoutInflater buckyinfluate=LayoutInflater.from(getContext());
		View custumView=buckyinfluate.inflate(R.layout.custum_view, parent,false);
       
		 CommentItem comment = getItem(position);   
        // Lookup view for data population
       TextView tvName = (TextView) custumView.findViewById(R.id.tvName);
       TextView tvLastName= (TextView) custumView.findViewById(R.id.etLastName);
       TextView tvTime = (TextView) custumView.findViewById(R.id.TVtime);
       TextView tvComment = (TextView) custumView.findViewById(R.id.TVComment);
        // Populate the data into the template view using the data object
       tvName.setText(comment.getFirstname());
       tvLastName.setText(comment.getLastname());
       tvTime.setText(comment.getTime_c());
       tvComment.setText(comment.getMessage());
       
       String image_p=comment.getImage_p();
       byte[] decodedByte = Base64.decode(image_p, Base64.DEFAULT);                     
       Bitmap b = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
       ImageView profile=(ImageView)custumView.findViewById(R.id.ivkikolaskovski);
       profile.setImageBitmap(b);
       
       
       String image_c=comment.getImage_c();
       byte[] decodedByte_c = Base64.decode(image_c, Base64.DEFAULT);                     
       Bitmap b_c = BitmapFactory.decodeByteArray(decodedByte_c, 0, decodedByte_c.length);
       ImageView img_comment=(ImageView)custumView.findViewById(R.id.commentimage);
       img_comment.setImageBitmap(b_c);
       
       
        // Return the completed view to render on screen*/
        return custumView;
    }
 }
