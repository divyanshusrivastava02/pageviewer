package com.akiyaBank;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
 * This Activity is showing the property in Kinki province
 */
public class Kinki_Map extends CustomMainScreen implements OnClickListener,Runnable {

	Kinki_Map kinki_Map;
	ProgressDialog dialog;		
	TextView heading;
	Button back;
	JSONArray kinkiMapJSONArray;	
	int idValue,length,densityDpi,heightinPX,widthInPx;	
	float density,ratioOfScreens;	
	String id,name_en,name_jp,total,txt_heading;	

	//*********Following are the x and y positions of the Buttons(Kinki_Map state cities)
	int aligationOfX_Heading=250,aligationOfX_Shiga_Ken=370,aligationOfX_Hyogo_Ken=20,aligationOfX_Kyoto_Fu=220,aligationOfX_Osaka_Fu=190,aligationOfX_Nara_Ken=270,aligationOfX_Wakayama_Ken=120;
	int aligationOfY_Heading=80,aligationOfY_Shiga_Ken=490,aligationOfY_Hyogo_Ken=530,aligationOfY_Kyoto_Fu=550,aligationOfY_Osaka_Fu=650,aligationOfY_Nara_Ken=720,aligationOfY_Wakayama_Ken=850;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.kinki_Map=this;	
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		LayoutInflater mInflater = LayoutInflater.from(this);		
		RelativeLayout setRelativeLayout=(RelativeLayout) mInflater.inflate(R.layout.kinki_map, null);
		container.addView(setRelativeLayout);	
		AbsoluteLayout absoluteLayoutObj=(AbsoluteLayout)this.findViewById(R.id.kinki_map_absoluteLayout);
		back=(Button)this.findViewById(R.id.btn_back_Kinki);
		back.setOnClickListener(this);
		heightinPX=MyUtility.getHeight(this);
		widthInPx=MyUtility.getWidth(this);
		dialog=MyUtility.progressDialog(kinki_Map,getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(this).start();        		

		idValue=getIntent().getIntExtra("idValue_Kinki", -1);	//*********carry ID from main japan map	
		txt_heading=getIntent().getStringExtra("Heading_Kinki");//*********carry name from main japan map	
	}

	public void run()
	{	
		try
		{
			HTTPConnect hc=new HTTPConnect();
			String url=MyUtility.serverAddressForStates+idValue;//********Url path
			final String response=hc.openHttpConnection(url);			
			this.runOnUiThread(new Runnable() 
			{
				public void run() {
					dialog.dismiss();
					try
					{
						kinkiMapJSONArray=new JSONArray(response);
						length=kinkiMapJSONArray.length();
						if(length>0)
						{
							for(int i=0;i<length;i++){
								JSONObject jsonObject=kinkiMapJSONArray.getJSONObject(i);
								int total=jsonObject.getInt("total");
							
								//*********Calculating and fixing the position of "States" according to screen resolution
								if(jsonObject.getString("name_en").equals("Shiga-ken"))
								{	
									Button bt_Shiga_Ken=(Button)kinki_Map.findViewById(R.id.btn_shiga_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Shiga_Ken.setVisibility(Button.VISIBLE);
										bt_Shiga_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Shiga_Ken, aligationOfY_Shiga_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Shiga_Ken, aligationOfY_Shiga_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Shiga_Ken.setLayoutParams(layoutParams);
										bt_Shiga_Ken.setTextSize(12);	
										bt_Shiga_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}						    	
								}
								//*********Setting Hyogo_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Hyogo-ken"))
								{
									Button bt_Hyogo_Ken=(Button)kinki_Map.findViewById(R.id.btn_hyogo_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Hyogo_Ken.setVisibility(Button.VISIBLE);
										bt_Hyogo_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Hyogo_Ken, aligationOfY_Hyogo_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Hyogo_Ken, aligationOfY_Hyogo_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Hyogo_Ken.setLayoutParams(layoutParams);
										bt_Hyogo_Ken.setTextSize(12);	
										bt_Hyogo_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}
								}
								//*********Setting Kyoto_Fu button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Kyoto-fu"))
								{
									Button bt_Kyoto_Fu=(Button)kinki_Map.findViewById(R.id.btn_kyoto_fu);
									if(jsonObject.getInt("total")>0)
									{
										bt_Kyoto_Fu.setVisibility(Button.VISIBLE);
										bt_Kyoto_Fu.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Kyoto_Fu, aligationOfY_Kyoto_Fu,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Kyoto_Fu, aligationOfY_Kyoto_Fu,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Kyoto_Fu.setLayoutParams(layoutParams);
										bt_Kyoto_Fu.setTextSize(12);	
										bt_Kyoto_Fu.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}	
								}
								//*********Setting Osaka_Fu button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Osaka-fu"))
								{
									Button bt_Osaka_Fu=(Button)kinki_Map.findViewById(R.id.btn_osaka_fu);
									if(jsonObject.getInt("total")>0)
									{
										bt_Osaka_Fu.setVisibility(Button.VISIBLE);
										bt_Osaka_Fu.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Osaka_Fu, aligationOfY_Osaka_Fu,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Osaka_Fu, aligationOfY_Osaka_Fu,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Osaka_Fu.setLayoutParams(layoutParams);
										bt_Osaka_Fu.setTextSize(12);	
										bt_Osaka_Fu.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}	
								}
								//*********Setting Nara_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Nara-ken"))
								{
									Button bt_Nara_Ken=(Button)kinki_Map.findViewById(R.id.btn_nara_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Nara_Ken.setVisibility(Button.VISIBLE);
										bt_Nara_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Nara_Ken, aligationOfY_Nara_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Nara_Ken, aligationOfY_Nara_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Nara_Ken.setLayoutParams(layoutParams);
										bt_Nara_Ken.setTextSize(12);	
										bt_Nara_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}	
								}
								//*********Setting Wakayama button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Wakayama-ken"))
								{
									Button bt_Wakayama_Ken=(Button)kinki_Map.findViewById(R.id.btn_wakayama_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Wakayama_Ken.setVisibility(Button.VISIBLE);
										bt_Wakayama_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Wakayama_Ken, aligationOfY_Wakayama_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Wakayama_Ken, aligationOfY_Wakayama_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Wakayama_Ken.setLayoutParams(layoutParams);
										bt_Wakayama_Ken.setTextSize(12);	
										bt_Wakayama_Ken.setOnClickListener(new OnClickListener() {

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
							MyUtility.showAlert(getResources().getString(R.string.no_vacant_houses), kinki_Map);
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
					MyUtility.netWorkDetail(kinki_Map);
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
		Intent intent=new Intent(kinki_Map, PropertyList.class);
		intent.putExtra("Id", id);
		kinki_Map.startActivity(intent);
	}
	
	private void dataNotFoundActivity(){		
		
		new AlertDialog.Builder(kinki_Map)
		.setMessage(getResources().getString(R.string.unableToFetchContent_jp))
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				kinki_Map.finish();
			}
		}).show();
	
}
}