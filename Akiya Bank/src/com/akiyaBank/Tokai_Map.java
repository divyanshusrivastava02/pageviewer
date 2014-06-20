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
 * This Activity is showing the property in Tokai province
 */
public class Tokai_Map extends CustomMainScreen implements OnClickListener,Runnable {

	Tokai_Map tokai_Map;
	ProgressDialog dialog;		
	TextView heading;
	Button back;
	JSONArray tokaiMapJSONArray;	
	int idValue,length,densityDpi,heightinPX,widthInPx;	
	float density,ratioOfScreens;		
	String id,name_en,name_jp,total,txt_heading;	

	//*********Following are the x and y positions of the Buttons(Tokai_Map state cities)
	int aligationOfX_Heading=250,aligationOfX_Gifu_Ken=200,aligationOfX_Aichi_Ken=170,aligationOfX_Shizuoka_Ken=360,aligationOfX_Mie_Ken=240;
	int aligationOfY_Heading=80,aligationOfY_Gifu_Ken=380,aligationOfY_Aichi_Ken=630,aligationOfY_Shizuoka_Ken=620,aligationOfY_Mie_Ken=730;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.tokai_Map=this;
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		LayoutInflater mInflater = LayoutInflater.from(this);		
		RelativeLayout setRelativeLayout=(RelativeLayout) mInflater.inflate(R.layout.tokai_map, null);
		container.addView(setRelativeLayout);	
		AbsoluteLayout absoluteLayoutObj=(AbsoluteLayout)this.findViewById(R.id.tokai_map_absoluteLayout);
		back=(Button)this.findViewById(R.id.btn_back_Tokai);
		back.setOnClickListener(this);
		heightinPX=MyUtility.getHeight(this);
		widthInPx=MyUtility.getWidth(this);
		dialog=MyUtility.progressDialog(tokai_Map, getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(this).start();        		

		idValue=getIntent().getIntExtra("idValue_Tokai", -1);//*********carry ID from main japan map	
		txt_heading=getIntent().getStringExtra("Heading_Tokai");//*********carry name from main japan map
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
						tokaiMapJSONArray=new JSONArray(response);
						length=tokaiMapJSONArray.length();
						if(length>0)
						{
							for(int i=0;i<length;i++){
								JSONObject jsonObject=tokaiMapJSONArray.getJSONObject(i);
								int total=jsonObject.getInt("total");
								
								//*********Calculating and fixing the position of "States" according to screen resolution
								
								if(jsonObject.getString("name_en").equals("Gifu-ken"))
								{	
									Button bt_Gifu_Ken=(Button)tokai_Map.findViewById(R.id.btn_gifu_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Gifu_Ken.setVisibility(Button.VISIBLE);
										bt_Gifu_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Gifu_Ken, aligationOfY_Gifu_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Gifu_Ken, aligationOfY_Gifu_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Gifu_Ken.setLayoutParams(layoutParams);
										bt_Gifu_Ken.setTextSize(12);	
										bt_Gifu_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}						    	
								}
								//*********Setting Aichi_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Aichi-ken"))
								{
									Button bt_Aichi_Ken=(Button)tokai_Map.findViewById(R.id.btn_aichi_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Aichi_Ken.setVisibility(Button.VISIBLE);
										bt_Aichi_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Aichi_Ken, aligationOfY_Aichi_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Aichi_Ken, aligationOfY_Aichi_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Aichi_Ken.setLayoutParams(layoutParams);
										bt_Aichi_Ken.setTextSize(12);	
										bt_Aichi_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}
								}
								//*********Setting Shizuoka_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Shizuoka-ken"))
								{
									Button bt_Shizuoka_Ken=(Button)tokai_Map.findViewById(R.id.btn_shizuoka_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Shizuoka_Ken.setVisibility(Button.VISIBLE);
										bt_Shizuoka_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Shizuoka_Ken, aligationOfY_Shizuoka_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Shizuoka_Ken, aligationOfY_Shizuoka_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Shizuoka_Ken.setLayoutParams(layoutParams);
										bt_Shizuoka_Ken.setTextSize(12);	
										bt_Shizuoka_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}
								}
								//*********Setting Mie_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Mie-ken"))
								{
									Button bt_Mie_Ken=(Button)tokai_Map.findViewById(R.id.btn_mie_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Mie_Ken.setVisibility(Button.VISIBLE);
										bt_Mie_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Mie_Ken, aligationOfY_Mie_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Mie_Ken, aligationOfY_Mie_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Mie_Ken.setLayoutParams(layoutParams);
										bt_Mie_Ken.setTextSize(12);	
										bt_Mie_Ken.setOnClickListener(new OnClickListener() {

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
							MyUtility.showAlert(getResources().getString(R.string.no_vacant_houses), tokai_Map);						}
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
				public void run() 
				{					
					dialog.dismiss();
					MyUtility.netWorkDetail(tokai_Map);
				}
			});
		}		
	}

	public void onClick(View v) {

		if(v.getId()==back.getId())
		{
			this.finish();
		}
	}

	//*********This method is use to open the next class/screen and sends the Id value to other class  *****//
	private void nextActivity(String id)
	{
		Intent intent=new Intent(tokai_Map, PropertyList.class);
		intent.putExtra("Id", id);
		tokai_Map.startActivity(intent);
	}
	private void dataNotFoundActivity(){		
		//MyUtility.showAlert("Data not found.", searchResultScreen);
		new AlertDialog.Builder(tokai_Map)
		.setMessage(getResources().getString(R.string.unableToFetchContent_jp))
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				tokai_Map.finish();
			}
		}).show();
	
}
}