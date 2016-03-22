package com.helpme.json;

public class Class_server_error_codes {

	public static String give_error(String status)
	{
		String result="";
		if(status.equals("200"))
		{
			result="OK";
		}
		else if(status.equals("201"))
		{
			result="CREATED";
		}
		else if(status.equals("202"))
		{
			result="ACCEPTED";
		}
		else if(status.equals("203"))
		{
			result="PARTIAL INFORMATION";
		}
		else if(status.equals("204"))
		{
			result="NO RESPONSE";
		}
		else if(status.equals("400"))
		{
			result="BAD REQUEST";
		}
		else if(status.equals("401"))
		{
			result="UNAUTHORIZED";
		}
		else if(status.equals("402"))
		{
			result="PAYMENT REQUIRED";
		}
		else if(status.equals("403"))
		{
			result="FORBIDDEN";
		}
		else if(status.equals("404"))
		{
			result="NOT FOUND";
		}
		else if(status.equals("500"))
		{
			result="INTERNAL SERVER ERROR";
		}
		else if(status.equals("501"))
		{
			result="NOT IMPLEMENTED";
		}
		
		else if(status.equals("502"))
		{
			result="SERVICE TEMPERORILY OVERLOADED";
		}
		else if(status.equals("503"))
		{
			result="GATEWAY TIMEOUT";
		}
		else if(status.equals("301"))
		{
			result="MOVED";
		}
		else if(status.equals("302"))
		{
			result="FOUND";
		}
		else if(status.equals("303"))
		{
			result="METHOD";
		}
		else if(status.equals("304"))
		{
			result="NOT MODIFIED";
		}
		return result;
	}
}
