package com.helpme.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;

public class Class_give_name {
	
	
	public  static String give(String phone,ContentResolver resolver)
	{
		String name="";
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
		Cursor cursor_names = resolver.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
		if(cursor_names.moveToFirst())
		{
			while(cursor_names.moveToNext())
			{
				name=cursor_names.getString(cursor_names.getColumnIndex(PhoneLookup.DISPLAY_NAME));
					
			}
		}
		cursor_names.close();
		return name;
	}
	
}
