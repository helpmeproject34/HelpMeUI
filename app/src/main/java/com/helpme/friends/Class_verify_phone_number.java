package com.helpme.friends;

import android.util.Log;

import com.helpme.json.Class_server_details;
import com.helpme.json.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Class_verify_phone_number {
	
	static JSONParser parser = new JSONParser();
	
	
	public static String[] verification(String[] phone,int count)
	{
		String[] result=new String[count+1];
		
		
		
		if(Class_server_details.server_on==1)
		{
			///Log.d("exception", "phone 0 is "+phone[0]);
			//Log.d("exception","count is :"+count);
			String url= Class_server_details.server_ip+"/account/checknumber";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			int i=0;
		
			for(i=0;i<count;i++)
			{
				params.add(new BasicNameValuePair(i+"",phone[i]));
				
				result[i]=0+"";
				
			}
			result[count]=0+"";
			//params.add(new BasicNameValuePair("count",count+""));
			
			try {
				JSONObject json = parser.makeHttpRequest(url, "POST", params);
				String response=json.getString("success");
				//Log.d("exception","respnse is "+ response);
				if(response.equals("False"))
				{
					Log.d("exception", "false recieved");
				}
				else if(response!=null&&response.length()!=0)
				{
					String[] comma_sep=response.split(",");
		        	int len=comma_sep.length;
		        	for(int j=0;j<len;j++)
		        	{
		        		String[] split=comma_sep[j].split(":");
		        		phone[j]=split[0];
		        		result[j]=split[1];
		        	}
		        	result[count]=1+"";
				}
				else
				{
					result[count]=1+"";
				}
	        	
	        	
			} catch (JSONException e1) {
				Log.d("exception", "json exception in class verify phone number");
			}
	        catch(NullPointerException e)
	        {
	        	Log.d("exception", "null pointer exception class verify phone number");
	        }
	        catch(Exception e)
	        {
	        	Log.d("exception", "unknown exception class verify phone number " + e.getMessage());
	        }
		}
		else if(Class_server_details.server_on==0)
		{
			int i=0;
			for(i=0;i<count;i++)
			{
				result[i]="1";	
			}
			result[count]=1+"";
		}
		
		return result;
	}
	/*public static boolean verification(String phone)
	{
		boolean result=true;
		return result;
	}*/
}
