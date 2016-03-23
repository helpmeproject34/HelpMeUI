package com.helpme.settings;

import android.util.Log;

import com.helpme.json.Class_server_details;
import com.helpme.json.JSONParser;
import com.helpme.json.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Class_save_tracklist {
	static JSONParser parser = new JSONParser();
	public static Response save(ArrayList<String> phone_list,ArrayList<String> block_list,String phone)
	{
		Response result=new Response();
		result.bool=false;
		if(Class_server_details.server_on==1)
		{
			String url= Class_server_details.server_ip+"/blocklist";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("mobile",phone));
			int length=phone_list.size();
			for(int i=0;i<length;i++)
			{
				params.add(new BasicNameValuePair(phone_list.get(i),block_list.get(i)));
			}
			try 
			{
				JSONObject json = parser.makeHttpRequest(url, "POST", params);
				String response=json.getString("success");
	        	if(response.equals("True"))
	        	{
	        		result.message="Successfully saved";
	        		result.bool=true;
	        	}
	        	else
	        	{
	        		result.message="Saving FAILED";
	        		result.bool=false;
	        	}
	        	
			} catch (JSONException e1) {
				Log.d("exception", "json exception in class_load_tracklist");
				result.message="json error occured";
			}
	        catch(NullPointerException e)
	        {
	        	Log.d("exception", "null pointer exception in class_load_tracklist");
	        	result.message="No response from server";
	        }
	        catch(Exception e)
	        {
	        	Log.d("exception", "unknown exception in class_load_tracklist");
	        	result.message="unknown error "+e.getMessage();
	        }
		}
		else
		{
			result.message="Not connected to server\n so saving failed";
    		result.bool=false;
		}
		return result;
	}
}
