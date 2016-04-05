package com.helpme.profiles;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.helpme.alert_dialogs.Alert_ok;
import com.helpme.databases.Db_functions;
import com.helpme.helpmeui.Activity_login;
import com.helpme.helpmeui.Class_alreadyLogin;
import com.helpme.helpmeui.R;
import com.helpme.json.Response;
import com.helpme.services.Class_applocationservice;

public class Activity_user_profile extends ActionBarActivity {

    com.helpme.widgets.SAutoBgButton button_logout;
    com.helpme.widgets.SAutoBgButton button_change;
    String var_username;
    String var_phone;

    ProgressDialog dialog;
    Handler handler;
    Thread t;
    TextView textview_email;
    TextView textview_phone;
    TextView textview_username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        get_intent();
        load_all_items();
        register_for_clicks();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void get_intent()
    {
        Intent i=getIntent();
        if(i==null)
        {
            finish();
        }
        Bundle b=i.getExtras();
        if(b==null)
        {
            finish();
        }
        var_username=b.getString("username");
        var_phone=b.getString("phone");

    }
    private void load_all_items()
    {
        button_logout=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_activity_user_profile_logout);
        button_change=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_activity_user_profile_change_password);
        handler=new Handler();
        textview_email=(TextView)findViewById(R.id.textview_user_profile_2);
        textview_phone=(TextView)findViewById(R.id.textview_user_profile_3);
        textview_username=(TextView)findViewById(R.id.textview_user_profile_6);
        textview_email.setText("Your email");
        textview_username.setText(var_username);
        textview_phone.setText(var_phone);
    }
    private void register_for_clicks()
    {
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert_ok alert=new Alert_ok()
                {
                    @Override
                    public void ontrue()
                    {
                        logout();
                    }
                };
                alert.ok_or_cancel(Activity_user_profile.this,"","Are you sure to logout ?","CANCEL","LOGOUT");

            }
        });
        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert_ok alert=new Alert_ok()
                {
                    @Override
                      public void ontrue(String current_password,String new_password)
                    {
                        change_password_in_server(current_password, new_password);
                    }
                    @Override
                    public void ontrue(String message)
                    {
                        Alert_ok.show(Activity_user_profile.this,message);
                    }

                };
                alert.change_password(Activity_user_profile.this,"CANCEL","CHANGE");
            }
        });
    }

    private void logout()
    {
        Intent i=new Intent(getApplicationContext(),Class_applocationservice.class);
        getApplicationContext().stopService(i);

        Db_functions funcs=new Db_functions(this);
        Db_functions.delete_table_prev_login();
        funcs.close_all();

        Class_alreadyLogin.islogin=false;

        /*Intent intent = new Intent(this, Activity_login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        Intent intent = new Intent(getApplicationContext(), Activity_login.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        startActivity(mainIntent);
    }
    private void change_password_in_server(final String current_password, final String new_password)
    {
        if(t!=null)
        {
            if(t.isAlive())
            {
                return;
            }
        }
        dialog=new ProgressDialog(Activity_user_profile.this,R.style.DialogTheme);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMessage("Saving please wait.....");
        dialog.show();
        t=new Thread(new Runnable() {
            @Override
            public void run() {
                final Response result=Class_change_password.change(current_password,new_password,var_username);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Alert_ok.show(Activity_user_profile.this,result.message);
                        if(dialog!=null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        t.start();
    }
}
