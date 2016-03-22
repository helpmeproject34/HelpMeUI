package com.helpme.groups;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.helpme.alert_dialogs.Alert_ok;
import com.helpme.helpmeui.R;
import com.helpme.json.Response;

import java.util.ArrayList;



public class Activity_group_people extends Activity {
 
	Handler handler;
	ListView listview;
	Adapter_groups adapter;

	String var_username;
	String var_phone;
	String var_group_id;
	String var_group_name;
	String var_admin_username;
	String var_admin_phone;
	Context this_context;
	Thread t;
	AlertDialog dialog;
	RelativeLayout layout;
	Bundle bundle;
	ArrayList<Class_group_object> friends_objects;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent_recieved= getIntent();
		bundle=intent_recieved.getExtras();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		if(bundle==null || intent_recieved==null)
		{
			Toast.makeText(getApplicationContext(), "Illeagal opening of activity", Toast.LENGTH_SHORT).show();
		}
		else
		{
			setContentView(R.layout.activity_group_people);
			load_all_objects();
			register_for_clicks();
		}
		
		
	}
	private void load_all_objects()
	{

		layout=(RelativeLayout)findViewById(R.id.relative_layout_group_people);
		var_username=bundle.getString("username");
		var_phone=bundle.getString("phone");
		var_group_id=bundle.getString("group_id");
		var_group_name=bundle.getString("group_name");
		var_admin_username=bundle.getString("group_admin_username");
		var_admin_phone=bundle.getString("group_admin_phone");
		handler=new Handler();
		this_context=this;
		setTitle(var_group_name+"'s people");
		listview=(ListView)findViewById(R.id.listview_group_people);
		adapter=new Adapter_groups();
		listview.setAdapter(adapter);
		friends_objects=new ArrayList<Class_group_object>();
		load_group_people();
	}
	private void register_for_clicks()
	{
		listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
										   int index, long id) {
				Class_group_object object = adapter.arraylist.get(index);
				itemLongClick(object, index);
				//Toast.makeText(getApplicationContext(), object.group_name + " long pressed and admin username is " + var_admin_username + "\nadmin phone is " + var_admin_phone, Toast.LENGTH_SHORT).show();
				return false;
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
				load_group_people();

			}
		});
		ActionBar.LayoutParams params=new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
		b.setLayoutParams(params);
		layout.addView(b);
	}
	public void load_group_people()
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
		clean();
		adapter.arraylist.clear();
		adapter.notifyDataSetChanged();
		dialog=new ProgressDialog(Activity_group_people.this,R.style.DialogTheme);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("Loading group people.....");
		dialog.show();
		t=new Thread(new Runnable() {
			@Override
			public void run() {
				final Response result=Class_group_friend_loader.load_friends(friends_objects, var_username, var_phone, var_group_id);
				if(result.bool) {
					int total_friends = friends_objects.size();
					for(int i = 0;i<total_friends;i++)
					{
						adapter.addItem(friends_objects.get(i));
					}
					handler.post(new Runnable() {

						@Override
						public void run() {
							dialog.dismiss();
							adapter.notifyDataSetChanged();
						}
					});
				}
				else
				{
					handler.post(new Runnable() {

						@Override
						public void run() {
							dialog.dismiss();
							Alert_ok.show(Activity_group_people.this,result.message);
							add_button();
						}
					});
				}
			}
		});
		t.start();

	}

	@Override
	protected void onStart() 
	{
		super.onStart();

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_groups, menu);
		if(!var_admin_phone.equals(var_phone))
		{
			menu.getItem(1).setVisible(false);
		}
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		
		if(item.getItemId()==R.id.action_group_refresh)
		{
			load_group_people();
		}
		 if(item.getItemId()==R.id.action_group_add)
		{

			get_new_person();
		}
		if(item.getItemId()==R.id.action_map)
		{
			Intent intent=new Intent(getApplicationContext(),Activity_show_people.class);
			Bundle bundle=new Bundle();
			bundle.putString("username", var_username);
			bundle.putString("phone", var_phone);
			bundle.putString("group_name",var_group_name);
			bundle.putString("group_id",var_group_id);
			bundle.putString("group_admin_username", var_admin_username);
			bundle.putString("group_admin_phone",var_admin_phone);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		if(item.getItemId()==android.R.id.home)
		{
			finish();
		}
		
		return super.onMenuItemSelected(featureId, item);
		
	}
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void get_new_person()
	{
		Toast.makeText(getApplicationContext(), "New person is going to be added", Toast.LENGTH_SHORT).show();
		Intent i=new Intent(this,Activity_pic_contact.class);
		Bundle bundle=new Bundle();
		bundle.putString("username", var_username);
		bundle.putString("phone",var_phone);
		i.putExtras(bundle);
		startActivityForResult(i,0,bundle);
	}
	private void add_new_person(final String phone_num,final String name)
	{
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						//progressbar.setVisibility(View.VISIBLE);
						
					}
				});
				if(Class_add_new_persorn.add(phone_num, var_group_id)==true)
				{
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							//friends_objects.add(new Class_group_object(name,phone_num,var_group_id));
							
						//	progressbar.setVisibility(View.INVISIBLE);
							Toast.makeText(getApplicationContext(), "added " + name + " into " + var_group_name, Toast.LENGTH_LONG).show();
							
						}
					});
					
					
				}
				else
				{
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "Cannot add the perosn now", Toast.LENGTH_LONG).show();
							Alert_ok.show(this_context, "Unable to add this person now.\nPlease try again later");
						}
					});
					
				}
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						load_all_objects();
						
					}
				});
			}
		});
		t.start();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		Bundle bundle;
		if(resultCode==1)
		{
			bundle=data.getExtras();
			final String name=bundle.getString("name");
			final String phone=bundle.getString("phone");
			
			add_new_person(phone,name);	
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void itemLongClick(final Class_group_object object,final int index)
	{
		
		 final CharSequence[] items = {
	                "Remove "+object.group_name+" from "+var_group_name
	        };

	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	       // builder.setTitle("Make your selection");
	        builder.setItems(items, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) {
	                if(item==0)
	                {
	                	//delete_group_member(object,index);
	                	if(var_phone.equals(var_admin_phone))
	                	{
	                		Alert_ok alert=new Alert_ok()
	                		{
	                			@Override
	                			public void ontrue() {
	                				
	                				super.ontrue();
	                				delete_group_member(object,index);
	                			}
	                		};
	                		alert.ok_or_cancel(this_context, "", "Are you sure to remove from group");
	                	}
	                	else
	                	{
	                		Alert_ok.show(this_context, "You are not ADMIN to remove this person");
	                	}
	                	
	                }
	               
	            }
	        });
	        AlertDialog alert = builder.create();
	        alert.show();
	}
	private void delete_group_member(final Class_group_object object,final int index)
	{
		//final Response result;
		//progressbar.setVisibility(View.VISIBLE);
		Toast.makeText(getApplicationContext(), "removing " + object.total_numbers + " from " + var_group_id, Toast.LENGTH_SHORT).show();
		Thread t=new Thread(new Runnable() {
			Response result;
			@Override
			public void run() {
				 result=Class_delete_groupmember.delete(var_group_id,object.total_numbers);	
				
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						if(result.bool)
						{
							adapter.arraylist.remove(index);
							if(object.total_numbers.equals(var_admin_phone)||adapter.arraylist.size()==0)
							{
								adapter.arraylist.clear();
								Toast.makeText(getApplicationContext(), "Admin is removed so group is deleted", Toast.LENGTH_SHORT).show();
								Alert_ok.show(this_context, "This group no longer exist");
								finish();
							}
							else
							{
								
								Toast.makeText(getApplicationContext(), result.message, Toast.LENGTH_SHORT).show();
							}
							
							adapter.notifyDataSetChanged();
						}
						else
						{
							Toast.makeText(getApplicationContext(), result.message, Toast.LENGTH_SHORT).show();
						}
						//progressbar.setVisibility(View.INVISIBLE);
						
					}
				});
			}
		});
		t.start();
		
	}
}
