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
 * This Activity is showing the property in Hokuriku province
 */
public class Hokuriku_Map extends CustomMainScreen implements OnClickListener,Runnable {

	Hokuriku_Map hokuriku_Map;
	ProgressDialog dialog;	
	TextView heading;
	Button bt_Ishikawa_Ken,bt_Toyama_Ken,bt_Fukui_Ken;
	JSONArray hokurikuMapJSONArray;	
	int idValue,length,densityDpi,heightinPX,widthInPx;		
	float density;	
	String id,name_en,name_jp,total,txt_heading;

	//*********Following are the x and y positions of the Buttons(Hokuriku_Map state cities)
	int aligationOfX_Heading=250,aligationOfX_Ishikawa_Ken=60,aligationOfX_Toyama_Ken=400,aligationOfX_Fukui_Ken=190;
	int aligationOfY_Heading=80,aligationOfY_Ishikawa_Ken=450,aligationOfY_Toyama_Ken=510,aligationOfY_Fukui_Ken=650;
	float ratioOfScreens;
	Button back;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.hokuriku_Map=this;	
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		LayoutInflater mInflater = LayoutInflater.from(this);		
		RelativeLayout setRelativeLayout=(RelativeLayout) mInflater.inflate(R.layout.hokuriku_map, null);
		container.addView(setRelativeLayout);	
		AbsoluteLayout absoluteLayoutObj=(AbsoluteLayout)this.findViewById(R.id.hokuriku_map_absoluteLayout);
		back=(Button)this.findViewById(R.id.btn_back_Hokuriku);
		back.setOnClickListener(this);
		heightinPX=MyUtility.getHeight(this);
		widthInPx=MyUtility.getWidth(this);
		dialog=MyUtility.progressDialog(hokuriku_Map,getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(this).start();        		

		idValue=getIntent().getIntExtra("idValue_Hokuriku", -1);//*********carry ID from main japan map	
		txt_heading=getIntent().getStringExtra("Heading_Hokuriku");//*********carry name from main japan map			
	}

	public void run()
	{		
		try
		{
			HTTPConnect hc=new HTTPConnect();
			String url=MyUtility.serverAddressForStates+idValue;  //********Url path
			final String response=hc.openHttpConnection(url);			
			this.runOnUiThread(new Runnable() 
			{
				public void run() {
					dialog.dismiss();
					try
					{
						hokurikuMapJSONArray=new JSONArray(response);
						length=hokurikuMapJSONArray.length();
						if(length>0)
						{
							for(int i=0;i<length;i++){
								JSONObject jsonObject=hokurikuMapJSONArray.getJSONObject(i);
								int total=jsonObject.getInt("total");
								
								//*********Calculating and fixing the position of "States" according to screen resolution
								
								if(jsonObject.getString("name_en").equals("Ishikawa-ken"))
								{	
									Button bt_Ishikawa_Ken=(Button)hokuriku_Map.findViewById(R.id.btn_ishikawa_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Ishikawa_Ken.setVisibility(Button.VISIBLE);
										bt_Ishikawa_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Ishikawa_Ken, aligationOfY_Ishikawa_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Ishikawa_Ken, aligationOfY_Ishikawa_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Ishikawa_Ken.setLayoutParams(layoutParams);
										bt_Ishikawa_Ken.setTextSize(12);	
										bt_Ishikawa_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}						    	
								}

								//*********Setting Toyama button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Toyama-ken"))
								{	
									Button bt_Toyama_Ken=(Button)hokuriku_Map.findViewById(R.id.btn_toyama_ken);
									if(jsonObject.getInt("total")>=0)
									{
										bt_Toyama_Ken.setVisibility(Button.VISIBLE);
										bt_Toyama_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Toyama_Ken, aligationOfY_Toyama_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Toyama_Ken, aligationOfY_Toyama_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Toyama_Ken.setLayoutParams(layoutParams);
										bt_Toyama_Ken.setTextSize(12);	
										bt_Toyama_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}						    	
								}
								//*********Setting Fukui button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Fukui-ken"))
								{	
									Button bt_Fukui_Ken=(Button)hokuriku_Map.findViewById(R.id.btn_fukui_ken);
									if(jsonObject.getInt("total")>=0)
									{
										bt_Fukui_Ken.setVisibility(Button.VISIBLE);
										bt_Fukui_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Fukui_Ken, aligationOfY_Fukui_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Fukui_Ken, aligationOfY_Fukui_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Fukui_Ken.setLayoutParams(layoutParams);
										bt_Fukui_Ken.setTextSize(12);	
										bt_Fukui_Ken.setOnClickListener(new OnClickListener() {

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
							MyUtility.showAlert(getResources().getString(R.string.no_vacant_houses), hokuriku_Map);
							
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
					MyUtility.netWorkDetail(hokuriku_Map);
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
		Intent intent=new Intent(hokuriku_Map, PropertyList.class);
		intent.putExtra("Id", id);
		hokuriku_Map.startActivity(intent);
	}
	
	private void dataNotFoundActivity(){		
		
		new AlertDialog.Builder(hokuriku_Map)
		.setMessage(getResources().getString(R.string.unableToFetchContent_jp))
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				hokuriku_Map.finish();
			}
		}).show();
	
}
}