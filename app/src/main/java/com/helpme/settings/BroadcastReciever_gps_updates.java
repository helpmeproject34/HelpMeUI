package com.helpme.settings;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BroadcastReciever_gps_updates extends BroadcastReceiver {

	public Activity_settings activity_reciever;
	@Override
	public void onReceive(Context context, Intent arg1) {
		//Toast.makeText(context,"broadcast recieved",Toast.LENGTH_SHORT).show();
			if(activity_reciever!=null)
			{
				activity_reciever.update_gps();
				activity_reciever.update_internet_connection();
			}

		
	}
	public void addActivity(Activity_settings activity_)
	{
		this.activity_reciever=activity_;
	}

}
