package com.helpme.groups;

public class Class_locations {
	public String username;
	public String phone;
	public Double latitude;
	public Double longitude;
	public String last_updated;
	public Class_locations(String username, String phone, Double latitude, Double longitude, String last_updated)
	{
		this.username=username;
		this.phone=phone;
		this.latitude=latitude;
		this.longitude=longitude;
		this.last_updated=last_updated;
	}
	public Class_locations()
	{
		
	}
	@Override
	public String toString() {
		
		return username+" "+latitude+" "+longitude+" "+last_updated;
	}
}
