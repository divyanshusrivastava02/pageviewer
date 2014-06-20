package com.akiyaBank;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.util.MyUtility;
/*
 * This Activity is showing the Search screen
 */
public class SearchScreen extends TabActivity 
{
	int widthInPx,heightinPX;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		TabHost tabHost = getTabHost();
		tabHost.setBackgroundColor(Color.GRAY);
		widthInPx=MyUtility.getWidth(this); //******** Fetching the screen width in pixels **********//
		heightinPX=MyUtility.getHeight(this);  //******** Fetching the screen height in pixels **********//
				
		//****************** Add tab for Search by Keyword  ********************//
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator(getResources().getString(R.string.by_keyword))
				.setContent(new Intent(this, SearchByKeyword.class)));
		
		//****************** Add tab for Search by address  ********************//
		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator(getResources().getString(R.string.by_address))
				.setContent(new Intent(this, SearchByAddress.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
		
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++)
		{ 	
			//************** Setting the size of the tab according to screen resolution *****************//
			tabHost.getTabWidget().getChildAt(i).setLayoutParams(new LinearLayout.LayoutParams(widthInPx/2,heightinPX/10));   
		} 		
		
	}
	public void onTabChanged(String tabId) 
	{
		Activity activity = getLocalActivityManager().getActivity(tabId);
		if (activity != null)
		{
			activity.onWindowFocusChanged(true);
		}
	}

}

