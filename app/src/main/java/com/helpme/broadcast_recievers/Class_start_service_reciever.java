package com.helpme.broadcast_recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.helpme.services.Start_service_from_here;

/**
 * Created by HARINATHKANCHU on 23-03-2016.
 */
public class Class_start_service_reciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Start_service_from_here.start(context);
    }
}
