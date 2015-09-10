package com.petcare.teamiki;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.petcare.teamiki.R;

public class CustumAdapterTips extends ArrayAdapter {

    Context context;
    List drawerItemList;
    int layoutResID;
    ShowTipInterface callBack;
    
    public CustumAdapterTips(Context context, int layoutResourceID,
                               List listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;

    }
    
    
    public interface ShowTipInterface{
   	 public void startActivityForShowingTip(String post_id);
       
    }
    
    public void setCallback(ShowTipInterface callback){

        this.callBack = callback;
    }
    
    
    
    

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            drawerHolder.ItemName = (TextView) view
                    .findViewById(R.id.navbartv);
            drawerHolder.icon = (ImageView) view.findViewById(R.id.img);

            view.setTag(drawerHolder);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();

        }

        final DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);

        drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(
                dItem.getImgResID()));
        drawerHolder.ItemName.setText(dItem.getItemName());

        view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				
				
				callBack.startActivityForShowingTip(dItem.getItemName());
				
				
			}
		});
        
        return view;
    }

    private static class DrawerItemHolder {
        TextView ItemName;
        ImageView icon;
    }

    

}

