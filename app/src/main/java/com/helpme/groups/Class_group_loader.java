package com.helpme.groups;


import com.helpme.json.Class_server_details;
import com.helpme.json.JSONParser;
import com.helpme.json.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Class_group_loader {
	static JSONParser parser = new JSONParser();
	public static Response get_group_names(String username,String phone,ArrayList<Class_group_object> group_objects)
	{
		Response result=new Response();
		result.bool=false;
		result.value=-1;
		group_objects.clear(); // first clear all the objects from array list
		
		if(Class_server_details.server_on==1)
		{
			
			String url= Class_server_details.server_ip+"/checkgroup";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			
			params.add(new BasicNameValuePair("username",username));
			params.add(new BasicNameValuePair("mobile",phone));
			
			result.message="check your internet connection";
			try {
				JSONObject json = parser.makeHttpRequest(url, "POST", params);
				
				String response=json.getString("success");
				if(response.equals("False")||response.equals("")||response==null)
				{
					result.value=0;
					result.message="No groups to load\n please create a group";
				}
				else
				{
					result.message="All groups loaded";
					String[] comma_sep=response.split(",");
		        	int len=comma_sep.length;
		        	for(int j=0;j<len;j++)
		        	{
		        		String[] split=comma_sep[j].split(":");
		        		if(split.length<4)
		        		{
		        			group_objects.add(new Class_group_object(split[0],"Group id is : null","-1","-1","-1"));
		        			result.message="Unexpected response from server";
		        		}
		        		else
		        		{
		        			if(username.equals(split[2]))
		        			{
		        				group_objects.add(new Class_group_object(split[0],"You created this group",split[1],split[2],split[3]));
		        			}
		        			else
		        			{
		        				group_objects.add(new Class_group_object(split[0],"You are a member of this group\nand admin is "+split[2],split[1],split[2],split[3]));
		        			}
		        			
		        		}
		        		result.value=1;
		        	}
		        	if(len==0)
		        	{
		        		result.message="NO groups to load \n please Create one group";
		        		result.value=0;
		        	}
		        	result.bool=true;
				}
	        	
	        	
			} catch (JSONException e1) {
				
				result.message="Oops !! error occured";
			}
	        catch(NullPointerException e)
	        { 	
	        	result.message="Unable to connect to server. Please check your network connection";
	        }
	        catch(Exception e)
	        {
	        	result.message="Unknown error occured";
	        }
		}
		else
		{
			group_objects.add(new Class_group_object("group 1","Group id is : "+"random","0"));
			group_objects.add(new Class_group_object("group 2","Group id is : "+"random","0"));
			group_objects.add(new Class_group_object("group 3","Group id is : "+"random","0"));
		}
		return result;
	}
}
