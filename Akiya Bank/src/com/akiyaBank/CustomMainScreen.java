package com.akiyaBank;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
/*
 * This Activity is showing the bottom buttons for search, Book mark and add property
 */
public class CustomMainScreen extends Activity
{
	Button left_btn,center_btn,right_btn;
	CustomMainScreen customMainScreen;
	CustomClickListener custom;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{		
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_layout);
		custom=new CustomClickListener();
		this.customMainScreen=this ;
		left_btn=(Button)this.findViewById(R.id.searchBtn);
		left_btn.setOnClickListener(custom);
		center_btn=(Button)this.findViewById(R.id.homeBtn);
		center_btn.setOnClickListener(custom);
		right_btn=(Button)this.findViewById(R.id.bookmarkBtn);
		
	}
	
	class CustomClickListener implements OnClickListener
	{		
		public void onClick(View v) {
			Button temp=(Button)v;
			if(temp.getId()==left_btn.getId())
			{
				Intent intent=new Intent(customMainScreen, SearchScreen.class);
				customMainScreen.startActivity(intent);

			}
			else if(temp.getId()==center_btn.getId())
			{
				Intent intent=new Intent(customMainScreen, BookMarkScreen.class);
				customMainScreen.startActivity(intent);

			}
			else if(temp.getId()==right_btn.getId())
			{
				System.out.println("CLICKED Add Property Button");
			}
		}

	}


}
