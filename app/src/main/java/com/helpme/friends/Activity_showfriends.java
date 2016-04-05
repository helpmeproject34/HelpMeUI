package com.helpme.friends;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.helpme.alert_dialogs.Alert_ok;
import com.helpme.contacts.Class_contacts_object;
import com.helpme.contacts.Class_give_name;
import com.helpme.contacts.Class_give_phones;
import com.helpme.groups.Class_locations;
import com.helpme.helpmeui.Activity_login;
import com.helpme.helpmeui.R;
import com.helpme.json.Response;
import com.helpme.tracking.Activity_tracking_friend;

import java.util.ArrayList;

/**
 * Created by HARINATHKANCHU on 08-03-2016.
 */
public class Activity_showfriends extends Activity {

    ListView listview;
    Adapter_friends adapter_friends;
    ArrayList<Class_contacts_object> friendslist;
    Handler handler;
    ProgressDialog dialog;
    Thread t;
    LinearLayout layout;
    String var_username;
    String var_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfriends);

        recieve_intent();
        load_all_objects();
        register_for_clicks();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setTitle("Your friends");

        listview.setAdapter(adapter_friends);
        refresh_friendslist();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if(item.getItemId()==R.id.action_refresh)
        {

            refresh_friendslist();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }
    private void recieve_intent()
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
    private void load_all_objects()
    {

        listview=(ListView)findViewById(R.id.listview_activity_showfriends);
        adapter_friends=new Adapter_friends();
        handler=new Handler();
        layout=(LinearLayout)findViewById(R.id.linearlayout_showfriends);
    }
    private void register_for_clicks()
    {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long arg3) {
                if(dialog!=null)
                {
                    if(dialog.isShowing())
                    {
                        return;
                    }
                }
                if(t!=null)
                {
                    if(t.isAlive())
                    {

                        return;
                    }
                }

                dialog=new ProgressDialog(Activity_showfriends.this,R.style.DialogTheme);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.setMessage("Checking for blocked status.....");
                dialog.show();

                final Class_friend_object object=adapter_friends.list.get(index);
                final String friend_phone=object.phone;
                final String friend_name=object.name;
                t=new Thread(new Runnable() {

                    @Override
                    public void run() {

                        Class_locations location=new Class_locations();
                        final Response result= Class_check_tracking.check(var_phone, friend_phone, friend_name,location);
                        if(result.bool)
                        {
                            handler.post(new  Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), result.message, Toast.LENGTH_SHORT).show();


                                    dialog.dismiss();
                                    Intent intent=new Intent(getApplicationContext(),Activity_tracking_friend.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putString("username",var_username);
                                    bundle.putString("phone", var_phone);
                                    bundle.putString("friend_name", friend_name);
                                    bundle.putString("friend_phone", friend_phone);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                   // Alert_ok.show(Activity_showfriends.this,"You can track this person");


                                }
                            });
                        }
                        else
                        {
                            handler.post(new  Runnable() {
                                public void run() {
                                    //Toast.makeText(getApplicationContext(),result.message,Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    Alert_ok.show(Activity_showfriends.this, result.message);

                                }
                            });
                        }
                    }
                });
                t.start();
            }
        });
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
    private void add_button()
    {
        Button b=new Button(getApplicationContext());
        b.setText("Refresh");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                layout.removeView(arg0);
                refresh_friendslist();

            }
        });
        ActionBar.LayoutParams params=new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        b.setLayoutParams(params);
        layout.addView(b);
    }
    private void refresh_friendslist()
    {
        if(dialog!=null)
        {
            if(dialog.isShowing())
            {
                return;
            }
        }
        if(t!=null)
        {
            if(t.isAlive())
            {
                Toast.makeText(this, "Thread is alive", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        clean();
        adapter_friends.list.clear();
        adapter_friends.notifyDataSetChanged();
        dialog=new ProgressDialog(Activity_showfriends.this,R.style.DialogTheme);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMessage("Loading contacts.....");
        dialog.show();
        final ContentResolver resolver=getContentResolver();
        t=new Thread(new Runnable() {
            @Override
            public void run() {



                String[] phones=Class_give_phones.give(resolver);
               String[] boolean_values=Class_verify_phone_number.verification(phones, phones.length);

                int i;

                if(boolean_values[phones.length].equals("1"))
                {
                    for(i=0;i<phones.length;i++)
                    {
                        if(boolean_values[i].equals("1"))
                        {
                            adapter_friends.list.add(new Class_friend_object(Class_give_name.give(phones[i],resolver),phones[i]));
                        }

                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            adapter_friends.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                }
                else
                {
                    handler.post(new  Runnable() {
                        public void run() {
                            //Toast.makeText(getApplicationContext(),result.message,Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            add_button();

                        }
                    });
                }

            }
        });
        t.start();


    }

}
