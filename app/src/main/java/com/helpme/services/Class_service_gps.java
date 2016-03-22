package com.helpme.services;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;

import com.helpme.helpmeui.Class_alreadyLogin;
import com.helpme.json.Class_server_details;
import com.helpme.json.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Class_service_gps extends Service {

	public static Thread t=null;
	public boolean run_service=true;
	Handler handler=new Handler();
	static JSONParser parser = new JSONParser();
	LocationManager location_manager;
	public Location location;
	private static final long MIN_DISTANCE_FOR_UPDATE = 10;
	private static final long MIN_TIME_FOR_UPDATE = 1000 * 30;
	
	//Context context=getApplicationContext();
	
	public Class_service_gps()
	{
		
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		run_service=true;
	}
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		run_service=false;
		t=null;
	}
	@Override
	public boolean stopService(Intent name) {
		// TODO Auto-generated method stub
		run_service=false;
		t=null;
		return super.stopService(name);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		run_service=true;
		location_manager=(LocationManager)(getApplicationContext()).getSystemService(LOCATION_SERVICE);
		Criteria criteria=new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
		location_manager.getBestProvider(criteria, true);
		if(t==null&& Class_alreadyLogin.login_or_not(getApplicationContext()))
		{
			t=new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(run_service&& Class_alreadyLogin.islogin)
					{
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								
								//Toast.makeText(getApplicationContext(), "running", Toast.LENGTH_SHORT).show();
								location=giveLocation();
							}
						});
						send();
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
					}

					
				}
			});
			
			t.start();
		}

		return super.onStartCommand(intent, flags, startId);
	}
	
	private void send()
	{
		String var_username= Class_alreadyLogin.username;
		String var_phone= Class_alreadyLogin.phone;
		
		final String latitude;//=Math.random()*100+"";
		final String longitude;//=Math.random()*100+"";
		if(location!=null)
		{
			latitude=location.getLatitude()+"";
			longitude=location.getLongitude()+"";
		}
		else                                                   
		{
			latitude=-1+"";
			longitude=-1+"";
		}


		if(location!=null)
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
				
			}
	        catch(NullPointerException e)
	        {
	        	
	        }
	        catch(Exception e)
	        {
	        	
	        }
		}
	}
	public Location giveLocation()
	{
		Location location_2=null;
		List<String> providers = location_manager.getProviders(true);
		Location bestLocation = null;
		for (String provider : providers)
		{
			location_2 = location_manager.getLastKnownLocation(provider);
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
}
