package com.helpme.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.helpme.alert_dialogs.Alert_ok;
import com.helpme.helpmeui.R;
import com.helpme.json.Response;
import com.helpme.profiles.Class_profile_object;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Activity_show_helpers_on_map extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    Bundle bundle;
    String var_username;
    String var_phone;
    Context context;
    boolean destroyed;
    Handler handler;
    private GoogleMap mMap=null;
    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
    private Double var_my_latitude;
    private Double var_my_longitude;
    private String postal_code;
    private int category;
    HashMap<String,Class_profile_object> hash_map=new HashMap<>();
    Thread t;
    ArrayList<Class_profile_object> helper_objects=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(services_ok())
        {
            setContentView(R.layout.fragment_show_helpers);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_show_helpers);
            mapFragment.getMapAsync(this);
        }
        else
        {
            finish();

        }

        Intent var_intent_received=getIntent();
        if(var_intent_received==null||var_intent_received.getExtras()==null)
        {
            if(var_intent_received==null)
            {
                Toast.makeText(getApplicationContext(), "intent is null", Toast.LENGTH_SHORT).show();

            }
            if(var_intent_received.getExtras()==null)
            {
                Toast.makeText(getApplicationContext(), "bundle is null", Toast.LENGTH_SHORT).show();

            }
            finish();
        }
        else
        {
            bundle=var_intent_received.getExtras();
            if(bundle==null)
            {
                finish();
            }
            try
            {
                var_my_latitude=bundle.getDouble("latitude");
                var_my_longitude=bundle.getDouble("longitude");
                postal_code=bundle.getString("postal_code");
                category=bundle.getInt("category");
            }
            catch(Exception e)
            {
                finish();
            }
            context=this;
            handler=new Handler();
        }


    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        destroyed=true;


    }
    @Override
    protected void onStop() {
        super.onStop();
        destroyed=true;

    }
    @Override
    protected void onStart() {

        super.onStart();
        destroyed=false;
        get_helpers();
    }
    private void get_helpers()
    {

        if(t!=null)
        {
            if(t.isAlive())
            {
                return ;
            }
        }

        t=new Thread(new Runnable() {
            @Override
            public void run() {
                final Response result=Class_get_helpers.get(helper_objects,postal_code,category);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),result.message,Toast.LENGTH_SHORT).show();
                        mMap.clear();
                        for(int i=0;i<helper_objects.size();i++)
                        {
                            Marker m = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(helper_objects.get(i).latitude, helper_objects.get(i).longitude)));

                            hash_map.put(m.getId(),helper_objects.get(i));

                        }

                    }
                });
            }
        });
        t.start();
    }

    private boolean services_ok()
    {
        return true;
      /*  boolean result=false;
        int isavail= GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if(isavail== ConnectionResult.SUCCESS)
        {
            result=true;
        }
        else if(GooglePlayServicesUtil.isUserRecoverableError(isavail))
        {
            Dialog dialog= GooglePlayServicesUtil.getErrorDialog(isavail, this, 9001);
            dialog.show();
        }
        else
        {
            Alert_ok.show(getApplicationContext(), "Google play services are not availabe");
        }
        return result;*/
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "maps ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Class_profile_object object=hash_map.get(marker.getId());
        String text="Name: "+object.working_name+"\n";
        text=text+"Phone Num: "+object.working_phone+"\n";
        text=text+"Available from: "+object.from+" AM\n";
        text=text+"Available till: "+object.to+"PM\n";
        Alert_ok.show(this,text);
        return true;
    }
}
