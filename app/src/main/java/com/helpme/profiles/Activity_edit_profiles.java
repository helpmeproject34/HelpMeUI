package com.helpme.profiles;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.helpme.helpmeui.R;

public class Activity_edit_profiles extends Activity {

    com.helpme.widgets.SAutoBgButton user_profile;
    com.helpme.widgets.SAutoBgButton helper_profile;
    String var_username;
    String var_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profiles);
        load_objects();
        register_clicks();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }
    private void load_objects()
    {
        user_profile=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_edit_profile_1);
        helper_profile=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_edit_profile_2);
    }
    private void register_clicks()
    {
        user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Activity_user_profile.class);
                Bundle bundle=new Bundle();
                bundle.putString("username","username");
                bundle.putString("phone", "phone");
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        helper_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Activity_helper_profile.class);
                Bundle bundle=new Bundle();
                bundle.putString("username","username");
                bundle.putString("phone", "phone");
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
