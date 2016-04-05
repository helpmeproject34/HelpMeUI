package com.helpme.after_login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.helpme.alert_dialogs.Alert_ok;
import com.helpme.helpmeui.R;
import com.helpme.json.Response;

public class Activity_contact_us extends ActionBarActivity {

    EditText var_edittext_name;
    EditText var_edittext_subject;
    EditText var_edittext_message;
    com.helpme.widgets.SAutoBgButton send;
    String name;
    String subject;
    String message;
    Thread t;
    ProgressDialog dialog;
    Handler handler;
    String var_username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        get_intent();
        load_all_items();
        register_for_clicks();
    }
    private void get_intent()
    {
        Intent i=getIntent();
        if(i==null)
        {
            finish();
        }
        Bundle bundle=i.getExtras();
        if(bundle==null)
        {
            finish();
        }
        var_username=bundle.getString("username");
    }
    private void load_all_items()
    {
        var_edittext_name=(EditText)findViewById(R.id.edittext_activity_contact_us_name);
        var_edittext_subject=(EditText)findViewById(R.id.edittext_activity_contact_us_subject);
        var_edittext_message=(EditText)findViewById(R.id.edittext_activity_contact_us_message);
        send=(com.helpme.widgets.SAutoBgButton)findViewById(R.id.button_activity_contact_us_send);
        handler=new Handler();
    }
    private void register_for_clicks()
    {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verify())
                {
                    send_feedback();
                }
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
    private void send_feedback()
    {
        if(t!=null)
        {
            if(t.isAlive())
            {
                return;
            }
        }
        dialog=new ProgressDialog(this,R.style.DialogTheme);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMessage("Sending your message.....");
        dialog.show();
        t=new Thread(new Runnable() {
            @Override
            public void run() {
                final Response result=Class_send_feedback.send(name,subject,message,var_username);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(dialog!=null)
                        {
                            dialog.dismiss();
                        }
                        Alert_ok.show(Activity_contact_us.this,result.message);
                    }
                });
            }
        });
        t.start();
    }
    private boolean verify()
    {
        boolean result=true;
         name=var_edittext_name.getText().toString().trim();
         subject=var_edittext_subject.getText().toString().trim();
         message=var_edittext_message.getText().toString().trim();
        if(subject.length()<5||subject.length()>=30)
        {
            var_edittext_subject.setError("Length should not be less than 5 and less than 30");
            result=false;
        }
        if(message.length()<5||message.length()>=100)
        {
            var_edittext_subject.setError("Length should not be less than 5 and less than 100");
            result=false;
        }
        return result;
    }
}
