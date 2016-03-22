package com.helpme.contacts;

import android.content.ContentResolver;

import java.util.ArrayList;

public class Class_give_contacts {

private static ArrayList<Class_contacts_object> arraylist;

    public static ArrayList<Class_contacts_object> giveList(ContentResolver resolver)
    {
        if(arraylist==null)
        {
            refresh(resolver);
        }
        return arraylist;
    }
    public static void initialise(ContentResolver resolver)
    {
        if(arraylist==null)
        {
            arraylist=new ArrayList<Class_contacts_object>();
            refresh(resolver);
        }
    }
    public static void refresh(ContentResolver resolver)
    {
        if(arraylist==null)
        {
            initialise(resolver);
        }
        else
        {
            arraylist.clear();

            String[] phones=Class_give_phones.give(resolver);//{"9970610243","9665644668","9970610243","9665644668"};//
            int i,len=phones.length;
            String[] names=new String[len];//{"Harinath","Mansoor","Harinath","Mansoor"};//
            for(i=0;i<len;i++)
            {
                names[i]=Class_give_name.give(phones[i],resolver);
                arraylist.add(new Class_contacts_object(phones[i],names[i]));
            }
        }
    }
}
