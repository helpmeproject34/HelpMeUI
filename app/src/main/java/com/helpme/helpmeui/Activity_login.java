package com.helpme.helpmeui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.helpme.after_login.Activity_welcome;
import com.helpme.alert_dialogs.Alert_ok;
import com.helpme.databases.Db_functions;
import com.helpme.json.Response;
import com.helpme.register.Activity_register;

public class Activity_login extends Activity {


    TextView var_register;
    Button var_login_button;
    ProgressDialog dialog;
    Handler handler;
    EditText var_textview_username;
    EditText var_textview_password;
    EditText var_textview_phone;
    String var_username;
    String var_phone;
    String var_password;
    Thread t;
    TextView var_text_view_forgot_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verify_for_previous_login();
        load_all_objects();
        register_for_clicks();
    }
    private void verify_for_previous_login()
    {
        if(Class_alreadyLogin.login_or_not(getApplicationContext()))
        {
            Log.e("service", "service triggered from verify_previous_login");
            start_service();
            Intent intent_welcome=new Intent(Activity_login.this.getApplicationContext(),Activity_welcome.class);
            intent_welcome.putExtra("username", Class_alreadyLogin.username);
            intent_welcome.putExtra("phone", Class_alreadyLogin.phone);

            startActivity(intent_welcome);
            finish();
        }
    }

    private void load_all_objects()
    {
         var_textview_username=(EditText)findViewById(R.id.edittext_username);
         var_textview_password=(EditText)findViewById(R.id.edittext_login_password);
        var_textview_phone=(EditText)findViewById(R.id.edittext_login_phone);
        var_register=(TextView)findViewById(R.id.textview_register);
        var_login_button=(Button)findViewById(R.id.button_login);
        var_text_view_forgot_password=(TextView)findViewById(R.id.textview_activity_login_forgot_password);
        handler=new Handler();
    }
    private void register_for_clicks()
    {

        var_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Activity_login.this, Activity_register.class);
                startActivity(i);
                finish();
            }
        });

        var_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog=new ProgressDialog(Activity_login.this,R.style.DialogTheme);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("Authenticating.....");
                dialog.show();
                if(verification())
                {
                    verify_details();

                }
                else
                {
                    dialog.cancel();
                }


            }
        });
        var_text_view_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert_ok alert=new Alert_ok()
                {
                    @Override
                    public void ontrue(String name) {
                        name=name.trim();
                        forgot_password(name);
                    }
                };
                alert.forgot_password_popup(Activity_login.this,"CANCEL","OK");
            }
        });

    }
    private boolean verification()
    {
        boolean result=false;
        final String string_username=var_textview_username.getText().toString().trim();
        final String string_password=var_textview_password.getText().toString().trim();
        final String string_phone=var_textview_phone.getText().toString().trim();
        var_username=string_username;
        var_password=string_password;
        var_phone=string_phone;
        if(string_username.length()==0)
        {
            Toast.makeText(Activity_login.this.getApplicationContext(), "Invalid username", Toast.LENGTH_LONG).show();
            var_textview_username.setError("Invalid Username");

        }
        else if(string_phone.length()!=10)
        {
            Toast.makeText(Activity_login.this.getApplicationContext(), "Invalid phone number", Toast.LENGTH_LONG).show();
            var_textview_username.setError("Invalid Phone number");

        }
        else if(string_password.length()<6)
        {
            Toast.makeText(Activity_login.this.getApplicationContext(), "Invalid password", Toast.LENGTH_LONG).show();
            var_textview_username.setError("Short Password");

        }
        else
        {
            result=true;
        }
        return result;
    }
    private void verify_details()
    {
        if(t!=null)
        {
            if(t.isAlive())
            {
                return;
            }
        }

        t=new Thread(new Runnable() {

            @Override
            public void run() {

                final Response res;
                res= Class_login_verifier.verify(var_username, var_phone, var_password);
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if(res.bool==true)
                        {
                            take_after_login(var_username,var_phone);

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), res.message, Toast.LENGTH_LONG).show();
                        }
                        dialog.cancel();
                    }
                });


            }
        });
        t.start();
    }
    private void take_after_login(String string_username,String string_phone)
    {
        Log.e("service", "service triggered from take_after_login");
        databasework(var_username,var_phone,0,3);
        start_service();
        Intent intent_after_login=new Intent(Activity_login.this.getApplicationContext(),Activity_welcome.class);
        intent_after_login.putExtra("username",string_username);
        intent_after_login.putExtra("phone", string_phone);
        startActivity(intent_after_login);
        finish();
    }
    private void start_service()
    {
        LocationManager manager=(LocationManager)this.getSystemService(this.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
           /* if(!isMyServiceRunning(Class_applocationservice.class))
            {
                Log.e("service", "service started now");
                Intent serviceIntent = new Intent(getApplicationContext(),Class_applocationservice.class);
                Bundle bundle=new Bundle();
                bundle.putLong("min_distance",3);
                bundle.putLong("min_time",1000);
                getBaseContext().startService(serviceIntent);
            }*/
            //Start_service_from_here.start(getBaseContext());
            Intent intent=new Intent();
            intent.setAction("helpme.start_service");
            sendBroadcast(intent);
        }
    }
    private void databasework(String username,String phone,int dnd,int accuracy)
    {
        Db_functions funcs=new Db_functions(this.getApplicationContext());
        funcs.write_table_prev_login(username, phone,dnd,accuracy);
        funcs.close_all();
    }

    private void forgot_password(final String email)
    {
        if(t!=null)
        {
            if(t.isAlive())
            {
                return;
            }
        }
        dialog=new ProgressDialog(Activity_login.this,R.style.DialogTheme);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMessage("Sending you a email.....");
        dialog.show();
        t=new Thread(new Runnable() {
            @Override
            public void run() {
                final Response result=Class_send_email_forgot_password.change(email);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(dialog!=null)
                        {
                            dialog.dismiss();
                        }
                        Alert_ok.show(Activity_login.this,result.message);
                    }
                });
            }
        });
        t.start();
    }


}
