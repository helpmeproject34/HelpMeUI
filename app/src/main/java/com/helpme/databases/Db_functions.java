package com.helpme.databases;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class Db_functions {
	
	Dbopenhelper helper;
	static SQLiteDatabase db_r;
	static SQLiteDatabase db_w;
	Context context;
	public Db_functions(Context context)
	{
		helper=new Dbopenhelper(context,"helpme_db",null,3);
		db_r=helper.getReadableDatabase();
		db_w=helper.getWritableDatabase();
		this.context=context;
	}
	public void close_all()
	{
		if(db_r!=null)
		{
			if(db_r.isOpen())
			{
				db_r.close();
			}
		}
		if(db_w!=null)
		{
			if(db_w.isOpen())
			{
				db_w.close();
			}
		}
	}
	public void write_table_prev_login(String username,String phone,int dnd,int accuracy)
	{
		ContentValues cv=new ContentValues();
		cv.put("username",username);
		cv.put("phone", phone);
		cv.put("dnd",dnd);
		cv.put("accuracy",accuracy);
		db_w.insert("table_prev_login", null, cv);
	}
	public static void delete_table_prev_login()
	{
		
		db_w.delete("table_prev_login", null, null);
		
		
	}
	public Cursor read_table_prev_login()
	{
		Cursor cursor=null;
		cursor=db_r.rawQuery("select * from table_prev_login;",null);
		return cursor;
	}
	public Cursor read_table_contacts()
	{
		Cursor cursor=null;
		cursor=db_r.rawQuery("select * from table_contacts;", null);
		
		return cursor;
	}
	public void delete_table_contacts()
	{
		db_w.delete("table_contacts", null,null);
	}
	public void insert_into_table_contact(String name,String phone)
	{
		ContentValues cv=new ContentValues();
		cv.put("name",name);
		cv.put("phone", phone);
		db_w.insert("table_contacts", null, cv);
	}
	public void set_dnd()
	{
		ContentValues cv=new ContentValues();
		cv.put("dnd",1);
		db_w.update("table_prev_login", cv, "", null);
	}
	public void unset_dnd()
	{
		ContentValues cv=new ContentValues();
		cv.put("dnd",0);
		db_w.update("table_prev_login",cv,"",null);
	}
	public void set_accuracy(int val)
	{
		if(val<0||val>3)
		{
			val=2;
		}
		ContentValues cv=new ContentValues();
		cv.put("accuracy",val);
		db_w.update("table_prev_login",cv,"",null);
	}
}
