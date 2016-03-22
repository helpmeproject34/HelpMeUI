package com.helpme.services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.helpme.helpmeui.Class_alreadyLogin;
import com.helpme.settings.Class_db_values_settings;

/**
 * Created by HARINATHKANCHU on 22-03-2016.
 */
public class Start_service_from_here {
    public static void start(Context context)
    {
        if(!isMyServiceRunning(Class_applocationservice.class,context))
        {
            if(Class_alreadyLogin.login_or_not(context)&&!Class_db_values_settings.is_dnd(context))
            {
                Intent i=new Intent(context,Class_applocationservice.class);
                Bundle bundle=new Bundle();
                int accuracy=Class_db_values_settings.give_accuracy(context);
                if(accuracy==1)
                {
                    bundle.putLong("min_distance",10);
                    bundle.putLong("min_time",10000);
                }
                else if(accuracy==2)
                {
                    bundle.putLong("min_distance",5);
                    bundle.putLong("min_time",5000);
                }
                else if(accuracy==3)
                {
                    bundle.putLong("min_distance",1);
                    bundle.putLong("min_time",1000);
                }
                else
                {
                    bundle.putLong("min_distance",3);
                    bundle.putLong("min_time",5000);
                }
                Log.e("service","service is now going to start definitely.....");
                context.startService(i);
            }
        }
    }
    private static  boolean isMyServiceRunning(Class<?> serviceClass,Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
