package com.helpme.settings;

public class Class_tracklist_object {

	public String phone;
	public String name;
	public String username;
	public boolean check;
	public Class_tracklist_object(String name, String phone, boolean check)
	{
		this.name=name;
		this.phone=phone;
		this.check=check;
	}
	public Class_tracklist_object(String name, String phone, boolean check, String username)
	{
		this.name=name;
		this.phone=phone;
		this.check=check;
		this.username=username;
	}
}
