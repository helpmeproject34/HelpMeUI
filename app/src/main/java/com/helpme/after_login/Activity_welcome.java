package com.helpme.after_login;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.helpme.alert_dialogs.Alert_ok;
import com.helpme.databases.Db_functions;
import com.helpme.friends.Activity_showfriends;
import com.helpme.groups.Activity_show_groups;
import com.helpme.helper.Activity_helper;
import com.helpme.helpmeui.Activity_login;
import com.helpme.helpmeui.Class_alreadyLogin;
import com.helpme.helpmeui.R;
import com.helpme.profiles.Activity_edit_profiles;
import com.helpme.services.Class_applocationservice;
import com.helpme.settings.Activity_settings;

/**
 * Created by HARINATHKANCHU on 07-03-2016.
 */

public class Activity_welcome extends Activity {
    com.helpme.widgets.SAutoBgButton track_friend;
    com.helpme.widgets.SAutoBgButton track_group;
    com.helpme.widgets.SAutoBgButton track_helper;
    com.helpme.widgets.SAutoBgButton track_contact;
    Switch var_switch_internet;
    Switch var_switch_location;
    Switch var_switch_dnd;
    String var_username;
    String var_phone;
   // RelativeLayout var_relative_layout;
    //Adapter_welcome adapter_welcome;
   //TextView var_welcome_textview;
   // ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
       // setContentView(R.layout.card_test);


       check_for_intent();
        load_all_objects();
        register_for_clicks();
        //register_for_gps_updates();
        //register_for_internet_updates();
        update_gps();
        update_internet_connection();
        load_banner_images();
        //databasework();
        setTitle("Welcome " + var_username);
        Class_alreadyLogin.islogin=true;
        Class_alreadyLogin.username=var_username;
        Class_alreadyLogin.phone=var_phone;
        getActionBar().setDisplayShowHomeEnabled(false);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        if(item.getItemId()==R.id.action_logout)
        {

            Alert_ok alert=new Alert_ok()
            {
                @Override
                public void ontrue() {
                    LocationManager manager=(LocationManager)Activity_welcome.this.getSystemService(Activity_welcome.this.LOCATION_SERVICE);
                    if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    {
                        if(isMyServiceRunning(Class_applocationservice.class))
                        {
                            Intent i=new Intent(getApplicationContext(),Class_applocationservice.class);
                            getApplicationContext().stopService(i);
                        }

                    }
                    Class_alreadyLogin.islogin=false;
                    Db_functions funcs=new Db_functions(Activity_welcome.this.getApplicationContext());
                    Db_functions.delete_table_prev_login();
                    funcs.close_all();
                    Intent i=new Intent(getApplicationContext(),Activity_login.class);
                    startActivity(i);
                    finish();
                }
            };
            alert.ok_or_cancel(this,"","Are you sure for LOGOUT?","Cancel","Logout");

        }
        else if(item.getItemId()==R.id.action_settings)
        {

            Intent i=new Intent(getApplicationContext(),Activity_settings.class);
            Bundle bundle=new Bundle();
            bundle.putString("username",var_username);
            bundle.putString("phone", var_phone);
            i.putExtras(bundle);
            startActivity(i);
        }
        else if(item.getItemId()==R.id.action_edit_profile)
        {
            Intent i=new Intent(getApplicationContext(),Activity_edit_profiles.class);
            Bundle bundle=new Bundle();
            bundle.putString("username",var_username);
            bundle.putString("phone", var_phone);
            i.putExtras(bundle);
            startActivity(i);
        }
        else if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onMenuItemSelected(featureId, item);
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(getApplicationContext().ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void check_for_intent()
    {
        Intent intent=getIntent();
        if(intent==null)
        {
            Intent i=new Intent(getApplicationContext(),Activity_login.class);
            startActivity(i);
            finish();

        }
        else
        {
            Bundle bundle=intent.getExtras();
            var_username=bundle.getString("username");
            var_phone=bundle.getString("phone");
        }
    }
    private  void load_all_objects()
    {
        track_friend=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.imageview_layout_welcome1);
        track_group=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.imageview_layout_welcome2);
        track_helper=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.imageview_layout_welcome3);
        track_contact=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.imageview_layout_welcome4);
        //var_switch_dnd=(Switch)findViewById(R.id.switch_welcome_dnd);
        //var_switch_internet=(Switch)findViewById(R.id.switch_welcome_internet);
       // var_switch_location=(Switch)findViewById(R.id.switch_welcome_location);
       // var_relative_layout=(RelativeLayout)findViewById(R.id.relative_layout_welcome);
       // var_welcome_textview=(TextView)findViewById(R.id.textview_activity_welcome_welcome_text);
       // var_welcome_textview.setText("Welcome "+var_username);
        //adapter_welcome=new Adapter_welcome();
       // track_group=(ImageView)findViewById(R.id.imageview_welcome_groups);
       // listview=(ListView)findViewById(R.id.listview_activity_welcome);
    }
    private  void register_for_clicks()
    {

        track_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Alert_ok.show(Activity_welcome.this,"This is a pop up to show some message.Now tracking a friend");

                Intent i=new Intent(Activity_welcome.this, Activity_showfriends.class);

                Bundle bundle=new Bundle();
                bundle.putString("username",var_username);
                bundle.putString("phone", var_phone);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        track_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Activity_welcome.this, Activity_show_groups.class);
                Bundle bundle=new Bundle();
                bundle.putString("username",var_username);
                bundle.putString("phone", var_phone);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        track_helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Activity_welcome.this, Activity_helper.class);
                Bundle bundle=new Bundle();
                bundle.putString("username",var_username);
                bundle.putString("phone", var_phone);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        track_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Activity_welcome.this, Activity_contact_us.class);
                Bundle bundle=new Bundle();
                bundle.putString("username",var_username);
                bundle.putString("phone", var_phone);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        /*var_switch_dnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        var_switch_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(var_switch_internet.isChecked())
                {
                    var_switch_internet.setChecked(false);
                }
                else
                {
                    var_switch_internet.setChecked(true);
                }
                Intent intent=new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        var_switch_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (var_switch_location.isChecked()) {
                    var_switch_location.setChecked(false);
                } else {
                    var_switch_location.setChecked(true);
                }
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });*/
        /*var_relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ViewGroup.LayoutParams params=var_relative_layout.getLayoutParams();
                //params.height=0;
                //var_relative_layout.setLayoutParams(params);
            }
        });*/
    }
   /* private void register_for_gps_updates()
    {
        BroadcastReciever_gps_updates broadcast_reciever=new BroadcastReciever_gps_updates();
        this.registerReceiver(broadcast_reciever, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        broadcast_reciever.addActivity(this);
    }
    private void register_for_internet_updates()
    {
        BroadcastReciever_gps_updates broadcast_reciever=new BroadcastReciever_gps_updates();
        this.registerReceiver(broadcast_reciever, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        broadcast_reciever.addActivity(this);
    }*/
    void update_gps()
    {
       /* LocationManager manager=(LocationManager)this.getSystemService(this.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            var_switch_location.setChecked(true);
        }
        else
        {
            var_switch_location.setChecked(false);
        }*/
    }
    void update_internet_connection()
    {
       /* ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo!=null)
        {
            var_switch_internet.setChecked(true);
        }
        else
        {
            var_switch_internet.setChecked(false);
        }*/
    }
    private void load_banner_images()
    {
        //adapter_welcome.list.add(new Class_banner_images("Track a friend",R.drawable.location_banner));
       // adapter_welcome.list.add(new Class_banner_images("Track a friend",R.drawable.location_banner));
       // adapter_welcome.list.add(new Class_banner_images("Track a friend",R.drawable.location_banner));
       // adapter_welcome.list.add(new Class_banner_images("Track a friend",R.drawable.location_banner));
       // listview.setAdapter(adapter_welcome);
    }

}
