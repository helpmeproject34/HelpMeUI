package com.helpme.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.helpme.helpmeui.R;


public class Activity_settings extends Activity {


    Switch switch_location;
    Switch switch_internet;
    Switch switch_dnd;
    Switch switch_accuracy_low;
    Switch switch_accuracy_med;
    Switch switch_accuracy_high;
    TextView textview_whocantrack;
    String var_username;
    String var_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        load_all_items();
        register_for_clicks();

    }
    private void load_all_items()
    {
        switch_location=(Switch)findViewById(R.id.switch_activity_settings_location);
        switch_internet=(Switch)findViewById(R.id.switch_activity_settings_internet);
        switch_dnd=(Switch)findViewById(R.id.switch_activity_settings_dnd);
        switch_accuracy_high=(Switch)findViewById(R.id.switch_activity_settings_accuracy_high);
        switch_accuracy_med=(Switch)findViewById(R.id.switch_activity_settings_accuracy_med);
        switch_accuracy_low=(Switch)findViewById(R.id.switch_activity_settings_accuracy_low);
        textview_whocantrack=(TextView)findViewById(R.id.textview_activity_settings_whocantrack);
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
    }
    @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
