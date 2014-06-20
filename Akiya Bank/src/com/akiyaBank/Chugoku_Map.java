package com.akiyaBank;

import java.text.ChoiceFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
 * This Activity is showing the property in Chugoku province
 */
public class Chugoku_Map extends CustomMainScreen implements OnClickListener,Runnable {

	Chugoku_Map chugoku_Map;
	ProgressDialog dialog;
	TextView heading;
	int idValue,length,densityDpi,heightinPX,widthInPx;	
	float density;	
	JSONArray chugokuMapJSONArray;		
	Button bt_Tottori_Ken,bt_Shimane_Ken,bt_Okayama_Ken,bt_Hiroshima_Ken,bt_Yamaguchi_Ken;
	String id,name_en,name_jp,total,txt_heading;	

	//*********Following are the x and y positions of the Buttons(Chugoku state cities) 
	int aligationOfX_Heading=250,aligationOfX_Tottori_Ken=400,aligationOfX_Shimane_Ken=170,aligationOfX_Okayama_Ken=410,aligationOfX_Hiroshima_Ken=250,aligationOfX_Yamaguchi_Ken=20;
	int aligationOfY_Heading=80,aligationOfY_Tottori_Ken=370,aligationOfY_Shimane_Ken=430,aligationOfY_Okayama_Ken=460,aligationOfY_Hiroshima_Ken=550,aligationOfY_Yamaguchi_Ken=570;
	float ratioOfScreens;
	Button back;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.chugoku_Map=this;	
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		LayoutInflater mInflater = LayoutInflater.from(this);		
		RelativeLayout setRelativeLayout=(RelativeLayout) mInflater.inflate(R.layout.chugoku_map, null);
		container.addView(setRelativeLayout);	
		AbsoluteLayout  absoluteLayoutObj=(AbsoluteLayout)this.findViewById(R.id.chugoku_map_absoluteLayout);
		back=(Button)this.findViewById(R.id.btn_back_Chugoku);
		back.setOnClickListener(this);		
		heightinPX=MyUtility.getHeight(this);
		widthInPx=MyUtility.getWidth(this);
		dialog=MyUtility.progressDialog(chugoku_Map, getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(this).start();      
		idValue=getIntent().getIntExtra("idValue_Chugoku", -1);	//*********Receiving ID from main japan map
		txt_heading=getIntent().getStringExtra("Heading_Chugoku");	//*********Receiving name from main japan map	
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
						chugokuMapJSONArray=new JSONArray(response);
						length=chugokuMapJSONArray.length();
						if(length>0)
						{
							for(int i=0;i<length;i++){
								JSONObject jsonObject=chugokuMapJSONArray.getJSONObject(i);
								int total=jsonObject.getInt("total");	
								
								//*********Calculating and fixing the position of "States" according to screen resolution *****//
								if(jsonObject.getString("name_en").equals("Tottori-ken"))
								{	
									Button bt_Tottori_Ken=(Button)chugoku_Map.findViewById(R.id.btn_tottori_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Tottori_Ken.setVisibility(Button.VISIBLE);
										bt_Tottori_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Tottori_Ken, aligationOfY_Tottori_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Tottori_Ken, aligationOfY_Tottori_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Tottori_Ken.setLayoutParams(layoutParams);
										bt_Tottori_Ken.setTextSize(12);	
										bt_Tottori_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}						    	
								}

								//*********Setting Shimane button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Shimane-ken"))
								{
									bt_Shimane_Ken=(Button)chugoku_Map.findViewById(R.id.btn_shimane_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Shimane_Ken.setVisibility(Button.VISIBLE);
										bt_Shimane_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Shimane_Ken, aligationOfY_Shimane_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Shimane_Ken, aligationOfY_Shimane_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Shimane_Ken.setLayoutParams(layoutParams);
										bt_Shimane_Ken.setTextSize(12);	
										bt_Shimane_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}
								}

								//*********Setting Hiroshima button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Hiroshima-ken"))
								{
									Button bt_Hiroshima_Ken=(Button)chugoku_Map.findViewById(R.id.btn_hiroshima_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Hiroshima_Ken.setVisibility(Button.VISIBLE);
										bt_Hiroshima_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Hiroshima_Ken, aligationOfY_Hiroshima_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Hiroshima_Ken, aligationOfY_Hiroshima_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Hiroshima_Ken.setLayoutParams(layoutParams);
										bt_Hiroshima_Ken.setTextSize(12);	
										bt_Hiroshima_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}
								}

								//*********Setting Yamaguchi button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Yamaguchi-ken"))
								{
									Button bt_Yamaguchi_Ken=(Button)chugoku_Map.findViewById(R.id.btn_yamaguchi_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Yamaguchi_Ken.setVisibility(Button.VISIBLE);
										bt_Yamaguchi_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Yamaguchi_Ken, aligationOfY_Yamaguchi_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Yamaguchi_Ken, aligationOfY_Yamaguchi_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Yamaguchi_Ken.setLayoutParams(layoutParams);
										bt_Yamaguchi_Ken.setTextSize(12);	
										bt_Yamaguchi_Ken.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{ 
												nextActivity(id);
											}
										});
									}
								}

								//*********Setting Okayama button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Okayama-ken"))
								{
									Button bt_Okayama_Ken=(Button)chugoku_Map.findViewById(R.id.btn_okayama_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Okayama_Ken.setVisibility(Button.VISIBLE);
										bt_Okayama_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Okayama_Ken, aligationOfY_Okayama_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Okayama_Ken, aligationOfY_Okayama_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Okayama_Ken.setLayoutParams(layoutParams);
										bt_Okayama_Ken.setTextSize(12);	
										bt_Okayama_Ken.setOnClickListener(new OnClickListener() {

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
							MyUtility.showAlert(getResources().getString(R.string.no_vacant_houses), chugoku_Map);
						}
					}
					catch(Exception ex)
					{
						dataNotFoundActivity();
						ex.printStackTrace();
						Log.d("Error:::", "error name - " + ex.toString()+"error message"+ex.getMessage());
					}
				}
			});
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			this.runOnUiThread(new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(!MyUtility.isNetworkAvailable(chugoku_Map))
					{
						MyUtility.showAlert("Unable to connect to internet, Please make sure you are conneted to data service or wifi.", chugoku_Map);
					}
					else
					{
						MyUtility.showAlert("Unable to fetch content, please try again.", chugoku_Map);
					}
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
		Intent intent=new Intent(chugoku_Map, PropertyList.class);
		intent.putExtra("Id", id);
		chugoku_Map.startActivity(intent);
	}
	
	private void dataNotFoundActivity(){		
			
			new AlertDialog.Builder(chugoku_Map)
			.setMessage(getResources().getString(R.string.unableToFetchContent_jp))
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					chugoku_Map.finish();
				}
			}).show();
		
	}
}