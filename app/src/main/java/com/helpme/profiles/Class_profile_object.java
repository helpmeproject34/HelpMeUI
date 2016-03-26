package com.helpme.profiles;

/**
 * Created by HARINATHKANCHU on 24-03-2016.
 */
public class Class_profile_object {


    public static int BLOOD_DONOR=1;
    public static int CARPENTER=2;
    public static int PLUMBER=3;
    public static int ELECTRICIAN=4;
    public static int BIKE_RENTAL=5;
    public static int CLEANER=6;
    public static int PAINTER=7;
    public static int CAR_RENTAL=8;


    public String working_name;
    public String working_phone;
    public int category;
    public Double latitude;
    public Double longitude;
    public int from;
    public int to;
    public String working_days;
    public String username;

    public Class_profile_object(String username,String working_name,String working_phone,int category,Double latitude,Double longitude,int from,int to,String working_days)
    {
        this.username=username;
        this.working_name=working_name;
        this.working_phone=working_phone;
        this.category=category;
        this.latitude=latitude;
        this.longitude=longitude;
        this.from=from;
        this.to=to;
        this.working_days=working_days;
    }
}
