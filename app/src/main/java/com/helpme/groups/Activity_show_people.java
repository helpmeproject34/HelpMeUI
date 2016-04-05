package com.helpme.groups;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.helpme.alert_dialogs.Alert_ok;
import com.helpme.helpmeui.R;
import com.helpme.json.Response;

import java.util.ArrayList;

public class Activity_show_people extends ActionBarActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Handler handler;

    String var_username;
    String var_phone;
    String var_group_id;
    String var_group_name;
    String var_admin_username;
    String var_admin_phone;
    Thread updating_thread;
    int imp_value;
    boolean activity_visible;
    Alert_ok alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        Intent intent_recieved= getIntent();
        Bundle bundle=null ;
        if(intent_recieved!=null)
        {
            bundle=intent_recieved.getExtras();
        }

        if(bundle==null || intent_recieved==null)
        {
            Toast.makeText(getApplicationContext(), "Illeagal opening of activity", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            setContentView(R.layout.activity_show_people);
            imp_value=0;
            activity_visible=true;
            var_username=bundle.getString("username");
            var_phone=bundle.getString("phone");
            var_group_id=bundle.getString("group_id");
            var_group_name=bundle.getString("group_name");
            var_admin_username=bundle.getString("group_admin_username");
            var_admin_phone=bundle.getString("group_admin_phone");

            handler=new Handler();
           start_refresh_thread(10000);
          //

        }
        getSupportActionBar().setTitle(var_group_name);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        if(services_ok())
        {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Your device don't have google play services.Please install",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {

        super.onStart();
    }


    private boolean services_ok()
    {

        boolean result=false;
        int isavail= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
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
            Alert_ok.show(this, "Google play services are not availabe");
        }
        return result;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    private void start_refresh_thread(final int time)
    {
        Toast.makeText(Activity_show_people.this,"refresh thread is started",Toast.LENGTH_SHORT).show();
        if(updating_thread!=null)
        {
            if(updating_thread.isAlive())
            {
                return;
            }
        }

        updating_thread=new Thread(new Runnable() {

            @Override
            public void run() {


                ArrayList<Class_locations> locations=new ArrayList<Class_locations>();
                String groupid=var_group_id;
                String username=var_username;

                while(true)
                {
                    locations.clear();
                    if(!Activity_show_people.this.isFinishing()&&mMap!=null)
                    {
                        final Response result= Class_locations_provider.provide_locations(locations, groupid, username);
                        final  ArrayList<Class_locations> all_locations=new ArrayList<>();
                        int len=locations.size();
                        for(int i=0;i<len;i++)
                        {
                            all_locations.add(locations.get(i));
                        }
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                //////////////////////////////////////////
                                String unlocated_people="Locations are unavailable for :";
                                int length=all_locations.size();
                                LatLng loc=new LatLng(-1,-1);
                                mMap.clear();
                                for(int i=0;i<length;i++)
                                {
                                    if(all_locations.get(i).latitude==-1&&all_locations.get(i).longitude==-1)
                                    {
                                        unlocated_people=unlocated_people+"\n"+all_locations.get(i).username;
                                        continue;
                                    }
                                    loc = new LatLng(all_locations.get(i).latitude,all_locations.get(i).longitude);
                                    mMap.addMarker(new MarkerOptions().position(loc).snippet(all_locations.get(i).last_updated).title(all_locations.get(i).username));
                                }
                                if(alert==null)
                                {
                                    alert=new Alert_ok();
                                    Alert_ok.show(Activity_show_people.this,unlocated_people);
                                }
                               // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 18.0f));
                                Toast.makeText(getApplicationContext(), "people's locations are updated now", Toast.LENGTH_SHORT).show();
                                if(result.value==-2)
                                {
                                    imp_value=-2;
                                    finish_all();
                                }
                            }
                        });

                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {

                        }
                    }
                    else
                    {

                    }


                }

            }

        });
        updating_thread.start();
    }
    private void finish_all()
    {
        if(activity_visible)
        {
            //finish();
            Alert_ok alert=new Alert_ok()
            {
                @Override
                public void onfalse() {

                    super.onfalse();
                    finish();
                }
                @Override
                public void ontrue() {
                    // TODO Auto-generated method stub
                    super.ontrue();
                    finish();
                }
            };
            alert.ok_or_cancel(Activity_show_people.this, "","This group no longer exist");
        }

    }
}
