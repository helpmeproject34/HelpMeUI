package com.helpme.groups;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.helpme.alert_dialogs.Alert_ok;
import com.helpme.helpmeui.R;
import com.helpme.json.Response;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class Activity_show_groups extends Activity {

	String var_username;
	String var_phone;
	ProgressDialog dialog;
	Handler handler;
	Adapter_groups adapter;
	ListView listview;
	RelativeLayout layout;
	Thread t;
	Boolean allow_thread=false;
	ArrayList<Class_group_object> group_objects;
	Context this_context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_groups);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		if(getIntent().getExtras()==null)
		{
			Toast.makeText(getApplicationContext(), "illeagal opening of activity", Toast.LENGTH_SHORT).show();
			finish();
		}
		else
		{
			
			Bundle bundle=getIntent().getExtras();
			var_username=bundle.getString("username");
			var_phone=bundle.getString("phone");
			this_context=this;
			//var_progressbar=(ProgressBar)findViewById(R.id.progressbar_show_groups);
			
			adapter=new Adapter_groups();
			layout=(RelativeLayout)findViewById(R.id.relativelayout_activity_show_groups);
			
			group_objects = new ArrayList<Class_group_object>();

			ListView listview=(ListView)findViewById(R.id.listview_groups);
			listview.setAdapter(adapter);
			
			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
					on_item_click(arg0, view, arg2, arg3);
				}

			});
			listview.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View view) {

					return false;
				}
			});
			handler=new Handler();
			load_group_names();
		}
	}
	
	//@SuppressLint("InlinedApi")
	private  void load_group_names()
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
			if(t.isAlive()&&!allow_thread)
			{
				return;
			}
		}
		clean();
		dialog=new ProgressDialog(Activity_show_groups.this,R.style.DialogTheme);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("Loading groups.....");
		dialog.show();
		adapter.arraylist.clear();
		adapter.notifyDataSetChanged();

		t=new Thread(new Runnable() {
			@Override
			public void run() {
				final Response result=Class_group_loader.get_group_names(var_username,var_phone,group_objects);
				handler.post(new Runnable() {

					@Override
					public void run() {

						if (result.bool == true) {
							int total_items = group_objects.size();
							for (int i = 0; i < total_items; i++) {
								adapter.addItem(group_objects.get(i));
							}
							adapter.notifyDataSetChanged();
						} else {
							Alert_ok alert = new Alert_ok() {
								@Override
								public void ontrue() {

									super.ontrue();
									if (result.value == -1) {
										refresh();
									} else {
										create_new_group();
									}
								}

								@Override
								public void onfalse() {

									super.onfalse();

								}
							};
							if (result.value == -1) {
								alert.ok_or_cancel(this_context, "", result.message, "CANCEL", "REFRESH");
							} else if (result.value == 0) {
								alert.ok_or_cancel(this_context, "", result.message, "CANCEL", "CREATE NEW GROUP");
							}
						}
						dialog.dismiss();
					}
				});
			}
		});
		t.start();

		
	}
	private void add_button()
	{
		Button b=new Button(getApplicationContext());
		b.setText("Refresh");

		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				layout.removeView(arg0);
				refresh();

			}
		});
		LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		b.setLayoutParams(params);
		layout.addView(b);
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
		}
	}
	private void on_item_click(AdapterView<?> parent, View view, int pos,long id)
	{
		Class_group_object object=adapter.arraylist.get(pos);
		Intent intent=new Intent(getApplicationContext(),Activity_group_people.class);
		Bundle bundle=new Bundle();
		bundle.putString("username", var_username);
		bundle.putString("phone", var_phone);
		bundle.putString("group_name",object.group_name);
		bundle.putString("group_id",object.group_id);
		bundle.putString("group_admin_username", object.group_admin_username);
		bundle.putString("group_admin_phone",object.group_admin_phone);
		
		intent.putExtras(bundle);
		
		startActivity(intent);
		Toast.makeText(getApplicationContext(), object.group_name, Toast.LENGTH_SHORT).show();
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.menu_groups, menu);
		menu.getItem(2).setVisible(false);

		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		if(item.getItemId()==R.id.action_group_add)
		{

			create_new_group();
		}
		else if(item.getItemId()==R.id.action_group_refresh)
		{
			refresh();
		}
		else if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}
	@Override
	protected void onStart() {
		
		super.onStart();
		refresh();
	}
	private void create_new_group()
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

				return;
			}
		}
		Toast.makeText(getApplicationContext(), "New group will be created", Toast.LENGTH_SHORT).show();

		Alert_ok alert=new Alert_ok()
		{
			@Override
			public void ontrue(String name)
			{

				add_new_group(name.trim());
			}
			@Override
			public void onfalse()
			{

			}

		};
		alert.new_group_input(this,"Cancel","Create");

	}
	private void add_new_group(final String group_name)
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

				return;
			}
		}
		dialog=new ProgressDialog(Activity_show_groups.this,R.style.DialogTheme);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("Adding new group.....");
		dialog.show();

		Thread t2=new Thread(new Runnable() {
			
			boolean result;
			@Override
			public void run() {
				result= Class_add_new_group.add_new_group(var_username, var_phone, group_name);
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						
						if(result==true)
						{
							//group_objects.add(new Class_group_object(group_name,"You created this group"));
							//load_group_names();
							Toast.makeText(Activity_show_groups.this.getApplicationContext(), "Creation of new group is success", Toast.LENGTH_SHORT).show();

							adapter.arraylist.clear();
							adapter.notifyDataSetChanged();
							add_button();

						}
						else
						{
							Toast.makeText(Activity_show_groups.this.getApplicationContext(), "Creation of new group failed", Toast.LENGTH_SHORT).show();
							Alert_ok.show(this_context, "Creation of new group failed");
						}
						dialog.dismiss();

					}
				});
				
			}
			
		});
		t2.start();
	}
	private void refresh()
	{
		/*Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				load_group_names();
			}
		});
		
		t.start();*/
		load_group_names();
	}
}
