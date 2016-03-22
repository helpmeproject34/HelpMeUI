package com.helpme.contacts;

public class Class_purify_phone_number {
	public static String purify(String phone)
	{
		String result="";
		
		int length=phone.length();
		
		for(int i=0;i<length;i++)
		{
			
			if(Character.isDigit(phone.charAt(i)))
			{
				result=result+phone.charAt(i);
			}
		}
		if(result.length()>=10)
		{
			result=result.substring(result.length()-10);
			
		}
		return result;
	}
	
}
