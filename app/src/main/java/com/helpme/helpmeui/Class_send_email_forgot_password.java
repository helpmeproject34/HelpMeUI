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

/**
 * Created by HARINATHKANCHU on 27-03-2016.
 */
public class Class_send_email_forgot_password {
    static JSONParser parser = new JSONParser();
    public static Response change(String email)
    {
        Response result=new Response();
        result.bool=false;

        if(Class_server_details.server_on==1)
        {

            String url= Class_server_details.server_ip+"/forgot_password";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email",email));

            try {
                JSONObject json = parser.makeHttpRequest(url, "POST", params);
                String response=json.getString("success");
                String message=json.getString("message");
                if(response.equals("True"))
                {
                    result.bool=true;

                }
                else
                {

                }
                result.message=message;
            } catch (JSONException e) {

                result.message="json exception occured";
            }
            catch(NullPointerException e)
            {
                //Log.d("exception", "null pointer exception from group_friend_loader " + e.getMessage());
                result.message="null pointer exception ";
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                result.message="out of bound exception ";
                //Log.d("exception", "out of bound exception from group_friend_loader " + e.getMessage());
            }
            catch(Exception e)
            {
                result.message="unknown exception ";
                //Log.d("exception", "unknown exception from group_friend_loader " + e.getMessage());
            }
        }

        return result;
    }
}
