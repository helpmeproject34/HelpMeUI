package com.helpme.settings;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.helpme.contacts.Class_give_phones;
import com.helpme.helpmeui.R;
import com.helpme.json.Response;

import java.util.ArrayList;

public class Activity_tracklist extends Activity {


    LinearLayout layout;
    String var_username;
    String var_phone;
    Thread t;
    AlertDialog dialog;
    Bundle bundle;
    Adapter_tracklist adapter;
    ListView listview;
    Handler handler;
    ArrayList<Class_tracklist_object> list;
    ContentResolver resolver;
    String[] phones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracklist);
        Intent intent_recieved= getIntent();
        bundle=intent_recieved.getExtras();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        if(bundle==null)
        {
            finish();
        }
        try
        {
            var_username=bundle.getString("username");
            var_phone=bundle.getString("phone");
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"illegal opening of activity",Toast.LENGTH_SHORT).show();
        }
        load_all_objects();
        register_for_clicks();
        refresh();
    }
    private void load_all_objects()
    {
        layout=(LinearLayout)findViewById(R.id.linearlayout_activity_tracklist);
        adapter=new Adapter_tracklist();
        listview=(ListView)findViewById(R.id.listview_activity_tracklist);
        listview.setAdapter(adapter);
        handler=new Handler();
        list=new ArrayList<>();
        resolver=getContentResolver();
    }
    private void register_for_clicks()
    {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int index, long id) {

                Class_tracklist_object object = adapter.arraylist.get(index);

                if (object.check == true) {
                    object.check = false;
                    CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox_layout_tracklist);

                    cb.setChecked(false);
                } else {
                    object.check = true;
                    CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox_layout_tracklist);
                    cb.setChecked(true);
                }
                adapter.notifyDataSetChanged();
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracklist, menu);
        return true;
    }
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        if(item.getItemId()==R.id.action_tracklist_refresh)
        {
            refresh();
        }
        else if(item.getItemId()==R.id.action_tracklist_save)
        {
            save();
        }
        else if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onMenuItemSelected(featureId, item);
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
                refresh();
            }
        });
        ActionBar.LayoutParams params=new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        b.setLayoutParams(params);
        layout.addView(b);
    }
    private void refresh()
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
                dialog=new ProgressDialog(Activity_tracklist.this,R.style.DialogTheme);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("please wait.....");
                dialog.show();
                return;
            }
        }

        dialog=new ProgressDialog(Activity_tracklist.this,R.style.DialogTheme);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("loading list .....");
        dialog.show();

        clean();
        adapter.arraylist.clear();
        phones= Class_give_phones.give(resolver);
        t=new Thread(new Runnable() {
            @Override
            public void run() {
                load_list();
            }
        });
        t.start();
    }
    private void load_list()
    {

        final Response result= Class_load_tracklist.load(list,phones, var_phone, resolver);

        handler.post(new Runnable() {

            @Override
            public void run() {

                adapter.notifyDataSetChanged();
                if(result.bool)
                {
                    adapter.arraylist.clear();
                    int length=list.size();
                    for(int i=0;i<length;i++)
                    {
                        adapter.arraylist.add(list.get(i));
                    }
                    Toast.makeText(getApplicationContext(), result.message, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    add_button();
                    Toast.makeText(getApplicationContext(), result.message, Toast.LENGTH_SHORT).show();
                }
                if(dialog!=null)
                {
                    dialog.dismiss();
                }
            }
        });
    }
    private void save()
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
                dialog=new ProgressDialog(Activity_tracklist.this,R.style.DialogTheme);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("please wait.....");
                dialog.show();
                return;
            }
        }
        dialog=new ProgressDialog(Activity_tracklist.this,R.style.DialogTheme);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("saving list.....");
        dialog.show();

        t=new Thread(new Runnable() {
            @Override
            public void run() {
                save_all();
            }
        });
        t.start();
    }
    private void save_all()
    {
        final Response result;
        ArrayList<String> list=new ArrayList<>();
        ArrayList<String> blocklist=new ArrayList<>();
        int len=adapter.arraylist.size();
        for(int i=0;i<len;i++)
        {
            if(adapter.arraylist.get(i).check==false)
            {
                list.add(adapter.arraylist.get(i).name);
                blocklist.add("1");
            }
            else
            {
                list.add(adapter.arraylist.get(i).name);
                blocklist.add("0");
            }
        }
        //final ArrayList<String> phone_list=list;
        //final ArrayList<String> block_list=blocklist;
        result= Class_save_tracklist.save(list, blocklist, var_phone);
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), result.message, Toast.LENGTH_SHORT).show();
                if(dialog!=null)
                {
                    dialog.dismiss();
                }
            }
        });
    }
}
