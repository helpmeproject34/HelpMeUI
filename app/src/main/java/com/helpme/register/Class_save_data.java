package com.helpme.register;


import com.helpme.json.Class_server_details;
import com.helpme.json.JSONParser;
import com.helpme.json.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Class_save_data {
		public static Response save_data(String username,String email,String phone,String password,String confirm_password)
		{
			
			Response res=new Response();
			res.bool=false;
			res.message="message not set";
	
			if(Class_server_details.server_on==1)
			{
				String url= Class_server_details.server_ip+"/account/signup";
				JSONParser parser=new JSONParser();
				List<NameValuePair> params=new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password",password));
				params.add(new BasicNameValuePair("email",email));
				params.add(new BasicNameValuePair("mobile",phone));
				
				JSONObject result;
				
				
				int result_code=-1;
				try {
					result= parser.makeHttpRequest(url, "POST", params);
					
					
						//result_code=result.getInt("status");
						String message=result.getString("message");
						
						String result_status=result.getString("success");
						
						if(result_status.equals("True"))
						{
							result_code=1;
							message="success";
						}
						else
						{
							JSONObject error_object=result.getJSONObject("message");
							message="";
							if(error_object.has("email"))
							{
								message=message+"email exists already";
							}
							if(error_object.has("username"))
							{
								if(!message.equals(""))
								{
									message=message+"\n";
								}
								message=message+"username exists already";
							}
							if(error_object.has("mobile"))
							{
								if(!message.equals(""))
								{
									message=message+"\n";
								}
								message=message+"mobile exists already";
							}
							//Log.d("json",error_object.toString() );
							
							//message=message+error_object.getString("email")+"\n"+error_object.getString("username");
							//Log.d("email",error_object.getString("email"));
							result_code=0;
						}
						res.message=message;
						
				} catch (JSONException e) {
					res.message="json exception occured";
					result_code=0;
				}
				catch(NullPointerException e)
				{
					res.message="null pointer exception";
					result_code=0;
				}
				catch(Exception e)
				{
					res.message="unknown exception occured";
					result_code=0;
				}
				if(result_code==1)
				{
					res.bool=true;
				}
				else
				{
					res.bool=false;
				}
			}
			else if(Class_server_details.server_on==0)
			{
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					
				}
				
				int random=(int)(Math.random()*100);
				if(random>80)
				{
					res.message="without server - success";
					res.bool=true;
				}
				else
				{
					res.message="without server - failed";
					res.bool=false;
				}
			}
			return res;
		}
}
