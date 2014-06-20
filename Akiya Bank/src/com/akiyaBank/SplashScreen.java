package com.akiyaBank;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashScreen extends Activity implements Runnable
{
	SplashScreen splashScreen;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splashscreen);
		this.splashScreen=this;     
		new Thread(this).start();

	}

	public void run()
	{
		try 
		{
			Thread.sleep(1000);
			this.runOnUiThread(new Runnable()
			{
				public void run()
				{			
					splashScreen.finish();
					Intent i=new Intent(splashScreen,JapanMap.class);					
					splashScreen.startActivity(i);	

				}				
			});			
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}		


}
