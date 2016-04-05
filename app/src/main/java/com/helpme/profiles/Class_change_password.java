package com.helpme.profiles;

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
public class Class_change_password {
    static JSONParser parser = new JSONParser();
    public static Response change(String current_password,String new_password,String username)
    {
        Response result=new Response();
        result.bool=false;
        result.message="no message";
        if(Class_server_details.server_on==1)
        {
            String url=Class_server_details.server_ip+"/account/changepassword";
            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair("current_password",current_password));
            params.add(new BasicNameValuePair("new_password",new_password));
            params.add(new BasicNameValuePair("username",username));
            try
            {
                JSONObject json = parser.makeHttpRequest(url, "POST", params);
                String response=json.getString("success");
                String message=json.getString("message");
                if(response.equals("True"))
                {
                    result.message="Successfully saved your details";
                    result.bool=true;
                }
                else
                {
                    result.message="Password change failed";
                    result.bool=false;
                }
                result.message=message;

            }
            catch(JSONException e)
            {
                result.message="Json exception occured";
            }
            catch(NullPointerException e)
            {
                result.message="null pointer exception occured";
            }
            catch(Exception e)
            {
                result.message="Unkonwn exception occured";
            }

        }
        return result;
    }
}
