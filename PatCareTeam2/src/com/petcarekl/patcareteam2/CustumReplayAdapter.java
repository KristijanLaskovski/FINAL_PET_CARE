package com.petcarekl.patcareteam2;

import java.util.ArrayList;

import com.petcare.teamiki.R;
import com.petcarekl.patcareteam2.CustumAdapterForComments.ViewHolderItem;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustumReplayAdapter extends ArrayAdapter<ReplayedItem> {
	Context mContext;
	
	  public CustumReplayAdapter(Context context, ArrayList<ReplayedItem> comments) {
	        super(context,R.layout.custum_replay_comment, comments);
	       this.mContext=context;
	     }
	
	
	     @Override
	     public View getView(final int position, View convertView, ViewGroup parent) {
	        // Get the data item for this position
	    	 ViewHolderItem viewHolder;
	        // Check if an existing view is being reused, otherwise inflate the view
	    	   
	    	    if(convertView==null){
	    	    
	    	    	LayoutInflater inflater = LayoutInflater.from(getContext());
	    	        convertView = inflater.inflate(R.layout.custum_replay_comment, parent, false);
	    	         
	    	        // well set up the ViewHolder
	    	        viewHolder = new ViewHolderItem();
	    	        viewHolder.tvName = (TextView) convertView.findViewById(R.id.replayName);
	    	        viewHolder.tvLastName = (TextView) convertView.findViewById(R.id.replayLastName);
	    	        viewHolder.tvTime = (TextView) convertView.findViewById(R.id.replayTime);
	    	        viewHolder.tvComment = (TextView) convertView.findViewById(R.id.replayComment);
	    	        viewHolder.img_profile=(ImageView)convertView.findViewById(R.id.replayImage);
	    	        
	    	        convertView.setTag(viewHolder);
	    	    }else{
	    	    	viewHolder=(ViewHolderItem)convertView.getTag();
	    	    }
	    	 
	    	    final ReplayedItem comment = getItem(position); 
	    	    
	    	       String image_p=comment.getImage();
	    	       //byte[] decodedByte = Base64.decode(image_p, Base64.DEFAULT);                     
	    	      // Bitmap b = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);


	    	    if(comment != null) {
	    	        // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
	    	        viewHolder.tvName.setText(comment.getFirstname());
	    	        viewHolder.tvLastName.setText(comment.getLastname());
	    	        viewHolder.tvTime.setText(comment.getTimePost());
	    	        viewHolder.tvComment.setText(comment.getMessage());
	    	        
	    	        Picasso.with(mContext).load(image_p)
	    	          .into(viewHolder.img_profile);
	    	       // viewHolder.img_profile.setImageBitmap(b);
	    	    }
						
	
	        return  convertView;
	    }
	  
	  
	  
	
	
	static class ViewHolderReplayItem {
		 
		ImageView img_profile;
		TextView tvName ;
	    TextView tvLastName;
	    TextView tvTime;
	    TextView tvComment;

	}

}

