package com.petcare.teamiki;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NetworkChangeReceiver extends  BroadcastReceiver  {
    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil.getConnectivityStatusString(context);
        Log.e("Sulod sa network reciever", "Sulod sa network reciever");
        if (!"android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            if(status==NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){
         //      Toast.makeText(context, "Network disabled.", Toast.LENGTH_LONG).show();
            	 MainActivity.connected = false;
                
            }else{
            	// Toast.makeText(context, "Network disabled.", Toast.LENGTH_LONG).show();
            	MainActivity.connected = true;
            }

       }
    }
}
