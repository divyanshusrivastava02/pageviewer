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
 * This Activity is showing the property in Koshin-etsu province
 */
public class Koshin_etsu_Map extends CustomMainScreen implements OnClickListener,Runnable {

	Koshin_etsu_Map koshin_etsu_Map;
	ProgressDialog dialog;
	TextView heading;
	Button back;
	JSONArray koshin_etsuMapJSONArray;	
	int idValue,length,densityDpi,heightinPX,widthInPx;	
	float density,ratioOfScreens; 
	String id,name_en,name_jp,total,txt_heading;

	//*********Following are the x and y positions of the Buttons(Koshin_etsu_Map state cities)
	int aligationOfX_Heading=250,aligationOfX_Niigata_Ken=310,aligationOfX_Nagano_Ken=270,aligationOfX_Yamanashi_Ken=350;
	int aligationOfY_Heading=80,aligationOfY_Niigata_Ken=380,aligationOfY_Nagano_Ken=590,aligationOfY_Yamanashi_Ken=730;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.koshin_etsu_Map=this;	
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		LayoutInflater mInflater = LayoutInflater.from(this);		
		RelativeLayout setRelativeLayout=(RelativeLayout) mInflater.inflate(R.layout.koshin_etsu_map, null);
		container.addView(setRelativeLayout);	
		AbsoluteLayout absoluteLayoutObj=(AbsoluteLayout)this.findViewById(R.id.koshin_etsu_map_absoluteLayout);
		back=(Button)this.findViewById(R.id.btn_back_Koshin_etsu);
		back.setOnClickListener(this);
		heightinPX=MyUtility.getHeight(this);
		widthInPx=MyUtility.getWidth(this);
		dialog=MyUtility.progressDialog(koshin_etsu_Map, getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(this).start();        		

		idValue=getIntent().getIntExtra("idValue_Koshin_etsu", -1);	//*********carry ID from main japan map
		txt_heading=getIntent().getStringExtra("Heading_Koshin_etsu");//*********carry name from main japan map		
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
						koshin_etsuMapJSONArray=new JSONArray(response);
						length=koshin_etsuMapJSONArray.length();
						if(length>0)
						{
							for(int i=0;i<length;i++){
								JSONObject jsonObject=koshin_etsuMapJSONArray.getJSONObject(i);
								int total=jsonObject.getInt("total");
								
								//*********Calculating and fixing the position of "States" according to screen resolution
								
								if(jsonObject.getString("name_en").equals("Niigata-ken"))
								{	
									Button bt_Niigata_Ken=(Button)koshin_etsu_Map.findViewById(R.id.btn_Niigata_Ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Niigata_Ken.setVisibility(Button.VISIBLE);
										bt_Niigata_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Niigata_Ken, aligationOfY_Niigata_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Niigata_Ken, aligationOfY_Niigata_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);

										bt_Niigata_Ken.setLayoutParams(layoutParams);
										bt_Niigata_Ken.setTextSize(12);	
										bt_Niigata_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}						    	
								}
								//*********Setting Yamanashi_Ken button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Yamanashi-ken"))
								{
									Button bt_Yamanashi_Ken=(Button)koshin_etsu_Map.findViewById(R.id.btn_Yamanashi_Ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Yamanashi_Ken.setVisibility(Button.VISIBLE);
										bt_Yamanashi_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Yamanashi_Ken, aligationOfY_Yamanashi_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Yamanashi_Ken, aligationOfY_Yamanashi_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);

										bt_Yamanashi_Ken.setLayoutParams(layoutParams);
										bt_Yamanashi_Ken.setTextSize(12);	
										bt_Yamanashi_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}
								}
								//*********Setting Nagano button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Nagano-ken"))
								{
									Button bt_Nagano_Ken=(Button)koshin_etsu_Map.findViewById(R.id.btn_Nagano_Ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Nagano_Ken.setVisibility(Button.VISIBLE);
										bt_Nagano_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Nagano_Ken, aligationOfY_Nagano_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Nagano_Ken, aligationOfY_Nagano_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);

										bt_Nagano_Ken.setLayoutParams(layoutParams);
										bt_Nagano_Ken.setTextSize(12);	
										bt_Nagano_Ken.setOnClickListener(new OnClickListener() {

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
							MyUtility.showAlert(getResources().getString(R.string.no_vacant_houses), koshin_etsu_Map);
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
					MyUtility.netWorkDetail(koshin_etsu_Map);
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
		Intent intent=new Intent(koshin_etsu_Map, PropertyList.class);
		intent.putExtra("Id", id);
		koshin_etsu_Map.startActivity(intent);
	}
	private void dataNotFoundActivity(){		
		
		new AlertDialog.Builder(koshin_etsu_Map)
		.setMessage(getResources().getString(R.string.unableToFetchContent_jp))
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				koshin_etsu_Map.finish();
			}
		}).show();
	
}
}