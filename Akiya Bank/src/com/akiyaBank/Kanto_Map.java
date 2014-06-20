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
 * This Activity is showing the property in Kanto province
 */
public class Kanto_Map extends CustomMainScreen implements OnClickListener,Runnable {

	Kanto_Map kanto_Map;
	ProgressDialog dialog;
	TextView heading;
	Button back;
	String name_Tochigi_Ken,name_Ibaraki_Ken,name_Gunma_Ken,Saitama_Ken,Tokyo_to,Chiba_Ken,Kanagawa_Ken;	
	JSONArray kantoMapJSONArray;	
	int idValue,length,densityDpi,heightinPX,widthInPx;	
	float density,ratioOfScreens;	
	String id,name_en,name_jp,total,txt_heading;	

	//*********Following are the x and y positions of the Buttons(Kanto_Map state cities)
	int aligationOfX_Heading=250,aligationOfX_Tochigi_Ken=270,aligationOfX_Ibaraki_Ken=410,aligationOfX_Gunma_Ken=200,aligationOfX_Saitama_Ken=290,aligationOfX_Tokyo_to=230,aligationOfX_Chiba_Ken=420,aligationOfX_Kanagawa_Ken=260;
	int aligationOfY_Heading=80,aligationOfY_Tochigi_Ken=410,aligationOfY_Ibaraki_Ken=460,aligationOfY_Gunma_Ken=500,aligationOfY_Saitama_Ken=570,aligationOfY_Tokyo_to=640,aligationOfY_Chiba_Ken=680,aligationOfY_Kanagawa_Ken=750;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.kanto_Map=this;	
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		LayoutInflater mInflater = LayoutInflater.from(this);		
		RelativeLayout setRelativeLayout=(RelativeLayout) mInflater.inflate(R.layout.kanto_map, null);
		container.addView(setRelativeLayout);	
		AbsoluteLayout absoluteLayoutObj=(AbsoluteLayout)this.findViewById(R.id.kanto_map_absoluteLayout);
		back=(Button)this.findViewById(R.id.btn_back_Kanto);
		back.setOnClickListener(this);
		heightinPX=MyUtility.getHeight(this);
		widthInPx=MyUtility.getWidth(this);
		dialog=MyUtility.progressDialog(kanto_Map, getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(this).start();        		

		idValue=getIntent().getIntExtra("idValue_Kanto", -1);//*********carry ID from main japan map
		txt_heading=getIntent().getStringExtra("Heading_Kanto");//*********carry name from main japan map
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
						kantoMapJSONArray=new JSONArray(response);
						length=kantoMapJSONArray.length();
						if(length>0)
						{
							for(int i=0;i<length;i++){
								JSONObject jsonObject=kantoMapJSONArray.getJSONObject(i);
								int total=jsonObject.getInt("total");
								
								//*********Calculating and fixing the position of "States" according to screen resolution
								if(jsonObject.getString("name_en").equals("Tochigi-ken"))
								{	
									Button bt_Tochigi_Ken=(Button)kanto_Map.findViewById(R.id.btn_tochigi_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Tochigi_Ken.setVisibility(Button.VISIBLE);
										bt_Tochigi_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Tochigi_Ken, aligationOfY_Tochigi_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Tochigi_Ken, aligationOfY_Tochigi_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);

										bt_Tochigi_Ken.setLayoutParams(layoutParams);
										bt_Tochigi_Ken.setTextSize(12);	
										bt_Tochigi_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}						    	
								}
								//*********Setting Ibaraki_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Ibaraki-ken"))
								{
									Button bt_Ibaraki_Ken=(Button)kanto_Map.findViewById(R.id.btn_ibaraki_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Ibaraki_Ken.setVisibility(Button.VISIBLE);
										bt_Ibaraki_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Ibaraki_Ken, aligationOfY_Ibaraki_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Ibaraki_Ken, aligationOfY_Ibaraki_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);

										bt_Ibaraki_Ken.setLayoutParams(layoutParams);
										bt_Ibaraki_Ken.setTextSize(12);	
										bt_Ibaraki_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}
								}
								//*********Setting Gunma_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Gunma-ken"))
								{
									Button bt_Gunma_Ken=(Button)kanto_Map.findViewById(R.id.btn_gunma_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Gunma_Ken.setVisibility(Button.VISIBLE);
										bt_Gunma_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Gunma_Ken, aligationOfY_Gunma_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Gunma_Ken, aligationOfY_Gunma_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);

										bt_Gunma_Ken.setLayoutParams(layoutParams);
										bt_Gunma_Ken.setTextSize(12);	
										bt_Gunma_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}		
								}
								//*********Setting Saitama_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Saitama-ken"))
								{
									Button bt_Saitama_Ken=(Button)kanto_Map.findViewById(R.id.btn_saitama_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Saitama_Ken.setVisibility(Button.VISIBLE);
										bt_Saitama_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Saitama_Ken, aligationOfY_Saitama_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Saitama_Ken, aligationOfY_Saitama_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);

										bt_Saitama_Ken.setLayoutParams(layoutParams);
										bt_Saitama_Ken.setTextSize(12);	
										bt_Saitama_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}	
								}
								//*********Setting Tokyo_to button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Tokyo-to"))
								{
									Button bt_Tokyo_to=(Button)kanto_Map.findViewById(R.id.btn_tokyo_to);
									if(jsonObject.getInt("total")>0)
									{
										bt_Tokyo_to.setVisibility(Button.VISIBLE);
										bt_Tokyo_to.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Tokyo_to, aligationOfY_Tokyo_to,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Tokyo_to, aligationOfY_Tokyo_to,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);

										bt_Tokyo_to.setLayoutParams(layoutParams);
										bt_Tokyo_to.setTextSize(12);	
										bt_Tokyo_to.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}	
								}
								//*********Setting Chiba_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Chiba-ken"))
								{
									Button bt_Chiba_Ken=(Button)kanto_Map.findViewById(R.id.btn_chiba_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Chiba_Ken.setVisibility(Button.VISIBLE);
										bt_Chiba_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Chiba_Ken, aligationOfY_Chiba_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Chiba_Ken, aligationOfY_Chiba_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);

										bt_Chiba_Ken.setLayoutParams(layoutParams);
										bt_Chiba_Ken.setTextSize(12);	
										bt_Chiba_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}		
								}
								//*********Setting Kanagawa button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Kanagawa-ken"))
								{
									Button bt_Kanagawa_Ken=(Button)kanto_Map.findViewById(R.id.btn_kanagawa_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Kanagawa_Ken.setVisibility(Button.VISIBLE);
										bt_Kanagawa_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Kanagawa_Ken, aligationOfY_Kanagawa_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Kanagawa_Ken, aligationOfY_Kanagawa_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");									
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);

										bt_Kanagawa_Ken.setLayoutParams(layoutParams);
										bt_Kanagawa_Ken.setTextSize(12);	
										bt_Kanagawa_Ken.setOnClickListener(new OnClickListener() {

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
							MyUtility.showAlert(getResources().getString(R.string.no_vacant_houses), kanto_Map);
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
					MyUtility.netWorkDetail(kanto_Map);
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
		Intent intent=new Intent(kanto_Map, PropertyList.class);
		intent.putExtra("Id", id);
		kanto_Map.startActivity(intent);
	}
	
	private void dataNotFoundActivity(){		
		
		new AlertDialog.Builder(kanto_Map)
		.setMessage(getResources().getString(R.string.unableToFetchContent_jp))
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				kanto_Map.finish();
			}
		}).show();
	
}
}