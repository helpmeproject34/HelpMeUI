package com.helpme.helpmeui;


import com.helpme.json.Class_server_details;
import com.helpme.json.JSONParser;
import com.helpme.json.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Class_login_verifier {

	private static String url= Class_server_details.server_ip+"/account/login";
	//JSONArray result;
	static String success;
	//JSONParser parser;
	static JSONParser parser = new JSONParser();
	public static Response verify(String var_username,String var_phone,String var_password)
	{
		
		Response res=new Response();
		res.bool=false;
		res.message="";
		
		if(Class_server_details.server_on==1)
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("username", var_username));
	        params.add(new BasicNameValuePair("mobile",var_phone));
	        params.add(new BasicNameValuePair("password",var_password));
			
	        JSONObject json = parser.makeHttpRequest(url, "POST", params);
	        try {
	        	success=json.getString("success");
	        	if(success.equals("True"))
	        	{
	        		res.bool=true;
	        		res.message="Login Success !!";
	        	}
	        	else
	        	{
	        		res.bool=false;
	        		res.message="Wrong Credentials";
	        	}
	        		
	        	
			} catch (JSONException e1) {
				res.bool=false;
				res.message="Error occured try again !!";
			}
	        catch(NullPointerException e)
	        {
	        	res.message="Unable to connect \n Please check Internet connection";
	        }
	        catch(Exception e)
	        {
	        	res.message="Unknown Error";
	        }
		}
		else if(Class_server_details.server_on==0)
		{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				
			}
			res.message="login without server";
			int var_rand=(int)(Math.random()*100);
			res.bool=false;
			if(var_rand>80)
			{
				res.bool=true;
				
			}
		}
		
		return res;
	}
	
	
}
