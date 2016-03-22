package com.helpme.after_login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.helpme.helpmeui.R;

import java.util.ArrayList;

/**
 * Created by HARINATHKANCHU on 10-03-2016.
 */
public class Adapter_welcome extends BaseAdapter {

    ArrayList<Class_banner_images> list;

    public Adapter_welcome()
    {
        list=new ArrayList<Class_banner_images>();

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int index, View view, ViewGroup parent) {
        if(view==null)
        {
            LayoutInflater inflater= LayoutInflater.from(parent.getContext());
            view=inflater.inflate(R.layout.layout_welcome,parent,false);

        }
        final String text=list.get(index).text;
        final Integer image=list.get(index).image;
        TextView tv=(TextView)(view.findViewById(R.id.textview_layout_welcome));
        ImageView iv=(ImageView)(view.findViewById(R.id.imageview_layout_welcome));

        tv.setText(text);
        iv.setImageResource(image);
        return view;
    }
}
