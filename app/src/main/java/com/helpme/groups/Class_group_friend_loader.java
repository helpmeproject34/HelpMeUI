package com.helpme.groups;

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

public class Class_group_friend_loader {

	static JSONParser parser = new JSONParser();
	public static Response load_friends(ArrayList<Class_group_object> friends_objects,String username,String phone,String groupid)
	{
		Response result=new Response();
		result.bool=false;
		result.value=0;
		if(Class_server_details.server_on==1)
		{
			friends_objects.clear();
			String url= Class_server_details.server_ip+"/groupmembers";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("groupid",groupid));
			
			try {
				JSONObject json = parser.makeHttpRequest(url, "POST", params);
				String response=json.getString("success");
	        	String[] comma_sep=response.split(",");
	        	int len=comma_sep.length;
				result.value=-1;
	        	for(int j=0;j<len;j++)
	        	{
	        		String[] split=comma_sep[j].split(";");
	        		if(split.length!=5)
	        		{
	        			friends_objects.add(new Class_group_object(split[0],"split 1","split 2"));
						result.value=-1;
						result.bool=false;
	        		}
	        		else
	        		{
						if(username.equals(split[0]))
						{
							Log.e("username_matched", split[0]);
							result.value=0;
							result.bool=true;
						}
	        			friends_objects.add(new Class_group_object(split[0],split[1],split[2]));

	        		}

	        	}

	        	
			} catch (JSONException e) {
				//Log.d("exception", "json exception from group_friend_loader " + e.getMessage());
				result.message="json exception from group_friend_loader ";
			}
	        catch(NullPointerException e)
	        {
	        	//Log.d("exception", "null pointer exception from group_friend_loader " + e.getMessage());
				result.message="null pointer exception from group_friend_loader ";
	        }
			catch(ArrayIndexOutOfBoundsException e)
			{
				result.message="out of bound exception from group_friend_loader ";
				//Log.d("exception", "out of bound exception from group_friend_loader " + e.getMessage());
			}
	        catch(Exception e)
	        {
				result.message="unknown exception from group_friend_loader ";
	        	//Log.d("exception", "unknown exception from group_friend_loader " + e.getMessage());
	        }
		}
		else if(Class_server_details.server_on==0)
		{
			
		}
		return result;
	}
	
}
