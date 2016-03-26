package com.helpme.helper;

import com.helpme.json.Class_server_details;
import com.helpme.json.JSONParser;
import com.helpme.json.Response;
import com.helpme.profiles.Class_profile_object;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HARINATHKANCHU on 26-03-2016.
 */
public class Class_get_helpers {
    static JSONParser parser = new JSONParser();
    public static Response get(ArrayList<Class_profile_object>helper_objects,String postal_code,int category)
    {
        Response result=new Response();
        result.bool=false;
        result.message="no message";
        if(Class_server_details.server_on==1)
        {
            String url=Class_server_details.server_ip+"/helper/find_helper";
            List<NameValuePair> params=new ArrayList<>();

            params.add(new BasicNameValuePair("postal_code",postal_code));
            params.add(new BasicNameValuePair("category",category+""));

            try
            {
                JSONObject json = parser.makeHttpRequest(url, "POST", params);
                String response=json.getString("success");
                JSONArray array=json.getJSONArray("helper_array");
                for(int i=0;i<array.length();i++)
                {
                    JSONObject obj=array.getJSONObject(i);
                    int from=obj.getInt("end_time");
                    int to=obj.getInt("start_time");
                    int cat=obj.getInt("category");
                    String name=obj.getString("name");
                    Double latitude=obj.getDouble("latitude");
                    Double longitude=obj.getDouble("longitude");
                    String phone=obj.getString("number");
                    String working_days=obj.getString("working_days");
                    helper_objects.add(new Class_profile_object(null,name,phone,cat,latitude,longitude,from,to,working_days));

                }

               if(response.equals("True"))
               {
                   result.bool=true;
                   result.message="Successfully retrieved all helpers";
               }
                else
               {
                   result.bool=false;
                   result.message="Failed";
               }
                if(array.length()==0)
                {
                    result.message="NO HELPERS FOUND IN YOUR LOCALITY !!";
                }

            }
            catch(JSONException e)
            {
                result.message="Json exception occured"+e.getMessage();
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
