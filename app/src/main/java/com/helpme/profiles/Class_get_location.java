package com.helpme.profiles;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by HARINATHKANCHU on 26-03-2016.
 */
public class Class_get_location {
  //  public static String city;
   // public static String postal_code;
   public static  LatLng get(final Context context)
    {
       // city=null;

        LatLng loc=null;
       int count=10;
        LocationManager manager=(LocationManager)context.getSystemService(context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Location location_2=null;
            while(location_2==null&&count!=0)
            {
                List<String> providers = manager.getProviders(true);
                Location bestLocation = null;
                for (String provider : providers)
                {
                    location_2 = manager.getLastKnownLocation(provider);
                    if (location_2 == null) {
                        continue;
                    }
                    if (bestLocation == null || location_2.getAccuracy() < bestLocation.getAccuracy()) {
                        // Found best last known location: %s", l);
                        bestLocation = location_2;
                    }
                }
                location_2=bestLocation;

                count--;
            }
            /*if(location_2!=null)
            {
                try
                {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location_2.getLatitude(), location_2.getLongitude(), 1);
                   // city = addresses.get(0).getAddressLine(0)+addresses.get(0).getAddressLine(1)+addresses.get(0).getAddressLine(2);
                    city=addresses.get(0).getSubLocality();
                    postal_code=addresses.get(0).getPostalCode();

                }
                catch(Exception e)
                {
                    city=null;
                    postal_code=null;
                }

            }
            else
            {
                city=null;
                postal_code=null;
            }*/
            if(location_2!=null)
            {
                loc=new LatLng(location_2.getLatitude(),location_2.getLongitude());
            }

        }
        else
        {

        }
        return loc;
    }
   /* public static Location get_location(final Context context)
    {

        int count=10;
        LocationManager manager=(LocationManager)context.getSystemService(context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Location location_2 = null;
            while (location_2 == null && count != 0) {
                List<String> providers = manager.getProviders(true);
                Location bestLocation = null;
                for (String provider : providers) {
                    location_2 = manager.getLastKnownLocation(provider);
                    if (location_2 == null) {
                        continue;
                    }
                    if (bestLocation == null || location_2.getAccuracy() < bestLocation.getAccuracy()) {
                        // Found best last known location: %s", l);
                        bestLocation = location_2;
                    }
                }
                location_2 = bestLocation;

                count--;
            }

           return location_2;
        }
        return null;

    }*/
}
