package com.helpme.settings;

import android.content.ContentResolver;

import com.helpme.contacts.Class_give_name;
import com.helpme.json.Class_server_details;
import com.helpme.json.JSONParser;
import com.helpme.json.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Class_load_tracklist {

	static JSONParser parser = new JSONParser();
	public static Response load(ArrayList<Class_tracklist_object> list,String[] phones,String user_phone,ContentResolver resolver)
	{
		int count;
		Response result=new Response();
		result.message="";
		
		/*int length=phones.length;
		for(int j=0;j<length;j++)
    	{
			
			list.add(new Class_tracklist_object(phones[j],Class_give_name.give(phones[j], resolver),true));
    	}
		result.bool=true;*/
		if(Class_server_details.server_on==1)
		{
			result.bool=false;
			list.clear();
			String url= Class_server_details.server_ip+"/blockcheck";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("mobile",user_phone));
			int i=0;
			count=phones.length;
			for(i=0;i<count;i++)
			{
				params.add(new BasicNameValuePair(i+"",phones[i]));
			}
			try 
			{
				JSONObject json = parser.makeHttpRequest(url, "POST", params);
				String response=json.getString("success");
	        	String[] comma_sep=response.split(",");
	        	int len=comma_sep.length;
	        	for(int j=0;j<len;j++)
	        	{
	        		String[] split=comma_sep[j].split(";");
	        		if(split[1].equals("1"))
	        		{
	        			if(split[2].equals("0"))
		        		{
	        				list.add(new Class_tracklist_object(split[0],Class_give_name.give(split[0], resolver),true));
		        		}
		        		else
		        		{
		        			list.add(new Class_tracklist_object(split[0],Class_give_name.give(split[0], resolver),false));
		        		}
	        		}
	        	}
	        	result.message="Loading completed";
	        	if(len==0)
	        	{
	        		result.message=result.message+"\n"+"No phone numbers in your mobile";
	        	}
	        	result.bool=true;
	        	
			} catch (JSONException e1) {
				//Log.d("exception", "json exception in class_load_tracklist");
				result.message="json error occured";
			}
	        catch(NullPointerException e)
	        {
	        	//Log.d("exception", "null pointer exception in class_load_tracklist");
	        	result.message="No response from server";
	        }
	        catch(Exception e)
	        {
	        	//Log.d("exception", "unknown exception in class_load_tracklist");
	        	result.message="unknown error "+e.getMessage();
	        }
		}
		else if(Class_server_details.server_on==0)
		{
			result.message="server connection is disabled in application";
			result.bool=true;
		}
		
		return result;
	}
}
