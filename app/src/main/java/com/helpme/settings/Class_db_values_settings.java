package com.helpme.settings;

import android.content.Context;
import android.database.Cursor;

import com.helpme.databases.Db_functions;

/**
 * Created by HARINATHKANCHU on 22-03-2016.
 */
public class Class_db_values_settings {
    public static boolean is_dnd(Context context)
    {
        boolean result=false;
        Db_functions funcs=new Db_functions(context);
        Cursor cursor=funcs.read_table_prev_login();
        if(cursor.moveToFirst())
        {
            cursor.moveToLast();
            int dnd_value=cursor.getInt(3);
            if(dnd_value==1)
            {
                result= true;
            }

        }
        funcs.close_all();
        return result;
    }

    public static int give_dnd(Context context)
    {
        int result=-1;
        Db_functions funcs=new Db_functions(context);
        Cursor cursor=funcs.read_table_prev_login();
        if(cursor.moveToFirst())
        {
            cursor.moveToLast();
            result=cursor.getInt(3);
        }
        funcs.close_all();
        return result;

    }
    public static int give_accuracy(Context context)
    {
        int result=-1;
        Db_functions funcs=new Db_functions(context);
        Cursor cursor=funcs.read_table_prev_login();
        if(cursor.moveToFirst())
        {
            cursor.moveToLast();
            result=cursor.getInt(4);
        }
        funcs.close_all();
        return result;
    }
}
