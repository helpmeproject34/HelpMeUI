package com.helpme.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import java.util.ArrayList;
import java.util.HashMap;

public class Class_give_phones {

	public static String[] give(ContentResolver resolver)
	{
		Cursor cursor_phones=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
		ArrayList<String> list=new ArrayList<String>();
		HashMap hashmap=new HashMap();
		String[] phones=null;
		int i=0;
		if(cursor_phones.moveToFirst())
		{
			while(cursor_phones.moveToNext())
			{
				String phone_num = cursor_phones.getString(cursor_phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				phone_num=Class_purify_phone_number.purify(phone_num.trim());
			
				if(phone_num.trim().length()==10)
				{
					
					if(!hashmap.containsKey(phone_num))
					{
						hashmap.put(phone_num, null);
						//phones[i]=phone_num;
						list.add(phone_num);
						i++;
					}
				}
			}
			 phones=new String[i];
			 i=0;
			 int j=list.size();
			for(i=0;i<j;i++)
			{
				phones[i]=list.get(i);
			}
			cursor_phones.close();
		}
		else
		{
			phones=new String[0];
		}
		return phones;
	}
}
