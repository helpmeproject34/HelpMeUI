package com.helpme.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.helpme.alert_dialogs.Alert_ok;
import com.helpme.helpmeui.R;
import com.helpme.profiles.Class_get_location;

public class Activity_helper extends Activity {

    int NUM_HELPERS=8;
    Handler handler;
    ProgressDialog dialog;
    Double latitude;
    Double longitude;
    Thread t;
    String postal_code;
    com.helpme.widgets.SAutoBgButton helper[]=new com.helpme.widgets.SAutoBgButton[NUM_HELPERS+1];
   /* com.helpme.widgets.SAutoBgButton helper_1;
    com.helpme.widgets.SAutoBgButton helper_2;
    com.helpme.widgets.SAutoBgButton helper_3;
    com.helpme.widgets.SAutoBgButton helper_4;
    com.helpme.widgets.SAutoBgButton helper_5;
    com.helpme.widgets.SAutoBgButton helper_6;
    com.helpme.widgets.SAutoBgButton helper_7;
    com.helpme.widgets.SAutoBgButton helper_8;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        load_all_items();
        register_for_clicks();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void load_all_items()
    {
        handler=new Handler();
        helper[1]=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_activity_helper_1);
        helper[2]=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_activity_helper_2);
        helper[3]=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_activity_helper_3);
        helper[4]=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_activity_helper_4);
        helper[5]=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_activity_helper_5);
        helper[6]=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_activity_helper_6);
        helper[7]=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_activity_helper_7);
        helper[8]=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_activity_helper_8);
    }
    private void register_for_clicks() {
       for(int i=1;i<=NUM_HELPERS;i++)
       {
           final int j=i;
           helper[i].setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   call_map(j);
               }
           });
       }
    }
    private void call_map(int val)
    {


        if(latitude==null||longitude==null)
        {
            get_location( val);
            return;
        }
        if(dialog!=null)
        {
            if(dialog.isShowing())
            {
                return ;
            }
        }
        if(t!=null)
        {
            if(t.isAlive())
            {

                return ;
            }
        }
        Intent i=new Intent(this,Activity_show_helpers_on_map.class);
        Bundle bundle=new Bundle();
        bundle.putDouble("latitude",latitude);
        bundle.putDouble("longitude",longitude);
        bundle.putString("postal_code", Class_get_location.postal_code);
        bundle.putInt("category", val);
        i.putExtras(bundle);
        startActivity(i);
    }
    private boolean gps_check()
    {
        LocationManager manager=(LocationManager)this.getSystemService(this.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            return true;
        }
        return false;
    }
    private void get_location(final int val)
    {
        if(!gps_check())
        {
            Alert_ok alert=new Alert_ok()
            {
                @Override
                public void ontrue()
                {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
                @Override
                public void onfalse()
                {

                }
            };
            alert.ok_or_cancel(this,"","Your GPS is off.Please turn it on","CANCEL","TURN ON");
            return ;
        }
        if(dialog!=null)
        {
            if(dialog.isShowing())
            {
                return ;
            }
        }
        if(t!=null)
        {
            if(t.isAlive())
            {
                dialog=new ProgressDialog(Activity_helper.this,R.style.DialogTheme);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("please wait.....");
                dialog.show();
                return ;
            }
        }
        dialog=new ProgressDialog(Activity_helper.this,R.style.DialogTheme);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("detecting your location.....");
        dialog.show();
        t=new Thread(new Runnable() {
            @Override
            public void run() {
                final LatLng loc= Class_get_location.get(Activity_helper.this);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(dialog!=null)
                        {
                            dialog.dismiss();
                        }
                        postal_code=Class_get_location.postal_code;
                        if(postal_code==null)
                        {
                            Alert_ok.show(Activity_helper.this,"Your internet connection is also required to get address");
                            latitude=null;
                            longitude=null;
                        }
                        else
                        {
                            if(loc!=null)
                            {
                                latitude=loc.latitude;
                                longitude=loc.longitude;
                                Toast.makeText(getApplicationContext(), "Location Detected", Toast.LENGTH_SHORT).show();
                                call_map(val);

                            }
                            else
                            {
                                latitude=null;
                                longitude=null;
                                Toast.makeText(getApplicationContext(),"Failed to get location.Try again",Toast.LENGTH_SHORT).show();
                            }
                        }



                    }
                });

            }
        });
        t.start();
    }


}
