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

public class Class_delete_groupmember {

		static JSONParser parser = new JSONParser();
		
		public  static Response delete(String groupid,String phone)
		{
			Response result=new Response();
			result.value=-1;
			String message="";
			result.bool=false;
			if(Class_server_details.server_on==1)
			{
				
				String url= Class_server_details.server_ip+"/deletemember";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				
				params.add(new BasicNameValuePair("mobile",phone));
				params.add(new BasicNameValuePair("groupid",groupid));
				
				try {
					
					JSONObject json = parser.makeHttpRequest(url, "POST", params);
					
					String response=json.getString("success");
		        	if(response.equals("True"))
		        	{
		        		result.value=1;
		        		message="Successfully deleted user";
		        		result.bool=true;
		        	}
		        	else
		        	{
		        		message="Not possible to delete";
		        		result.value=0;
		        	}
		        	
		        	
				} catch (JSONException e1) {
					
					message="json exception occured";
				}
		        catch(NullPointerException e)
		        {
		        	
		        	message="no response from server";
		        	
		        }
		        catch(Exception e)
		        {
		        	
		        	message=message+"unknown error occured \n"+e.getMessage();
		        	
		        }
			}
			else
			{
				message="deleting member without server";
			}
			result.message=message;
			return result;
		}
}
