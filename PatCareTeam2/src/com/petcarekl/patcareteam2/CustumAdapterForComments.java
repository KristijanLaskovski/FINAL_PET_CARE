package com.petcarekl.patcareteam2;

import java.util.ArrayList;

import com.petcarekl.patcareteam2.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustumAdapterForComments extends ArrayAdapter<CommentItem> {

	ArrayList<CommentItem> comments;
	Context mContext;
	ReplayOnCommentInterface callBack;
	
	   public interface ReplayOnCommentInterface{
	    	 public void startActivityForReplayComent(String post_id);
	         public void startActivityForShowingLocation(String langitude, String latitude);
	         public void startActivityForCallingPerson(String phone);
	         public void startActivityVisitUser(String username);
	     }
	     
	     public void setCallback(ReplayOnCommentInterface callback){

	         this.callBack = callback;
	     }
	
	
    public CustumAdapterForComments(Context context, ArrayList<CommentItem> comments) {
        super(context,R.layout.custum_view, comments);
        this.comments=comments;
        this.mContext=context;
     }

    
    @Override
    public boolean areAllItemsEnabled() 
    {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0) 
    {
        return true;
    }
    
    
    
     @Override
     public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
    	 ViewHolderItem viewHolder;
        // Check if an existing view is being reused, otherwise inflate the view
    	   
    	    if(convertView==null){
    	    
    	    	LayoutInflater inflater = LayoutInflater.from(getContext());
    	        convertView = inflater.inflate(R.layout.custum_view, parent, false);
    	         
    	        // well set up the ViewHolder
    	        viewHolder = new ViewHolderItem();
    	        viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
    	        viewHolder.tvLastName = (TextView) convertView.findViewById(R.id.etLastName);
    	        viewHolder.tvTime = (TextView) convertView.findViewById(R.id.TVtime);
    	        viewHolder.tvComment = (TextView) convertView.findViewById(R.id.TVComment);
    	        viewHolder.img_profile=(ImageView)convertView.findViewById(R.id.replayImage);
    	        viewHolder.img_comment=(ImageView)convertView.findViewById(R.id.commentimage);
    	        viewHolder.btnShowLocation=(Button)convertView.findViewById(R.id.showmapbtn);
    	        viewHolder.btnReplayComment=(Button)convertView.findViewById(R.id.btnreplayComment);
    	        viewHolder.btnCallContact=(Button)convertView.findViewById(R.id.typecomentBTN);
    	        
    	        convertView.setTag(viewHolder);
    	    }else{
    	    	viewHolder=(ViewHolderItem)convertView.getTag();
    	    }
    	 
    	    final CommentItem comment = getItem(position); 
    	    
    	       String image_p=comment.getImage_p();
    	       String image_c=comment.getImage_c();

    	    if(comment != null) {
    	        // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
    	        viewHolder.tvName.setText(comment.getFirstname());
    	        viewHolder.tvLastName.setText(comment.getLastname());
    	        viewHolder.tvTime.setText(comment.getTime_c());
    	        viewHolder.tvComment.setText(comment.getMessage());
    	        Picasso.with(mContext).load(image_c).into(viewHolder.img_comment);
    	        Picasso.with(mContext).load(image_p).into(viewHolder.img_profile);

    	        viewHolder.btnShowLocation.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//KIKO_KOMENTS ___________________ZA__DIDI________________________________________________________________________________________________________________
						callBack.startActivityForShowingLocation(comment.getLongitude(),comment.getLangitude() );	
//KIKO_KOMENTS _____________________________________________________________________________________________________________________________________
											
					}
				}) ;
    	        viewHolder.btnReplayComment.setOnClickListener(new OnClickListener() {
    	  					@Override
    	  					public void onClick(View v) {
    	  						callBack.startActivityForReplayComent(comment.getPost_id());
    	  					}
    	  				}) ;
    	        
    	        viewHolder.btnCallContact.setOnClickListener(new OnClickListener() {
  					@Override
  					public void onClick(View v) {
  						callBack.startActivityForCallingPerson(comment.getContact());
  					}
  				}) ;
    	        
    	        
    	        viewHolder.img_profile.setOnClickListener(new OnClickListener() {
  					@Override
  					public void onClick(View v) {
  						callBack.startActivityVisitUser(comment.getUser());
  					}
  				}) ;
    	        
    	        
    	    }
    	
//KIKO_KOMENTS _________________NEZNAM_DALI_TUKA _AKO_SAKAME_DA_PRAVIME_DA_SE_PRIKAZI_AKTIVITITO_KAKO_NA_FB__________________________________________________________________________________________________________________		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			View g=	(View) v.getParent();
			TextView name=(TextView)g.findViewById(R.id.tvName);
			Toast.makeText(getContext(),name.getText().toString(), Toast.LENGTH_SHORT).show();
			
			}
		});
		
	 	  //KIKO_KOMENTS _________________NEZNAM_DALI_TUKA _AKO_SAKAME_DA_PRAVIME_DA_SE_PRIKAZI_AKTIVITITO_KAKO_NA_FB__________________________________________________________________________________________________________________
		
	
		
       
		  

        // Return the completed view to render on screen*/
        return  convertView;
    }
     
     
     
     static class ViewHolderItem {
    	 ImageView img_comment;
    	 ImageView img_profile;
    	 TextView tvName ;
         TextView tvLastName;
         TextView tvTime;
         TextView tvComment;
         Button btnShowLocation;
         Button btnCallContact;
         Button btnReplayComment;
     }
     
     
  
     
 
     
 }
