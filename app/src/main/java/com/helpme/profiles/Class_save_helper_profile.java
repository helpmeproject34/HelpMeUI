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
 * Created by HARINATHKANCHU on 24-03-2016.
 */
public class Class_save_helper_profile {
    static JSONParser parser = new JSONParser();
    public static Response save(Class_profile_object object,String postal_code)
    {
        Response result=new Response();
        result.bool=false;
        result.message="no message";
        if(Class_server_details.server_on==1)
        {
            String url=Class_server_details.server_ip+"/helper/profile";
            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair("username",object.username));
            params.add(new BasicNameValuePair("working_name",object.working_name));
            params.add(new BasicNameValuePair("working_mobile",object.working_phone));
            params.add(new BasicNameValuePair("category",object.category+""));
            params.add(new BasicNameValuePair("latitude",object.latitude+""));
            params.add(new BasicNameValuePair("longitude",object.longitude+""));
            params.add(new BasicNameValuePair("from",object.from+""));
            params.add(new BasicNameValuePair("to",object.to+""));
            params.add(new BasicNameValuePair("working_days",object.working_days));
            params.add(new BasicNameValuePair("postal_code",postal_code));

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
