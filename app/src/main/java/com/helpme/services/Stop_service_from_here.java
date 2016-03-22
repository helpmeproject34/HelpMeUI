package com.helpme.services;

import android.content.Context;
import android.content.Intent;

/**
 * Created by HARINATHKANCHU on 22-03-2016.
 */
public class Stop_service_from_here {
    public static  void stop(Context context)
    {
        Intent i=new Intent(context,Class_applocationservice.class);
        context.stopService(i);
    }
}
