package com.helpme.alert_dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
		tv.setTextColor(Color.BLACK);
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
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				onfalse();
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
	public void ontrue(String current_password,String new_password)
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
	public void forgot_password_popup(Context context,String left,String right)
	{
		final Dialog dialog=new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//dialog.setTitle("Enter your email to get new password");
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.dialog_forgot_password);
		//dialog.setTitle(title);
		final EditText tv=(EditText)dialog.findViewById(R.id.edittext_dialog_forgot_password_input);
		tv.setText("");
		com.helpme.widgets.SAutoBgButton left_button=(com.helpme.widgets.SAutoBgButton)dialog.findViewById(R.id.button_dialog_forgot_password_cancel);
		left_button.setText(left);
		left_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				onfalse();

			}
		});
		com.helpme.widgets.SAutoBgButton right_button=(com.helpme.widgets.SAutoBgButton)dialog.findViewById(R.id.button_dialog_forgot_password_ok);
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
	public void change_password(final Context context,String left,String right)
	{
		final Dialog dialog=new Dialog(context);
		//dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//dialog.setTitle("Enter your email to get new password");
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.dialog_change_password);
		//dialog.setTitle(title);
		final EditText editext_current_password=(EditText)dialog.findViewById(R.id.edittext_dialog_change_password_current);
		final EditText editext_new_password=(EditText)dialog.findViewById(R.id.edittext_dialog_change_password_new);
		final EditText editext_repeat_password=(EditText)dialog.findViewById(R.id.edittext_dialog_change_password_repeat);



		com.helpme.widgets.SAutoBgButton left_button=(com.helpme.widgets.SAutoBgButton)dialog.findViewById(R.id.button_change_password_cancel);
		left_button.setText(left);
		left_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				onfalse();

			}
		});

		com.helpme.widgets.SAutoBgButton right_button=(com.helpme.widgets.SAutoBgButton)dialog.findViewById(R.id.button_change_password_ok);
		right_button.setText(right);
		right_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();


				final String current_password=editext_current_password.getEditableText().toString().trim();
				final String new_password=editext_new_password.getEditableText().toString().trim();
				final String repeat_password=editext_repeat_password.getEditableText().toString().trim();

				if (repeat_password.equals(new_password)) {
					//Toast.makeText(context,"new password is "+new_password,Toast.LENGTH_SHORT).show();
					ontrue(current_password, new_password);
				} else {
					ontrue("New Passwords does not match.");
				}
			}
		});

		dialog.show();
	}

}
