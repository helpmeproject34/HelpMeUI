package com.helpme.json;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.List;

public class JSONParser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    public String status_code="";
    // constructor
    public JSONParser() {
    	
    }
 
    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method,
            List<NameValuePair> params) {
 
    	HttpParams hparams=new BasicHttpParams();
  
        // Making HTTP request
    	
        try {
 
            // check for request method
        	
            if(method == "POST"||method.equals("POST")){
                // request method is POST
                // defaultHttpClient
            	
                DefaultHttpClient httpClient = new DefaultHttpClient();
                hparams=httpClient.getParams();
                HttpConnectionParams.setConnectionTimeout(hparams, 10000);
                
              	HttpConnectionParams.setSoTimeout(hparams, 10000);
              	
                HttpPost httpPost = new HttpPost(url);
               
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                
                HttpResponse httpResponse = httpClient.execute(httpPost);
                
                status_code=httpResponse.getStatusLine().getStatusCode()+"";
                HttpEntity httpEntity = httpResponse.getEntity();
               
                is = httpEntity.getContent();
                
 
            }else if(method == "GET"){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
                
                
                hparams=httpClient.getParams();
                HttpConnectionParams.setConnectionTimeout(hparams, 10000);
              	HttpConnectionParams.setSoTimeout(hparams, 10000);
              	
              	
                HttpResponse httpResponse = httpClient.execute(httpGet);
                status_code=httpResponse.getStatusLine().getStatusCode()+"";
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            } 
            
            
        }
        catch(SocketException e)
        {
        	Log.d("exception", "socket " + e.getMessage());
        }
        catch (UnsupportedEncodingException e) {
        	Log.d("exception", "unsupported encoding " + e.getMessage());
           // e.printStackTrace();
        	return null;
        } catch (ClientProtocolException e) {
           // e.printStackTrace();
        	Log.d("exception", "client protocol " + e.getMessage());
        	return null;
        }
        catch (InterruptedIOException e)
        {
        	Log.d("exception", "interrupted io exception " + e.getMessage());
        	return null;
        }
        catch (IOException e) {
           // e.printStackTrace();
        	Log.d("exception", " io exception " + e.getMessage());
        	return null;
        }
        
        catch(Exception e)
       {
    	   Log.d("exception", "unknown exception " + e.getMessage());
       	return null;
       }
        
       
 
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            //Log.e("Buffer Error", "Error converting result " + e.toString());
        	Log.d("exception", "buffered exception in parser " + e.getMessage());
        	return null;
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
           // Log.e("JSON Parser", "Error parsing data " + e.toString());
        	Log.d("exception", "json object conversion excep in parser " + e.getMessage());
        	return null;
        }
        
 
        // return JSON String
      
        return jObj;
 
    }
}
