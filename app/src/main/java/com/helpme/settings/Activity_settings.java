package com.helpme.settings;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.helpme.alert_dialogs.Alert_ok;
import com.helpme.databases.Db_functions;
import com.helpme.helpmeui.R;
import com.helpme.services.Start_service_from_here;
import com.helpme.services.Stop_service_from_here;


public class Activity_settings extends ActionBarActivity {


    Switch switch_location;
    Switch switch_internet;
    Switch switch_dnd;
    Switch switch_accuracy_low;
    Switch switch_accuracy_med;
    Switch switch_accuracy_high;
    TextView textview_whocantrack;
    String var_username;
    String var_phone;
    View.OnClickListener  click_listner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        recieve_intent();
        load_all_items();
        register_for_clicks();

    }
    private void recieve_intent()
    {
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        if(i==null||bundle==null)
        {
            finish();
        }
        var_phone=bundle.getString("phone");
        var_username=bundle.getString("username");
    }
    private void load_all_items()
    {
        switch_location=(Switch)findViewById(R.id.switch_activity_settings_location);
        switch_internet=(Switch)findViewById(R.id.switch_activity_settings_internet);
        switch_dnd=(Switch)findViewById(R.id.switch_activity_settings_dnd);
        if(Class_db_values_settings.is_dnd(getApplicationContext()))
        {
            switch_dnd.setChecked(true);
        }
        else
        {
            switch_dnd.setChecked(false);
        }
        switch_accuracy_high=(Switch)findViewById(R.id.switch_activity_settings_accuracy_high);
        switch_accuracy_med=(Switch)findViewById(R.id.switch_activity_settings_accuracy_med);
        switch_accuracy_low=(Switch)findViewById(R.id.switch_activity_settings_accuracy_low);
        textview_whocantrack=(TextView)findViewById(R.id.textview_activity_settings_whocantrack);
        arrange_accuracy_switches();
        update_internet_connection();
        update_gps();
        register_for_internet_updates();
        register_for_gps_updates();

    }
    private void register_for_clicks()
    {
        textview_whocantrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     Intent i=new Intent(Activity_settings.this.getApplicationContext(),Activity_tracklist.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("username",var_username);
                    bundle.putString("phone",var_phone);
                    i.putExtras(bundle);
                    startActivity(i);
                }
        });
        switch_dnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch_dnd.isChecked()) {
                    Alert_ok alert = new Alert_ok() {

                        public void ontrue() {
                            set_dnd();
                        }

                        public void onfalse() {
                            switch_dnd.setChecked(false);
                        }

                    };
                    alert.ok_or_cancel(Activity_settings.this, "", "You cannot be TRACKED if you enable DND. Continue?", "cancel", "Enable DND");

                } else {
                    Alert_ok alert = new Alert_ok() {

                        public void ontrue() {
                            unset_dnd();
                        }

                        public void onfalse() {
                            switch_dnd.setChecked(true);
                        }

                    };
                    alert.ok_or_cancel(Activity_settings.this, "", "You can be TRACKED if you disable DND. Continue?", "cancel", "Disable DND");

                }
            }
        });
        click_listner=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Switch s=(Switch)findViewById(v.getId());
                final int accuracy;
                if(v.getId()==R.id.switch_activity_settings_accuracy_high)
                {
                    accuracy=3;
                }
                else if(v.getId()==R.id.switch_activity_settings_accuracy_med)
                {
                    accuracy=2;
                }
                else
                {
                    accuracy=1;
                }
                if(s.isChecked())
                {
                    Alert_ok alert=new Alert_ok()
                    {
                        Switch s1=(Switch) findViewById(R.id.switch_activity_settings_accuracy_high);
                        Switch s2=(Switch) findViewById(R.id.switch_activity_settings_accuracy_med);
                        Switch s3=(Switch) findViewById(R.id.switch_activity_settings_accuracy_low);

                        @Override
                        public void ontrue() {

                            s1.setChecked(false);
                            s2.setChecked(false);
                            s3.setChecked(false);
                            Stop_service_from_here.stop(Activity_settings.this.getApplicationContext());
                            Start_service_from_here.start(Activity_settings.this.getApplicationContext());

                            s.setChecked(true);
                            Db_functions funcs=new Db_functions(Activity_settings.this.getApplicationContext());
                            funcs.set_accuracy(accuracy);
                            funcs.close_all();
                            Stop_service_from_here.stop(Activity_settings.this.getApplicationContext());
                            Start_service_from_here.start(Activity_settings.this.getApplicationContext());

                        }

                        @Override
                        public void onfalse() {
                            s1.setChecked(false);
                            s2.setChecked(false);
                            s3.setChecked(false);

                            int previous_accuracy=Class_db_values_settings.give_accuracy(Activity_settings.this.getApplicationContext());
                            if(previous_accuracy==1)
                            {
                                s3.setChecked(true);
                            }
                            else if(previous_accuracy==2)
                            {
                                s2.setChecked(true);
                            }
                            else
                            {
                                previous_accuracy=3;
                                s1.setChecked(true);
                            }
                            Db_functions funcs=new Db_functions(Activity_settings.this.getApplicationContext());
                            funcs.set_accuracy(previous_accuracy);
                            funcs.close_all();

                        }

                    };
                    alert.ok_or_cancel(Activity_settings.this, "", "Are you sure to change the accuracy?");

                }
                else
                {
                    s.setChecked(true);
                }
            }
        };
        switch_accuracy_high.setOnClickListener(click_listner);
        switch_accuracy_low.setOnClickListener(click_listner);
        switch_accuracy_med.setOnClickListener(click_listner);
        switch_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch_internet.isChecked()) {
                    switch_internet.setChecked(false);
                } else {
                    switch_internet.setChecked(true);
                }
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        switch_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch_location.isChecked()) {
                    switch_location.setChecked(false);
                } else {
                    switch_location.setChecked(true);
                }
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
    }
    @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void set_dnd()
    {
        Stop_service_from_here.stop(getApplicationContext());
        Db_functions funcs=new Db_functions(getApplicationContext());
        funcs.set_dnd();
        funcs.close_all();
    }
    private void unset_dnd()
    {
        Db_functions funcs=new Db_functions(getApplicationContext());
        funcs.unset_dnd();
        funcs.close_all();
        Start_service_from_here.start(getApplicationContext());
    }
    private void arrange_accuracy_switches()
    {
        Switch s1=(Switch) findViewById(R.id.switch_activity_settings_accuracy_high);
        Switch s2=(Switch) findViewById(R.id.switch_activity_settings_accuracy_med);
        Switch s3=(Switch) findViewById(R.id.switch_activity_settings_accuracy_low);
        s1.setChecked(false);
        s2.setChecked(false);
        s3.setChecked(false);
        int previous_accuracy=Class_db_values_settings.give_accuracy(Activity_settings.this.getApplicationContext());
        if(previous_accuracy==1)
        {
            s3.setChecked(true);
        }
        else if(previous_accuracy==2)
        {
            s2.setChecked(true);
        }
        else
        {
            s1.setChecked(true);
        }
    }
    public void update_gps()
    {
        LocationManager manager=(LocationManager)this.getSystemService(this.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            switch_location.setChecked(true);
        }
        else
        {
            switch_location.setChecked(false);
        }
    }
    public void update_internet_connection()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo!=null)
        {
            switch_internet.setChecked(true);
        }
        else
        {
            switch_internet.setChecked(false);
        }
    }
    private void register_for_gps_updates()
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
    }

}
