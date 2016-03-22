package com.helpme.groups;

import android.util.Log;

import com.helpme.json.Class_server_details;
import com.helpme.json.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Class_add_new_group {

	static JSONParser parser = new JSONParser();
	protected static boolean add_new_group(String username,String phone,String group_name)
	{
		boolean result=true;
		if(Class_server_details.server_on==1)
		{
			String url= Class_server_details.server_ip+"/creategroup";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			
			params.add(new BasicNameValuePair("username",username));
			params.add(new BasicNameValuePair("mobile",phone));
			params.add(new BasicNameValuePair("name",group_name));
			
			try {
				JSONObject json = parser.makeHttpRequest(url, "POST", params);
				String response=json.getString("success");
	        	if(response.equals("True"))
	        	{
	        		result=true;
	        	}
	        	else
	        	{
	        		result=false;
	        	}
	        	
			} catch (JSONException e1) {
				Log.d("exception", "json exception from class_add_group");
				result=false;
			}
	        catch(NullPointerException e)
	        {
	        	Log.d("exception", "null pointer exception from class_add_group");
	        	result=false;
	        }
	        catch(Exception e)
	        {
	        	Log.d("exception", "unknown exception from class_add_group " + e.getMessage());
	        	result=false;
	        }
		}
		else
		{
			result=false;
		}
		return result;
	}
}
