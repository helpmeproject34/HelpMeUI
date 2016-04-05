package com.helpme.profiles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.helpme.alert_dialogs.Alert_ok;
import com.helpme.databases.Db_functions;
import com.helpme.helpmeui.R;
import com.helpme.json.Response;

public class Activity_helper_profile extends ActionBarActivity {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    Thread t;
    ProgressDialog dialog;
    Switch switch_;
    ScrollView scrollview;
    String working_name;
    String working_phone;
    String var_username;
    int category;
    int from;
    int to;
    int is_enabled;
    Double latitude;
    Double longitude;
    String working_days;
    EditText edittext_working_name;
    EditText edittext_working_mobile;
    EditText edittext_from;
    EditText edittext_to;
    CheckBox[] checkboxes=new CheckBox[7];
    com.helpme.widgets.SAutoBgButton detect_location;
    com.helpme.widgets.SAutoBgButton change_location;
    com.helpme.widgets.SAutoBgButton save_button;
    LinearLayout layout;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        recive_intent();
        load_all_objects();
        register_for_clicks();
        read_database();
        if(is_enabled==1)
        {
            switch_.setChecked(true);
           enable_all_views();
        }
        else
        {
            switch_.setChecked(false);
            disable_all_views();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void recive_intent()
    {
        Intent i=getIntent();
        Bundle b=i.getExtras();
        var_username=b.getString("username");
    }
    private void load_all_objects()
    {
        spinner = (Spinner) findViewById(R.id.spinner_helper_profile);
        adapter = ArrayAdapter.createFromResource(this,R.array.array_helper_categories,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        switch_=(Switch)findViewById(R.id.switch_activity_helper_profile);
        scrollview=(ScrollView)findViewById(R.id.scrollview_activity_helper_pofile);
        edittext_working_name=(EditText)findViewById(R.id.edittext_activity_helper_working_name);
        edittext_working_mobile=(EditText)findViewById(R.id.edittext_activity_helper_working_phone);
        edittext_from=(EditText)findViewById(R.id.edittext_activity_helper_from);
        edittext_to=(EditText)findViewById(R.id.edittext_activity_helper_to);
        detect_location=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_activity_helper_detect_location);
        change_location=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_activity_helper_change_location);
        save_button=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_activity_helper_change_save);
        checkboxes[0]=(CheckBox)findViewById(R.id.checkbox_activity_helper_checkbox_0);
        checkboxes[1]=(CheckBox)findViewById(R.id.checkbox_activity_helper_checkbox_1);
        checkboxes[2]=(CheckBox)findViewById(R.id.checkbox_activity_helper_checkbox_2);
        checkboxes[3]=(CheckBox)findViewById(R.id.checkbox_activity_helper_checkbox_3);
        checkboxes[4]=(CheckBox)findViewById(R.id.checkbox_activity_helper_checkbox_4);
        checkboxes[5]=(CheckBox)findViewById(R.id.checkbox_activity_helper_checkbox_5);
        checkboxes[6]=(CheckBox)findViewById(R.id.checkbox_activity_helper_checkbox_6);
        layout=(LinearLayout)findViewById(R.id.linearlayout_activity_helper_profile);
        handler=new Handler();
    }
    private void register_for_clicks()
    {
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        switch_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch_.isChecked()) {
                    switch_.setChecked(false);
                    enable_helper_profile();
                } else {
                    switch_.setChecked(true);
                    disable_helper_profile();
                }
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        detect_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detect_location.setError(null);
               get_loction();

            }
        });
        change_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude=null;
                longitude=null;
                detect_location.setEnabled(true);
                change_location.setEnabled(false);
            }
        });
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
    private void get_loction()
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
                dialog=new ProgressDialog(Activity_helper_profile.this,R.style.DialogTheme);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("please wait.....");
                dialog.show();
                return;
            }
        }
        dialog=new ProgressDialog(Activity_helper_profile.this,R.style.DialogTheme);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("detecting your location.....");
        dialog.show();
        t=new Thread(new Runnable() {
            @Override
            public void run() {
                final LatLng loc=Class_get_location.get(Activity_helper_profile.this);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(dialog!=null)
                        {
                            dialog.dismiss();
                        }
                        if(loc!=null)
                        {
                            latitude=loc.latitude;
                            longitude=loc.longitude;
                            detect_location.setEnabled(false);
                            change_location.setEnabled(true);
                            Toast.makeText(getApplicationContext(),"Location Detected",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            latitude=null;
                            longitude=null;
                            detect_location.setEnabled(true);
                            change_location.setEnabled(false);
                            Toast.makeText(getApplicationContext(),"Failed to get location.Try again",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
        t.start();
    }
    private void read_database()
    {
        Db_functions funcs=new Db_functions(getApplicationContext());
        Cursor cursor=funcs.read_table_helper_profile();

        if(cursor.moveToFirst())
        {
            cursor.moveToLast();
            working_name=cursor.getString(1);
            working_phone=cursor.getString(2);
            category=cursor.getInt(3);
            from=cursor.getInt(4);
            to=cursor.getInt(5);
            working_days=cursor.getString(6);
            is_enabled=cursor.getInt(7);
        }
        else
        {
            working_name="";
            working_phone="";
            category=0;
            working_days="0000000";
            is_enabled=1;
        }
        funcs.close_all();
        edittext_working_name.setText(working_name);
        edittext_working_mobile.setText(working_phone);
        edittext_from.setText(from + "");
        edittext_to.setText(to + "");
        spinner.setSelection(category);
        for(int i=0;i<7;i++)
        {
            if(working_days.charAt(i)=='0')
            {
                checkboxes[i].setChecked(false);
            }
            else
            {
                checkboxes[i].setChecked(true);
            }
        }

    }

    private boolean verify_fillings()
    {
        boolean result=true;
        working_name=edittext_working_name.getText().toString().trim();
        if(working_name.length()<5)
        {
            edittext_working_name.setError("At least 5 chars required");
            result=false;
        }
        working_phone=edittext_working_mobile.getText().toString().trim();
        if(working_phone.length()!=10)
        {
            edittext_working_mobile.setError("wrong mobile number");
            result=false;
        }
        category=spinner.getSelectedItemPosition();
        if(category<1||category>8)
        {
            Toast.makeText(getApplicationContext(),"Select one category",Toast.LENGTH_SHORT).show();
            return false;
        }
        try
        {
            from=Integer.parseInt(edittext_from.getText().toString().trim());
            to=Integer.parseInt(edittext_to.getText().toString().trim());
            if(from<1||from>12||to<1||to>12)
            {
                edittext_from.setError("enter a number from 1 to 12");
                edittext_to.setError("enter a number from 1 to 12");
                return false;
            }
        }
        catch(Exception e)
        {
           edittext_from.setError("enter a number from 1 to 12");
            edittext_to.setError("enter a number from 1 to 12");
            return false;
        }
        working_days="";
        for(int i=0;i<7;i++)
        {
            if(checkboxes[i].isChecked())
            {
                working_days=working_days+"1";
            }
            else
            {
                working_days=working_days+"0";
            }
        }
        if(working_days.equals("0000000"))
        {
            Toast.makeText(getApplicationContext(),"Select atleast one day in working days",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(latitude==null||longitude==null)
        {
            Toast.makeText(getApplicationContext(),"Please set your current location",Toast.LENGTH_SHORT).show();
            detect_location.setError("Set your current location here");
            result=false;
        }
        /*if(Class_get_location.postal_code==null)
        {
            Toast.makeText(getApplicationContext(),"Postal code not detected",Toast.LENGTH_SHORT).show();
            Alert_ok.show(this,"Postal code not detected.Turn on GPS and internet connection and set your location again");
            result=false;
        }*/
        return result;
    }
    private void save()
    {
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
                dialog=new ProgressDialog(Activity_helper_profile.this,R.style.DialogTheme);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("please wait.....");
                dialog.show();
                return;
            }
        }
        if(!verify_fillings())
        {
            return;
        }
        dialog=new ProgressDialog(Activity_helper_profile.this,R.style.DialogTheme);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("saving your detials.....");
        dialog.show();
        final Class_profile_object object=new Class_profile_object(var_username,working_name,working_phone,category,latitude,longitude,from,to,working_days);


        t=new Thread(new Runnable() {
            @Override
            public void run() {
               final  Response result=Class_save_helper_profile.save(object);
               handler.post(new Runnable() {
                   @Override
                   public void run() {
                       if(dialog!=null)
                       {
                           dialog.dismiss();
                       }
                       if(result.bool)
                       {
                           save_into_database(working_name,working_phone,category,from,to,working_days);
                           Toast.makeText(Activity_helper_profile.this.getApplicationContext(),"Successfully saved",Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           Alert_ok.show(Activity_helper_profile.this,result.message);
                       }
                   }
               });
            }
        });
        t.start();
    }
    private void save_into_database(String w_name,String w_phone,int cat,int frm,int too,String w_days)
    {
        Db_functions funcs=new Db_functions(getApplicationContext());
        funcs.write_table_helper_profile(w_name, w_phone, cat, frm, too, w_days);
        funcs.close_all();
    }

    private void enable_helper_profile()
    {
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
                dialog=new ProgressDialog(Activity_helper_profile.this,R.style.DialogTheme);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("please wait.....");
                dialog.show();
                return;
            }
        }
        dialog=new ProgressDialog(Activity_helper_profile.this,R.style.DialogTheme);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("enabling your profile.....");
        dialog.show();
        t=new Thread(new Runnable() {
            @Override
            public void run() {
                final Response result= Class_enable_or_disable_helper_profile.disable(var_username, 1);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(dialog!=null)
                        {
                            dialog.dismiss();
                        }
                        if(result.bool)
                        {
                            Alert_ok.show(Activity_helper_profile.this,"Successfully enabled your profile");
                            Db_functions funcs=new Db_functions(getApplicationContext());
                            funcs.enable_helper_profile();
                            funcs.close_all();
                            switch_.setChecked(true);
                            enable_all_views();
                        } else
                        {
                            Alert_ok.show(Activity_helper_profile.this,result.message);
                            switch_.setChecked(false);
                        }

                    }
                });
            }
        });
        t.start();
    }
    private void disable_helper_profile()
    {
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
                dialog=new ProgressDialog(Activity_helper_profile.this,R.style.DialogTheme);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("please wait.....");
                dialog.show();
                return;
            }
        }
        dialog=new ProgressDialog(Activity_helper_profile.this,R.style.DialogTheme);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("disabling your profile.....");
        dialog.show();
        t=new Thread(new Runnable() {
            @Override
            public void run() {
                final Response result= Class_enable_or_disable_helper_profile.disable(var_username, 0);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(dialog!=null)
                        {
                            dialog.dismiss();
                        }
                        if(result.bool)
                        {
                            Alert_ok.show(Activity_helper_profile.this, "Successfully disabled your profile");
                            Db_functions funcs=new Db_functions(getApplicationContext());
                            funcs.disable_helper_profile();
                            funcs.close_all();
                            switch_.setChecked(false);
                            disable_all_views();

                        } else
                        {
                            Alert_ok.show(Activity_helper_profile.this,result.message);
                            switch_.setChecked(true);
                        }

                    }
                });
            }
        });
        t.start();
    }
    private void enable_all_views()
    {

        int len=layout.getChildCount();
        for(int i=0;i<len;i++)
        {
            layout.getChildAt(i).setEnabled(true);
            layout.getChildAt(i).setVisibility(View.VISIBLE);
        }
        layout.setVisibility(LinearLayout.VISIBLE);
        layout.requestLayout();


    }
    private void disable_all_views()
    {

        int len=layout.getChildCount();
        for(int i=0;i<len;i++)
        {
            layout.getChildAt(i).setEnabled(false);
            layout.getChildAt(i).setVisibility(View.GONE);
        }
        layout.setVisibility(LinearLayout.GONE);
        layout.requestLayout();
    }

}
