package com.helpme.helpmeui;

import android.content.Context;
import android.database.Cursor;

import com.helpme.databases.Db_functions;


public class Class_alreadyLogin {
	
	 public static String username;
	 public static String phone;
	 public static boolean islogin;
	 public static String email;
	public static Boolean login_or_not(Context context)
	{
		Db_functions funcs=new Db_functions(context);
		boolean result=true;
		
		Cursor cursor=funcs.read_table_prev_login();
		if(cursor.moveToFirst())
		{
			cursor.moveToLast();
			username=cursor.getString(1);
			phone=cursor.getString(2);
			email=cursor.getString(3);
			//Toast.makeText(context, "username and  phone are already found\n"+username+"\n"+phone,3000);
		}
		else
		{
			result=false;
		}
		
		funcs.close_all();
		islogin=result;
		return result;
	}
}
