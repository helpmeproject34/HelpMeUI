package com.helpme.tracking;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.helpme.alert_dialogs.Alert_ok;
import com.helpme.bimaps.Bitmap_editor;
import com.helpme.friends.Class_check_tracking;
import com.helpme.groups.Class_locations;
import com.helpme.helpmeui.R;
import com.helpme.json.Response;

public class Activity_tracking_friend extends ActionBarActivity implements OnMapReadyCallback {

	Bundle bundle;
	String var_friend_name;
	String var_friend_phone;
	String var_username;
	String var_phone;
	Class_locations location;
	Context context;
	boolean destroyed;
	Handler handler;
	private GoogleMap mMap=null;
	Alert_ok alert;
	Thread t;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

		Intent var_intent_received=getIntent();
		 if(var_intent_received==null||var_intent_received.getExtras()==null)
		 {
			 Toast.makeText(getApplicationContext(), "Illeagal opening of activity", Toast.LENGTH_SHORT).show();
			 finish();
		 }
		 else
		 {
			 bundle=var_intent_received.getExtras();
			 var_username=bundle.getString("username");
			 var_phone=bundle.getString("phone");
			 var_friend_name=bundle.getString("friend_name");
			 var_friend_phone=bundle.getString("friend_phone");
			 location=new Class_locations();
			 context=this;
			 handler=new Handler();


		 }
		getSupportActionBar().setTitle("Tracking "+var_friend_name);
		if(services_ok())
		{
			setContentView(R.layout.fragment_tracking_friend);
			SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragment_tracking_friend);
			mapFragment.getMapAsync(this);
		}
		else
		{
			Toast.makeText(getApplicationContext(),"Your device don't have google play services.Please install",Toast.LENGTH_SHORT).show();
			finish();

		}
	}
	@Override
	protected void onDestroy() {

		super.onDestroy();
		destroyed=true;


	}
	@Override
	protected void onStop() {
		super.onStop();
		destroyed=true;

	}
	@Override
	protected void onStart() {

		super.onStart();
		destroyed=false;
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

				while(!destroyed)
				{
					refresh();

					try {
						Thread.sleep(10000);
					} catch (final InterruptedException e) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								Alert_ok.show(context, "Interrupeed exception occured in thread\n" + e.getMessage());
							}
						});

					}
				}
			}
		});
		t.start();

	}
	private void refresh()
	{
		
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				final Response result= Class_check_tracking.check(var_phone, var_friend_phone, var_friend_name, location);
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						if(result.bool==true)
						{
							//Toast.makeText(context, "Updated now....", Toast.LENGTH_SHORT).show();
							if(services_ok())
							{
								if(mMap!=null)
								{
									try
									{
										Double latitude= location.latitude;
										Double longitude= location.longitude;
										if(latitude==-1&&longitude==-1)
										{
											mMap.clear();
											if(alert==null)
											{
												alert=new Alert_ok();
												Alert_ok.show(Activity_tracking_friend.this,"Location unavailable\nSuggest your friend to turn on GPS.");
											}
											Toast.makeText(Activity_tracking_friend.this,"Suggest your friend to turn on GPS.",Toast.LENGTH_SHORT).show();
										}
										else
										{
											alert=null;
											mMap.clear();
											LatLng sydney = new LatLng(latitude,longitude);
											mMap.addMarker(new MarkerOptions().position(sydney).title("Last seen time:").snippet(location.last_updated).icon(BitmapDescriptorFactory.fromBitmap(Bitmap_editor.drawTextToBitmap(getApplicationContext(), R.drawable.marker_tracking, " " + var_friend_name + " "))));
											mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18.0f));
										}
										// Add a marker in Sydney and move the camera

									}
									catch(Exception e)
									{
										Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
									}

								}
							}
						}
						else
						{
							if(result.value==0)
							{
								Alert_ok.show(context, result.message);
							}
							else if(result.value==-1)
							{
								//Alert_ok.show(context, result.message);
								Toast.makeText(context,result.message,Toast.LENGTH_SHORT).show();
								finish();
							}
							
						}
					}
				});
				
			}
		});
		t.start();
	}
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.menu_refresh, menu);
		return true;
	}
	private boolean services_ok()
	{
		boolean result=false;
		int isavail= GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if(isavail== ConnectionResult.SUCCESS)
		{
			result=true;
		}
		else if(GooglePlayServicesUtil.isUserRecoverableError(isavail))
		{
			Dialog dialog= GooglePlayServicesUtil.getErrorDialog(isavail, this, 9001);
			dialog.show();
		}
		else
		{
			Alert_ok.show(getApplicationContext(), "Google play services are not availabe");
		}
		return result;
	}
	@Override
	public void onMapReady(GoogleMap googleMap) {
		Toast.makeText(this, "maps ready", Toast.LENGTH_SHORT).show();
		       mMap = googleMap;
		//Long latitude= Long.valueOf(location.latitude);
		//Long longitude=Long.valueOf(location.longitude);
		// Add a marker in Sydney and move the camera
		//mMap.clear();
		//LatLng sydney = new LatLng(-34, 151);
		//mMap.addMarker(new MarkerOptions().position(sydney).title(location.last_updated));
		//mMap.setMapType();
		//mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
	//	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
	}

}
