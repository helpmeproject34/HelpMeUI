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

public class Class_locations_provider {
	static JSONParser parser = new JSONParser();
	static Double latitude;
	static Double longitude;
	public static Response provide_locations(ArrayList<Class_locations> locations,String groupid,String username)
	{
		Response result=new Response();
		result.value=-1;
		result.message="Unable to load\nPlease try again";
		if(Class_server_details.server_on==1)
		{
			String url= Class_server_details.server_ip+"/groupmembers";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("groupid",groupid));
			
			try {
				JSONObject json = parser.makeHttpRequest(url, "POST", params);
				String response=json.getString("success");
	        	String[] comma_sep=response.split(",");
	        	int len=comma_sep.length;
	        	boolean isremoved=true;
	        	for(int j=0;j<len;j++)
	        	{
	        		String[] split=comma_sep[j].split(";");
	        		if(split[0].equals("False")||split.length!=5)
	        		{
	        			//locations.add(new Class_locations("Group does not exist","","","",""));
	        			result.message="This group no longer exists";
	        			result.value=-2;
	        		}
	        		else
	        		{
						latitude=Double.parseDouble(split[2]);
						longitude=Double.parseDouble(split[3]);
	        			locations.add(new Class_locations(split[0],split[1],latitude,longitude,split[4]));
	        			if(split[0].equals(username))
	        			{
	        				isremoved=false;
	        			}
	        		}
	        	}
	        	result.message="Loaded all locations";
	        	if(isremoved)
	        	{
	        		result.message="You have been removed from the group";
	        		result.value=-2;
	        	}
	        	
			} catch (JSONException e1) {
				//Log.d("exception", "json exception from location provider");
				
			}
	        catch(NullPointerException e)
	        {
	        	//Log.d("exception", "null pointer exception from location provider "+e.getMessage());
	        }
			catch(ArrayIndexOutOfBoundsException e)
			{
				//Log.d("exception", "out of bound exception from location provider "+e.getMessage());
			}
	        catch(Exception e)
	        {
	        	//Log.d("exception", "unknown exception from location provider "+e.getMessage());
	        }
		}
		else if(Class_server_details.server_on==0)
		{
			
		}
		return result;
	}
}
