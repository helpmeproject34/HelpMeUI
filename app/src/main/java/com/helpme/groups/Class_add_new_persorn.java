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

public class Class_add_new_persorn {

	static JSONParser parser = new JSONParser();
	protected static boolean add(String add_phone,String groupid)
	{
		boolean result=true;
		if(Class_server_details.server_on==1)
		{
			String url= Class_server_details.server_ip+"/addgroupmember";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair("mobile",add_phone));
			params.add(new BasicNameValuePair("groupid",groupid));
			
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
				Log.d("exception", "json exception from class_add_person");
				result=false;
			}
	        catch(NullPointerException e)
	        {
	        	Log.d("exception", "null pointer exception from class_add_person");
	        	result=false;
	        }
	        catch(Exception e)
	        {
	        	Log.d("exception", "unknown exception from class_person " + e.getMessage());
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
