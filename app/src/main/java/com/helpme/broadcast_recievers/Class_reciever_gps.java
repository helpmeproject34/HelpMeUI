package com.helpme.broadcast_recievers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.helpme.services.Start_service_from_here;
import com.helpme.services.Stop_service_from_here;

public class Class_reciever_gps extends BroadcastReceiver {

	 //public  static  int GPS;
	Context con;
	@Override
	public void onReceive(final Context context, Intent intent) {
		
		con=context;
		if(intent.getAction().matches("android.location.PROVIDERS_CHANGED"))
		{
			
			LocationManager manager=(LocationManager)context.getSystemService(context.LOCATION_SERVICE);
			if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
	           // GPS=1;
				Start_service_from_here.start(context);
			}
			else
			{
				Stop_service_from_here.stop(context);
		    	//GPS=0;
		    }
		
		
		}
	
	}
}