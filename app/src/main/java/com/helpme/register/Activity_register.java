package com.helpme.register;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.helpme.helpmeui.Activity_login;
import com.helpme.helpmeui.R;
import com.helpme.json.Response;

/**
 * Created by HARINATHKANCHU on 07-03-2016.
 */

public class Activity_register extends Activity {

    EditText var_edittext_username;
    EditText var_edittext_password;
    EditText var_edittext_confirm_password;
    EditText var_edittext_phone;
    EditText var_edittext_email;
    Button var_button_register;
    TextView var_button_login;
    String var_username;
    String var_email;
    String var_password;
    String var_confirm_password;
    String var_phone;
    ProgressDialog dialog;
    Handler handler;
    Thread t;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        load_all_objects();
        register_all_clicks();
    }
    private void load_all_objects()
    {
        var_edittext_password=(EditText)findViewById(R.id.edittext_register_password);
        var_edittext_username=(EditText)findViewById(R.id.edittext_register_username);
        var_edittext_email=(EditText)findViewById(R.id.edittext_register_email);
        var_edittext_phone=(EditText)findViewById(R.id.edittext_register_phone);
        var_edittext_confirm_password=(EditText)findViewById(R.id.edittext_register_repeatpassword);
        var_button_register=(Button)findViewById(R.id.button_register_register);
        var_button_login=(TextView)findViewById(R.id.textview_register_login);
        handler=new Handler();
    }
    private void register_all_clicks()
    {
        var_button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Activity_register.this, Activity_login.class);
                startActivity(i);
                finish();
            }
        });
        var_button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var_username=var_edittext_username.getText().toString();
                var_email=var_edittext_email.getText().toString();
                var_phone=var_edittext_phone.getText().toString();
                var_password=var_edittext_password.getText().toString();
                var_confirm_password=var_edittext_confirm_password.getText().toString();
                dialog=new ProgressDialog(Activity_register.this,R.style.DialogTheme);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("Register in progress.....");

                dialog.show();
                if(check(var_username, var_email, var_phone, var_password, var_confirm_password))
                {
                    add_data_to_server();
                }
                else {
                    dialog.dismiss();
                }
            }
        });
    }
    private boolean check(String username,String email,String phone,String password,String confirm_password)
    {
        boolean result=false;
        username=username.trim();
        email=email.trim();
        phone=phone.trim();
        if(username.length()==0)
        {
            Toast.makeText(Activity_register.this.getApplicationContext(), "Invalid username", Toast.LENGTH_LONG).show();
            var_edittext_username.setError("Invalid Username (empty)");
        }
        else if(!email.contains("@")||!email.contains(".")||email.length()<5)
        {
            Toast.makeText(Activity_register.this.getApplicationContext(), "Invalid email", Toast.LENGTH_LONG).show();
            var_edittext_email.setError("Invalid email");
        }
        else if(phone.length()!=10)
        {
            Toast.makeText(Activity_register.this.getApplicationContext(), "Invalid phone number", Toast.LENGTH_LONG).show();
            var_edittext_phone.setError("Invalid phone number");
        }
        else if(password.length()<6)
        {
            Toast.makeText(Activity_register.this.getApplicationContext(), "Short password", Toast.LENGTH_LONG).show();
            var_edittext_password.setError("Short password");
        }
        else if(!confirm_password.equals(password))
        {
            Toast.makeText(Activity_register.this.getApplicationContext(),"Passwords does not match", Toast.LENGTH_LONG).show();
            var_edittext_confirm_password.setError("Password does not match");
            var_edittext_password.setError("Password does not match");
            var_edittext_confirm_password.setText("");
            var_edittext_password.setText("");
        }
        else
        {
            result=true;
        }
        return result;
    }
    private void add_data_to_server()
    {
        if(t!=null)
        {
            if(t.isAlive())
            {
                if(dialog!=null)
                {
                    if(dialog.isShowing())
                    {
                        dialog.dismiss();
                    }
                }
                return;
            }
        }
        t=new Thread(new Runnable() {

            @Override
            public void run() {

                final Response res= Class_save_data.save_data(var_username, var_email, var_phone, var_password, var_confirm_password);


                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if(res.bool)
                        {
                            dialog.dismiss();
                            Toast.makeText(Activity_register.this.getApplicationContext(), "Registration successful now please login", Toast.LENGTH_LONG).show();
                            Intent intent_login=new Intent(Activity_register.this.getApplicationContext(),Activity_login.class);
                            startActivity(intent_login);
                            finish();
                        }
                        else
                        {
                            if(res.message.contains("username"))
                            {
                                var_edittext_username.setError("Username already exists");
                            }
                            if(res.message.contains("email"))
                            {
                                var_edittext_email.setError("email already exists");
                            }
                            if(res.message.contains("phone")||res.message.contains("mobile"))
                            {
                                var_edittext_phone.setError("Number already exists");
                            }
                            dialog.dismiss();
                            Toast.makeText(Activity_register.this.getApplicationContext(), res.message, Toast.LENGTH_LONG).show();

                        }

                    }
                });


            }
        });
        t.start();
    }
}
