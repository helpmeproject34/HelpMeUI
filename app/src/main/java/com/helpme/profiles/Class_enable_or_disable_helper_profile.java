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
 * Created by HARINATHKANCHU on 26-03-2016.
 */
public class Class_enable_or_disable_helper_profile {
    static JSONParser parser = new JSONParser();
    public static Response disable(String username,int enable)
    {
        Response result=new Response();
        result.bool=false;
        result.message="no message";
        if(Class_server_details.server_on==1)
        {
            String url=Class_server_details.server_ip+"/helper/profile_enable_or_disable";
            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair("username",username));
            params.add(new BasicNameValuePair("enable",enable+""));
            try
            {
                JSONObject json = parser.makeHttpRequest(url, "POST", params);
                String response=json.getString("success");
                if(response.equals("True"))
                {
                    result.message="Successfully saved your details";
                    result.bool=true;
                }
                else
                {
                    result.message="Not saved your details";
                    result.bool=false;
                }

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
