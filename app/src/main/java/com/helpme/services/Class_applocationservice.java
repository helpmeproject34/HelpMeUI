package com.helpme.services;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.helpme.helpmeui.Activity_login;
import com.helpme.helpmeui.Class_alreadyLogin;
import com.helpme.helpmeui.R;
import com.helpme.json.Class_server_details;
import com.helpme.json.JSONParser;
import com.helpme.settings.Activity_settings;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Class_applocationservice extends Service implements LocationListener {

	protected LocationManager locationManager;
	Location location;
	JSONParser parser;
	private  long MIN_DISTANCE_FOR_UPDATE = 3;
	private long MIN_TIME_FOR_UPDATE = 1000;
	Handler handler;
	Thread t;
	public Class_applocationservice () {

	}

	@Override
	public boolean stopService(Intent name) {
		Toast.makeText(getApplicationContext(),"gps service is stopped",Toast.LENGTH_SHORT).show();
		if(locationManager!=null)
		{
			locationManager.removeUpdates(this);
		}
		return super.stopService(name);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(getApplicationContext(),"gps service is destroyed",Toast.LENGTH_SHORT).show();
		if(locationManager!=null)
		{
			locationManager.removeUpdates(this);
		}
		stopSelf();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {


		send_notifications();
		parser=new JSONParser();
		handler=new Handler();
		locationManager = (LocationManager) getApplicationContext()
				.getSystemService(LOCATION_SERVICE);
		Toast.makeText(getApplicationContext(),"gps service started",Toast.LENGTH_SHORT).show();

		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		String best = locationManager.getBestProvider(crit, false);
		locationManager.requestLocationUpdates(best, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
		//return super.onStartCommand(intent, flags, startId);
		return START_REDELIVER_INTENT;
	}

	public Location getLocation() {
		Location location_2=null;
		List<String> providers = locationManager.getProviders(true);
		Location bestLocation = null;
		for (String provider : providers)
		{
			location_2 = locationManager.getLastKnownLocation(provider);
			if (location_2 == null) {
				continue;
			}
			if (bestLocation == null || location_2.getAccuracy() < bestLocation.getAccuracy()) {
				// Found best last known location: %s", l);
				bestLocation = location_2;
			}
		}
		return bestLocation;

	}

	@Override
	public void onLocationChanged(final Location changed_loc) {


		if(Class_alreadyLogin.islogin)
		{
			if(t!=null)
			{
				if(!t.isAlive())
				{
					Toast.makeText(getApplicationContext(),"Location changed",Toast.LENGTH_SHORT).show();
					final Location loc=getLocation();
					t=new Thread(new Runnable() {
						@Override
						public void run() {
							send(loc);
						}
					});
					t.start();
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(),"Location changed",Toast.LENGTH_SHORT).show();
				final Location loc=getLocation();
				t=new Thread(new Runnable() {
					@Override
					public void run() {
						send(loc);
					}
				});
				t.start();
			}


		}


	}
	private void send(Location loc)
	{
		String var_username= Class_alreadyLogin.username;
		String var_phone= Class_alreadyLogin.phone;

		final String latitude;//=Math.random()*100+"";
		final String longitude;//=Math.random()*100+"";
		if(loc!=null)
		{
			latitude=loc.getLatitude()+"";
			longitude=loc.getLongitude()+"";
		}
		else
		{
			latitude=-1+"";
			longitude=-1+"";
		}


		if(loc!=null)
		{

			String url= Class_server_details.server_ip+"/account/location";

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", var_username));
			params.add(new BasicNameValuePair("mobile",var_phone));
			params.add(new BasicNameValuePair("latitude",latitude));
			params.add(new BasicNameValuePair("longitude",longitude));

			JSONObject json = parser.makeHttpRequest(url, "POST", params);
			if(json==null)
			{
				return ;
			}
			try {
				final int success;
				String success_value=json.getString("success");
				if(success_value.equals("True"))
				{
					success=1;
				}
				else
				{
					success=0;
				}
	        	/*handler.post(new Runnable() {

					@Override
					public void run() {

						if(success==1)
			        	{
			        		Toast.makeText(getApplicationContext(), "success=1", Toast.LENGTH_SHORT).show();

			        	}
			        	else
			        	{
			        		Toast.makeText(getApplicationContext(), "success=0", Toast.LENGTH_SHORT).show();
			        	}
					}
				});*/



			} catch (JSONException e1) {

				handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(),"JSON exception", Toast.LENGTH_SHORT).show();
					}
				});

			}
			catch(NullPointerException e)
			{
				handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(),"NULL ptr exception", Toast.LENGTH_SHORT).show();
					}
				});
			}
			catch(Exception e)
			{
				handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(),"Some exception ", Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
		else
		{
				handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(),"NULL location recieved", Toast.LENGTH_SHORT).show();
					}
				});
		}
	}
	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(getApplicationContext(),"Providers disabled",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(getApplicationContext(),"providers enabled",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	private void send_notifications()
	{

		if(Class_alreadyLogin.login_or_not(getApplicationContext()))
		{
			NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
			builder.setSmallIcon(R.drawable.logo_big);
			builder.setContentTitle("HELP ME !!");
			builder.setContentText("tracing your location\n"+"min-dist:"+MIN_DISTANCE_FOR_UPDATE+"\nmin-time:"+MIN_TIME_FOR_UPDATE);
			//Intent resultIntent=new Intent(getApplicationContext(),Activity_welcome.class);
			//TaskStackBuilder stackBuilder=TaskStackBuilder.create(getApplicationContext());
			//stackBuilder.addParentStack(Activity_login.class);
			//stackBuilder.addNextIntent(resultIntent);
			//builder.setContentIntent(resultIntent);

			Intent backIntent = new Intent(getApplicationContext(), Activity_login.class);
			backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			Intent intent = new Intent(getApplicationContext(), Activity_settings.class);
			intent.putExtra("username", Class_alreadyLogin.username);
			intent.putExtra("phone",Class_alreadyLogin.phone);
			final PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 2,
					new Intent[] {backIntent, intent}, PendingIntent.FLAG_ONE_SHOT);
			builder.setContentIntent(pendingIntent);
			NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(1,builder.build());
		}


	}

}