package com.akiyaBank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;

import com.util.MyUtility;

/*
 * This Activity is showing the Search by Keyword
 */
public class SearchByKeyword extends Activity implements OnClickListener,Runnable {

	SearchByKeyword searchByKeyword;
	MultiAutoCompleteTextView textView;
	String keyword;
	Button searchButton;	
	String switcher="ok";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_screen);
		this.searchByKeyword=this;		
		textView=(MultiAutoCompleteTextView)this.findViewById(R.id.multiAutoCompleteTextView1);
		searchButton=(Button)this.findViewById(R.id.searchButton);
		searchButton.setOnClickListener(this);
		
	}
	public void run() 
	{
		try
		{
			this.runOnUiThread(new Runnable()
			{
				public void run()
				{								
					Intent intent=new Intent(searchByKeyword, SearchResultScreen.class);			
					intent.putExtra("keyword", keyword);	
					intent.putExtra("valueSwitcherKeyword",switcher);
					searchByKeyword.startActivity(intent);	
				}
			});
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.d("Error:::", "error name - " + e.toString()+"error message"+e.getMessage());
		}

	}

	public void onClick(View v) {
		Button bt=(Button)v;
		if(bt.getId()==searchButton.getId()){
			keyword=textView.getText().toString().trim();
			if(keyword.equals(""))
			{
				MyUtility.showAlert(getResources().getString(R.string.enter_valid_keyword),this);
			}
			else
			{			
				new Thread(this).start();
			}			

		}

	}


}
