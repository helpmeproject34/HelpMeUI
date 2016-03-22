package com.helpme.tracking;

import java.nio.charset.Charset;

/**
 * Created by HARINATHKANCHU on 02-02-2016.
 */
public class Class_purify_string_for_long {
    public  static String purify(String s)
    {
        Charset.forName("UTF-8").encode(s);
        String result="";

        int length=s.length();

        for(int i=0;i<length;i++)
        {

            if(Character.isDigit(s.charAt(i))||s.charAt(i)=='.')
            {
                result=result+s.charAt(i);
            }
        }

        return result;
    }
}
