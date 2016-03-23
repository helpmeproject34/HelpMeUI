package com.helpme.settings;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helpme.helpmeui.R;

public class Activity_tracklist extends AppCompatActivity {


    LinearLayout layout;
    String var_username;
    String var_phone;
    Thread t;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracklist);
        load_all_objects();
        register_for_clicks();
    }
    private void load_all_objects()
    {
        layout=(LinearLayout)findViewById(R.id.linearlayout_activity_tracklist);
    }
    private void register_for_clicks()
    {

    }
    private void clean()
    {
        int len=layout.getChildCount();
        for(int i=0;i<len;i++)
        {
            View view=layout.getChildAt(i);
            if(view instanceof Button)
            {
                layout.removeViewAt(i);
            }
            else if(view instanceof TextView)
            {
                layout.removeViewAt(i);
            }
        }
    }
}
