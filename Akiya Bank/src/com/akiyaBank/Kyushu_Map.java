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
 * This Activity is showing the property in Kyushu province
 */
public class Kyushu_Map extends CustomMainScreen implements OnClickListener,Runnable {

	Kyushu_Map kyushu_Map;
	ProgressDialog dialog;	
	TextView heading;
	JSONArray kyushuMapJSONArray;	
	int idValue,length,densityDpi,heightinPX,widthInPx;		
	float density,ratioOfScreens;
	Button back;		 
	String id,name_en,name_jp,total,txt_heading;	

	//*********Following are the x and y positions of the Buttons(Kyushu_Map state cities)
	int aligationOfX_Heading=250,aligationOfX_Fukuoka_Ken=320,aligationOfX_Saga_Ken=230,aligationOfX_Nagasaki_Ken=80,aligationOfX_Oita_Ken=400,aligationOfX_Kumamota_Ken=220,aligationOfX_Miyazaki_Ken=350,aligationOfX_Kagoshima_Ken=170;
	int aligationOfY_Heading=60,aligationOfY_Fukuoka_Ken=330,aligationOfY_Saga_Ken=380,aligationOfY_Nagasaki_Ken=440,aligationOfY_Oita_Ken=450,aligationOfY_Kumamota_Ken=570,aligationOfY_Miyazaki_Ken=630,aligationOfY_Kagoshima_Ken=670;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.kyushu_Map=this;	
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		LayoutInflater mInflater = LayoutInflater.from(this);		
		RelativeLayout setRelativeLayout=(RelativeLayout) mInflater.inflate(R.layout.kyushu_map, null);
		container.addView(setRelativeLayout);	
		AbsoluteLayout absoluteLayoutObj=(AbsoluteLayout)this.findViewById(R.id.kyushu_map_absoluteLayout);
		back=(Button)this.findViewById(R.id.btn_back_Kyushu);
		back.setOnClickListener(this);		

		heightinPX=MyUtility.getHeight(this);
		widthInPx=MyUtility.getWidth(this);
		dialog=MyUtility.progressDialog(kyushu_Map, getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(this).start();        		

		idValue=getIntent().getIntExtra("idValue_Kyushu", -1);	//*********carry ID from main japan map
		txt_heading=getIntent().getStringExtra("Heading_Kyushu");//*********carry name from main japan map		
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
						kyushuMapJSONArray=new JSONArray(response);
						length=kyushuMapJSONArray.length();
						if(length>0)
						{
							for(int i=0;i<length;i++){
								JSONObject jsonObject=kyushuMapJSONArray.getJSONObject(i);
								int total=jsonObject.getInt("total");
								
								//*********Calculating and fixing the position of "States" according to screen resolution
							
								if(jsonObject.getString("name_en").equals("Fukuoka-ken"))
								{	
									Button bt_Fukuoka_Ken=(Button)kyushu_Map.findViewById(R.id.btn_fukuoka_ken);
									bt_Fukuoka_Ken.setVisibility(Button.INVISIBLE);
									System.out.println("Cities Name::::::"+jsonObject.getString("name_en"));
									if(jsonObject.getInt("total")>0)
									{
										bt_Fukuoka_Ken.setVisibility(Button.VISIBLE);
										bt_Fukuoka_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Fukuoka_Ken, aligationOfY_Fukuoka_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Fukuoka_Ken, aligationOfY_Fukuoka_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Fukuoka_Ken.setLayoutParams(layoutParams);
										bt_Fukuoka_Ken.setTextSize(12);	
										bt_Fukuoka_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}						    	
								}
								//*********Setting Saga_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Saga-ken"))
								{
									Button bt_Saga_Ken=(Button)kyushu_Map.findViewById(R.id.btn_saga_ken);
									bt_Saga_Ken.setVisibility(Button.INVISIBLE);
									if(jsonObject.getInt("total")>0)
									{
										bt_Saga_Ken.setVisibility(Button.VISIBLE);
										bt_Saga_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Saga_Ken, aligationOfY_Saga_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Saga_Ken, aligationOfY_Saga_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Saga_Ken.setLayoutParams(layoutParams);
										bt_Saga_Ken.setTextSize(12);	
										bt_Saga_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}
								}
								//*********Setting Oita_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Oita-ken"))
								{
									Button bt_Oita_Ken=(Button)kyushu_Map.findViewById(R.id.btn_oita_ken);
									bt_Oita_Ken.setVisibility(Button.INVISIBLE);
									if(jsonObject.getInt("total")>0)
									{
										bt_Oita_Ken.setVisibility(Button.VISIBLE);
										bt_Oita_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Oita_Ken, aligationOfY_Oita_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Oita_Ken, aligationOfY_Oita_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Oita_Ken.setLayoutParams(layoutParams);
										bt_Oita_Ken.setTextSize(12);	
										bt_Oita_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}
								}
								//*********Setting Kumamota_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Kumamoto-ken"))
								{
									Button bt_Kumamota_Ken=(Button)kyushu_Map.findViewById(R.id.btn_kumamota_ken);
									bt_Kumamota_Ken.setVisibility(Button.INVISIBLE);
									if(jsonObject.getInt("total")>0)
									{
										bt_Kumamota_Ken.setVisibility(Button.VISIBLE);
										bt_Kumamota_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Kumamota_Ken, aligationOfY_Kumamota_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Kumamota_Ken, aligationOfY_Kumamota_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Kumamota_Ken.setLayoutParams(layoutParams);
										bt_Kumamota_Ken.setTextSize(12);	
										bt_Kumamota_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}
								}
								//*********Setting Miyazaki_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Miyazaki-ken"))
								{
									Button bt_Miyazaki_Ken=(Button)kyushu_Map.findViewById(R.id.btn_miyazaki_ken);
									bt_Miyazaki_Ken.setVisibility(Button.INVISIBLE);
									if(jsonObject.getInt("total")>0)
									{
										bt_Miyazaki_Ken.setVisibility(Button.VISIBLE);
										bt_Miyazaki_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Miyazaki_Ken, aligationOfY_Miyazaki_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Miyazaki_Ken, aligationOfY_Miyazaki_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");									
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Miyazaki_Ken.setLayoutParams(layoutParams);
										bt_Miyazaki_Ken.setTextSize(12);	
										bt_Miyazaki_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}	
								}
								//*********Setting Kagoshima_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Kagoshima-ken"))
								{
									Button bt_Kagoshima_Ken=(Button)kyushu_Map.findViewById(R.id.btn_kagoshima_ken);
									bt_Kagoshima_Ken.setVisibility(Button.INVISIBLE);
									if(jsonObject.getInt("total")>0)
									{
										bt_Kagoshima_Ken.setVisibility(Button.VISIBLE);
										bt_Kagoshima_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Kagoshima_Ken, aligationOfY_Kagoshima_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Kagoshima_Ken, aligationOfY_Kagoshima_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Kagoshima_Ken.setLayoutParams(layoutParams);
										bt_Kagoshima_Ken.setTextSize(12);	
										bt_Kagoshima_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}	
								}
								//*********Setting Nagasaki_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Nagasaki-ken"))
								{
									Button bt_Nagasaki_Ken=(Button)kyushu_Map.findViewById(R.id.btn_nagasaki_ken);
									bt_Nagasaki_Ken.setVisibility(Button.INVISIBLE);
									if(jsonObject.getInt("total")>0)
									{
										bt_Nagasaki_Ken.setVisibility(Button.VISIBLE);
										bt_Nagasaki_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Nagasaki_Ken, aligationOfY_Nagasaki_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Nagasaki_Ken, aligationOfY_Nagasaki_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Nagasaki_Ken.setLayoutParams(layoutParams);
										bt_Nagasaki_Ken.setTextSize(12);	
										bt_Nagasaki_Ken.setOnClickListener(new OnClickListener() {

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
							MyUtility.showAlert(getResources().getString(R.string.no_vacant_houses), kyushu_Map);
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
					MyUtility.netWorkDetail(kyushu_Map);
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
		Intent intent=new Intent(kyushu_Map, PropertyList.class);
		intent.putExtra("Id", id);
		kyushu_Map.startActivity(intent);
	}
	private void dataNotFoundActivity(){		
		//MyUtility.showAlert("Data not found.", searchResultScreen);
		new AlertDialog.Builder(kyushu_Map)
		.setMessage(getResources().getString(R.string.unableToFetchContent_jp))
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				kyushu_Map.finish();
			}
		}).show();
	
}
}