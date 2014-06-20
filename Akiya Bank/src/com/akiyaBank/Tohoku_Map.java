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
 * This Activity is showing the property in Tohoku province
 */
public class Tohoku_Map extends CustomMainScreen implements OnClickListener,Runnable {

	Tohoku_Map tohoku_Map;
	ProgressDialog dialog;
	TextView heading;
	Button back;
	JSONArray tohokuMapJSONArray;	
	int idValue,length,densityDpi,heightinPX,widthInPx;	
	float density,ratioOfScreens;		
	String id,name_en,name_jp,total,txt_heading;

	//*********Following are the x and y positions of the Buttons(Tohoku_Map state cities)
	int aligationOfX_Heading=250,aligationOfX_Aomori_ken=400,aligationOfX_Akita_Ken=240,aligationOfX_Iwate_Ken=420,aligationOfX_Yamagata_Ken=170,aligationOfX_Miyagi_Ken=390,aligationOfX_Fukushima_Kenu=350;
	int aligationOfY_Heading=80,aligationOfY_Aomori_ken=250,aligationOfY_Akita_Ken=500,aligationOfY_Iwate_Ken=510,aligationOfY_Yamagata_Ken=630,aligationOfY_Miyagi_Ken=640,aligationOfY_Fukushima_Kenu=810;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.tohoku_Map=this;	
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		LayoutInflater mInflater = LayoutInflater.from(this);		
		RelativeLayout setRelativeLayout=(RelativeLayout) mInflater.inflate(R.layout.tohoku_map, null);
		container.addView(setRelativeLayout);	
		AbsoluteLayout absoluteLayoutObj=(AbsoluteLayout)this.findViewById(R.id.tohoku_map_absoluteLayout);
		back=(Button)this.findViewById(R.id.btn_back_Tohoku);
		back.setOnClickListener(this);
		heightinPX=MyUtility.getHeight(this);
		widthInPx=MyUtility.getWidth(this);
		dialog=MyUtility.progressDialog(tohoku_Map, getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(this).start();        		

		idValue=getIntent().getIntExtra("idValue_Tohoku", -1);//*********carry ID from main japan map
		txt_heading=getIntent().getStringExtra("Heading_Tohoku");//*********carry name from main japan map	
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
						System.out.println("Response: "+response);
						tohokuMapJSONArray=new JSONArray(response);
						length=tohokuMapJSONArray.length();
						if(length>0)
						{
							for(int i=0;i<length;i++){
								JSONObject jsonObject=tohokuMapJSONArray.getJSONObject(i);
								int total=jsonObject.getInt("total");
								
								//*********Calculating and fixing the position of "States" according to screen resolution
							
								if(jsonObject.getString("name_en").equals("Aomori-ken"))
								{	
									Button bt_Aomori_ken=(Button)tohoku_Map.findViewById(R.id.btn_Aomori_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Aomori_ken.setVisibility(Button.VISIBLE);
										bt_Aomori_ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Aomori_ken, aligationOfY_Aomori_ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Aomori_ken, aligationOfY_Aomori_ken,widthInPx,heightinPX);
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										final String id=jsonObject.getString("id");
										bt_Aomori_ken.setLayoutParams(layoutParams);
										bt_Aomori_ken.setTextSize(12);	
										bt_Aomori_ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}						    	
								}
								//*********Setting Akita_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Akita-ken"))
								{
									Button bt_Akita_Ken=(Button)tohoku_Map.findViewById(R.id.btn_Akita_Ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Akita_Ken.setVisibility(Button.VISIBLE);
										bt_Akita_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Akita_Ken, aligationOfY_Akita_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Akita_Ken, aligationOfY_Akita_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Akita_Ken.setLayoutParams(layoutParams);
										bt_Akita_Ken.setTextSize(12);	
										bt_Akita_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}	
								}
								//*********Setting Iwate_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Iwate-ken"))
								{
									Button bt_Iwate_Ken=(Button)tohoku_Map.findViewById(R.id.btn_Iwate_Ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Iwate_Ken.setVisibility(Button.VISIBLE);
										bt_Iwate_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Iwate_Ken, aligationOfY_Iwate_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Iwate_Ken, aligationOfY_Iwate_Ken,widthInPx,heightinPX);
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										final String id=jsonObject.getString("id");
										bt_Iwate_Ken.setLayoutParams(layoutParams);
										bt_Iwate_Ken.setTextSize(12);	
										bt_Iwate_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}	
								}
								//*********Setting Yamagata_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Yamagata-ken"))
								{
									Button bt_Yamagata_Ken=(Button)tohoku_Map.findViewById(R.id.btn_Yamagata_Ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Yamagata_Ken.setVisibility(Button.VISIBLE);
										bt_Yamagata_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Yamagata_Ken, aligationOfY_Yamagata_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Yamagata_Ken, aligationOfY_Yamagata_Ken,widthInPx,heightinPX);
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										final String id=jsonObject.getString("id");
										bt_Yamagata_Ken.setLayoutParams(layoutParams);
										bt_Yamagata_Ken.setTextSize(12);	
										bt_Yamagata_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}		
								}
								//*********Setting Miyagi_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Miyagi-ken"))
								{
									Button bt_Miyagi_Ken=(Button)tohoku_Map.findViewById(R.id.btn_Miyagi_Ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Miyagi_Ken.setVisibility(Button.VISIBLE);
										bt_Miyagi_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Miyagi_Ken, aligationOfY_Miyagi_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Miyagi_Ken, aligationOfY_Miyagi_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Miyagi_Ken.setLayoutParams(layoutParams);
										bt_Miyagi_Ken.setTextSize(12);	
										bt_Miyagi_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}		
								}
								//*********Setting Fukushima_Kenu button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Fukushima-ken"))
								{
									Button bt_Fukushima_Kenu=(Button)tohoku_Map.findViewById(R.id.btn_Fukushima_Ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Fukushima_Kenu.setVisibility(Button.VISIBLE);
										bt_Fukushima_Kenu.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Fukushima_Kenu, aligationOfY_Fukushima_Kenu,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Fukushima_Kenu, aligationOfY_Fukushima_Kenu,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Fukushima_Kenu.setLayoutParams(layoutParams);
										bt_Fukushima_Kenu.setTextSize(12);	
										bt_Fukushima_Kenu.setOnClickListener(new OnClickListener() {

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
							MyUtility.showAlert(getResources().getString(R.string.no_vacant_houses), tohoku_Map);
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
					MyUtility.netWorkDetail(tohoku_Map);
				}
			});
		}		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==back.getId()){
			this.finish();
		}
	}

	//*********This method is use to open the next class/screen and sends the Id value to other class  *****//
	private void nextActivity(String id)
	{
		Intent intent=new Intent(tohoku_Map, PropertyList.class);
		intent.putExtra("Id", id);
		tohoku_Map.startActivity(intent);
	}
	private void dataNotFoundActivity(){		
		//MyUtility.showAlert("Data not found.", searchResultScreen);
		new AlertDialog.Builder(tohoku_Map)
		.setMessage(getResources().getString(R.string.unableToFetchContent_jp))
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				tohoku_Map.finish();
			}
		}).show();
	
}
}