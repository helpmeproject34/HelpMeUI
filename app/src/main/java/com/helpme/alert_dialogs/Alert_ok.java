package com.helpme.alert_dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.helpme.helpmeui.R;


public class Alert_ok {


	Context con;

	public static void show(Context context,String message)
	{

		final Dialog dialog=new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_display);
		//dialog.setTitle(title);
		TextView tv=(TextView)dialog.findViewById(R.id.textview_dialog_display);
		tv.setText(message);
		com.helpme.widgets.SAutoBgButton b=(com.helpme.widgets.SAutoBgButton)dialog.findViewById(R.id.button_dialog_display);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				
			}
		});
		
		dialog.show();
	}
	public  void  ok_or_cancel(Context context,String title,String message)
	{

		ok_or_cancel(context, title, message, "CANCEL", "OK");
	}
	public  void ok_or_cancel(Context context,String title,String message,String left,String right)
	{
		
		final Dialog dialog=new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.dialog_ok_cancel);
		//dialog.setTitle(title);
		TextView tv=(TextView)dialog.findViewById(R.id.textview_dialog_ok_cancel);
		tv.setText(message);
		com.helpme.widgets.SAutoBgButton left_button=(com.helpme.widgets.SAutoBgButton)dialog.findViewById(R.id.button_dialog_ok_cancel_left);
		left_button.setText(left);
		left_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				onfalse();
			}
		});
		com.helpme.widgets.SAutoBgButton right_button=(com.helpme.widgets.SAutoBgButton)dialog.findViewById(R.id.button_dialog_ok_cancel_right);
		right_button.setText(right);
		right_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				ontrue();
				
			}
		});
		
		dialog.show();
	}
	public void ontrue()
	{
	
	}
	public void onfalse()
	{
		
	}
	public void ontrue(String name)
	{

	}
	public void new_group_input(Context context,String left,String right)
	{

		final Dialog dialog=new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.dialog_new_group);
		//dialog.setTitle(title);
		final EditText tv=(EditText)dialog.findViewById(R.id.edittext_dialog_new_group_input);
		tv.setText("");
		com.helpme.widgets.SAutoBgButton left_button=(com.helpme.widgets.SAutoBgButton)dialog.findViewById(R.id.button_dialog_new_group_cancel);
		left_button.setText(left);
		left_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				onfalse();

			}
		});
		com.helpme.widgets.SAutoBgButton right_button=(com.helpme.widgets.SAutoBgButton)dialog.findViewById(R.id.button_dialog_new_group_ok);
		right_button.setText(right);
		right_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				ontrue(tv.getEditableText().toString());


			}
		});

		dialog.show();
	}
	
}
