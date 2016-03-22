package com.helpme.groups;

public class Class_group_object {
	public String group_name;
	public String total_numbers;
	public String group_id;
	public String group_admin_username;
	public String group_admin_phone;
	public Class_group_object(String name, String about, String group_id)
	{
		group_name=name;
		total_numbers=about;
		this.group_id=group_id;
	}
	public Class_group_object(String name, String about, String group_id, String admin_username, String admin_phone)
	{
		group_name=name;
		total_numbers=about;
		this.group_id=group_id;
		this.group_admin_username=admin_username;
		this.group_admin_phone=admin_phone;
	}
}
