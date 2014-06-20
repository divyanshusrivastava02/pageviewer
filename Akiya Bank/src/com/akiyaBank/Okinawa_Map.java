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
 * This Activity is showing the property in Okinawa province
 */
public class Okinawa_Map extends CustomMainScreen implements OnClickListener,Runnable {

	Okinawa_Map okinawa_Map;
	ProgressDialog dialog;	
	TextView heading;
	Button back;
	JSONArray okinawaMapJSONArray;
	int idValue,length,densityDpi,heightinPX,widthInPx;	
	float density,ratioOfScreens;	
	String id,name_en,name_jp,total,txt_heading;

	//*********Following are the x and y positions of the Buttons(Okinawa_Map state cities)
	int aligationOfX_Okinawa_ken=240,aligationOfX_Heading=250;
	int aligationOfY_Okinawa_ken=400,aligationOfY_Heading=20;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.okinawa_Map=this;		
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		LayoutInflater mInflater = LayoutInflater.from(this);		
		RelativeLayout setRelativeLayout=(RelativeLayout) mInflater.inflate(R.layout.okinawa_map, null);
		container.addView(setRelativeLayout);	
		AbsoluteLayout absoluteLayoutObj=(AbsoluteLayout)this.findViewById(R.id.okinawa_map_absoluteLayout);
		back=(Button)this.findViewById(R.id.btn_back_Okinawa);
		back.setOnClickListener(this);

		idValue=getIntent().getIntExtra("idValue_Okinawa_Ken", -1);	//*********carry ID from main japan map
		txt_heading=getIntent().getStringExtra("Heading_Okinawa_Ken");	//*********carry name from main japan map	
		heightinPX=MyUtility.getHeight(this);
		widthInPx=MyUtility.getWidth(this);
		dialog=MyUtility.progressDialog(okinawa_Map, getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(this).start();   
	}

	public void run() {		
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
						okinawaMapJSONArray=new JSONArray(response);
						length=okinawaMapJSONArray.length();
						if(length>0)
						{
							for(int i=0;i<length;i++){
								JSONObject jsonObject=okinawaMapJSONArray.getJSONObject(i);
								int total=jsonObject.getInt("total");				
								
								//*********Calculating and fixing the position of "States" according to screen resolution
								
								if(jsonObject.getString("name_en").equals("Okinawa-ken"))
								{	
									Button bt_Okinawa_ken=(Button)okinawa_Map.findViewById(R.id.btn_okinawa_ken);										
									if(jsonObject.getInt("total")>0)
									{
										bt_Okinawa_ken.setVisibility(Button.VISIBLE);
										bt_Okinawa_ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Okinawa_ken, aligationOfY_Okinawa_ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Okinawa_ken, aligationOfY_Okinawa_ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Okinawa_ken.setLayoutParams(layoutParams);
										bt_Okinawa_ken.setTextSize(12);	
										bt_Okinawa_ken.setOnClickListener(new OnClickListener() {

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
							MyUtility.showAlert(getResources().getString(R.string.no_vacant_houses), okinawa_Map);
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
					MyUtility.netWorkDetail(okinawa_Map);
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
		Intent intent=new Intent(okinawa_Map, PropertyList.class);
		intent.putExtra("Id", id);
		okinawa_Map.startActivity(intent);
	}
	private void dataNotFoundActivity(){		
		//MyUtility.showAlert("Data not found.", searchResultScreen);
		new AlertDialog.Builder(okinawa_Map)
		.setMessage(getResources().getString(R.string.unableToFetchContent_jp))
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				okinawa_Map.finish();
			}
		}).show();
	
}
}