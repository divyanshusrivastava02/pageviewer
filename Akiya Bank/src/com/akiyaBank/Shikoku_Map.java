package com.akiyaBank;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.util.HTTPConnect;
import com.util.MyUtility;
/*
 * This Activity is showing the property in Shikoku province
 */
public class Shikoku_Map extends CustomMainScreen implements OnClickListener,Runnable {

	Shikoku_Map shikoku_Map;
	ProgressDialog dialog;	
	TextView heading;
	Button back;
	JSONArray shikokuMapJSONArray;	
	int idValue,length,densityDpi,heightinPX,widthInPx;	
	float density,ratioOfScreens;	
	String id,name_en,name_jp,total,txt_heading;	

	//*********Following are the x and y positions of the Buttons(Shikoku_Map state cities)
	int aligationOfX_Heading=250,aligationOfX_Kagawa_Ken=300,aligationOfX_Ehime_Ken=80,aligationOfX_Tokushima_Ken=350,aligationOfX_Kochi_Ken=120;
	int aligationOfY_Heading=80,aligationOfY_Kagawa_Ken=450,aligationOfY_Ehime_Ken=540,aligationOfY_Tokushima_Ken=530,aligationOfY_Kochi_Ken=690;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.shikoku_Map=this;	
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		LayoutInflater mInflater = LayoutInflater.from(this);		
		RelativeLayout setRelativeLayout=(RelativeLayout) mInflater.inflate(R.layout.shikoku_map, null);
		container.addView(setRelativeLayout);	
		AbsoluteLayout absoluteLayoutObj=(AbsoluteLayout)this.findViewById(R.id.shikoku_map_absoluteLayout);
		back=(Button)this.findViewById(R.id.btn_back_Shikoku);
		back.setOnClickListener(this);
		heightinPX=MyUtility.getHeight(this);
		widthInPx=MyUtility.getWidth(this);
		dialog=MyUtility.progressDialog(shikoku_Map, getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(this).start();        		

		idValue=getIntent().getIntExtra("idValue_Shikoku", -1);	//*********carry ID from main japan map	
		txt_heading=getIntent().getStringExtra("Heading_Shikoku");//*********carry name from main japan map		
	}

	public void run()
	{		
		try
		{
			HTTPConnect hc=new HTTPConnect();
			String url=MyUtility.serverAddressForStates+idValue;
			final String response=hc.openHttpConnection(url);			
			this.runOnUiThread(new Runnable() 
			{
				public void run() {
					dialog.dismiss();
					try
					{
						shikokuMapJSONArray=new JSONArray(response);
						length=shikokuMapJSONArray.length();
						if(length>0)
						{
							for(int i=0;i<length;i++){
								JSONObject jsonObject=shikokuMapJSONArray.getJSONObject(i);
								
								int total=jsonObject.getInt("total");	
								
								//*********Calculating and fixing the position of "States" according to screen resolution
							
								if(jsonObject.getString("name_en").equals("Kagawa-ken"))
								{	
									Button bt_Kagawa_Ken=(Button)shikoku_Map.findViewById(R.id.btn_kagawa_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Kagawa_Ken.setVisibility(Button.VISIBLE);
										bt_Kagawa_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Kagawa_Ken, aligationOfY_Kagawa_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Kagawa_Ken, aligationOfY_Kagawa_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");									
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Kagawa_Ken.setLayoutParams(layoutParams);
										bt_Kagawa_Ken.setTextSize(12);	
										bt_Kagawa_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}						    	
								}
								//*********Setting Ehime_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Ehime-ken"))
								{
									Button bt_Ehime_Ken=(Button)shikoku_Map.findViewById(R.id.btn_ehime_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Ehime_Ken.setVisibility(Button.VISIBLE);
										bt_Ehime_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Ehime_Ken, aligationOfY_Ehime_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Ehime_Ken, aligationOfY_Ehime_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Ehime_Ken.setLayoutParams(layoutParams);
										bt_Ehime_Ken.setTextSize(12);	
										bt_Ehime_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}
								}
								//*********Setting Tokushima_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Tokushima-ken"))
								{
									Button bt_Tokushima_Ken=(Button)shikoku_Map.findViewById(R.id.btn_tokushima_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Tokushima_Ken.setVisibility(Button.VISIBLE);
										bt_Tokushima_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Tokushima_Ken, aligationOfY_Tokushima_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Tokushima_Ken, aligationOfY_Tokushima_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Tokushima_Ken.setLayoutParams(layoutParams);
										bt_Tokushima_Ken.setTextSize(12);	
										bt_Tokushima_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}	
								}
								//*********Setting Kochi_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Kochi-ken"))
								{
									Button bt_Kochi_Ken=(Button)shikoku_Map.findViewById(R.id.btn_kochi_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Kochi_Ken.setVisibility(Button.VISIBLE);
										bt_Kochi_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Kochi_Ken, aligationOfY_Kochi_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Kochi_Ken, aligationOfY_Kochi_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Kochi_Ken.setLayoutParams(layoutParams);
										bt_Kochi_Ken.setTextSize(12);	
										bt_Kochi_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}	
								}												
							}
						}						
						else
						{
							MyUtility.showAlert(getResources().getString(R.string.no_vacant_houses), shikoku_Map);
						}
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
						dataNotFoundActivity();
					}
				}
			});
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			this.runOnUiThread(new Runnable() {
				public void run() {					
					dialog.dismiss();
					MyUtility.netWorkDetail(shikoku_Map);
				}
			});
		}		
	}

	public void onClick(View v) {		
		if(v.getId()==back.getId()){
			this.finish();
		}
	}

	//*********This method is use to open the next class/screen and sends the Id value to other class  *****//
	private void nextActivity(String id)
	{
		Intent intent=new Intent(shikoku_Map, PropertyList.class);
		intent.putExtra("Id", id);
		shikoku_Map.startActivity(intent);
	}
	private void dataNotFoundActivity(){		
		
		new AlertDialog.Builder(shikoku_Map)
		.setMessage(getResources().getString(R.string.unableToFetchContent_jp))
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				shikoku_Map.finish();
			}
		}).show();
	
}
}