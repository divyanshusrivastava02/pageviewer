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

import com.util.HTTPConnect;
import com.util.MyUtility;
/*
 * This Activity is showing the property in japan province
 */
public class JapanMap extends CustomMainScreen implements Runnable {

	JapanMap japanMap;
	ProgressDialog dialog;	
	JSONArray japanMapJSONArray;
	int densityDpi;
	float density;
	int heightinPX,widthInPx;		

	//*********Following are the x and y positions of the Buttons(States) 
	final int aligationOfX_Hokkaido=380,aligationOfX_Okinawa=130,aligationOfX_Tohoku=380,aligationOfX_Hokuriku=240,aligationOfX_Koshin_etsu=300,aligationOfX_Chugoku=10,aligationOfX_Kinki=180,aligationOfX_Kanto=370,aligationOfX_Tokai=260,aligationOfX_Shikoku=120,aligationOfX_Kyushu=20;
	final int aligationOfY_Hokkaido=160,aligationOfY_Okinawa=280,aligationOfY_Tohoku=310,aligationOfY_Hokuriku=420,aligationOfY_Koshin_etsu=490,aligationOfY_Chugoku=540,aligationOfY_Kinki=570,aligationOfY_Kanto=550,aligationOfY_Tokai=630,aligationOfY_Shikoku=680,aligationOfY_Kyushu=740;

	final float target_Width_forRatioFinding=600;//we set the target screen width to 600 so that we can compare rest resolution with the given one
	final float target_Height_forRatioFinding=1024;//we set the target screen height to 1024 so that we can compare rest resolution with the given one
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.japanMap=this;		
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		LayoutInflater mInflater = LayoutInflater.from(this);		
		RelativeLayout setRelativeLayout=(RelativeLayout) mInflater.inflate(R.layout.japan_map, null);
		container.addView(setRelativeLayout);	
		AbsoluteLayout absoluteLayoutObj=(AbsoluteLayout)this.findViewById(R.id.japan_map_absoluteLayout);

		//**********getting the height and width from MyUtility class *****//
		heightinPX=MyUtility.getHeight(this);
		widthInPx=MyUtility.getWidth(this);
	
		//********** Loading dialog box starts here ***********//
		dialog=MyUtility.progressDialog(japanMap, getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(this).start();	
		
	}
	public void run() {
		try
		{
			HTTPConnect hc=new HTTPConnect();     
			String url=MyUtility.serverAddress; 
			
			//*******Here we are getting the url response  ******// 
			final String response=hc.openHttpConnection(url);			
			this.runOnUiThread(new Runnable() 
			{
				public void run() 
				{
					//********** Loading dialog box Finished  ***********//
					dialog.dismiss();
					try
					{
						japanMapJSONArray=new JSONArray(response);

						//******** Getting the length of the response *********//						
						int length=japanMapJSONArray.length();
						if(length>0)
						{
							//******** Here we initialized the condition, so that all the values are retrieved ********//
							for(int i=0;i<length;i++)
							{
								JSONObject jsonObject=japanMapJSONArray.getJSONObject(i);
								int total=jsonObject.getInt("total");

								//*********Calculating and fixing the position of "States" according to screen resolution

								if(jsonObject.getString("name_en").equals("Tohoku"))
								{	
									final Button bt_Tohoku=(Button)japanMap.findViewById(R.id.btn_Tohoku);
									if(jsonObject.getInt("total")>0)
									{
										bt_Tohoku.setVisibility(Button.VISIBLE); //Setting the button visible if there is any vacant houses are available
										bt_Tohoku.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Tohoku, aligationOfY_Tohoku,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Tohoku, aligationOfY_Tohoku,widthInPx,heightinPX);
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										final String name_Tohoku=jsonObject.getString("name_jp");
										final int idValue_Tohoku=jsonObject.getInt("id");
										bt_Tohoku.setLayoutParams(layoutParams);
										bt_Tohoku.setTextSize(12);	
										bt_Tohoku.setOnClickListener(new OnClickListener() {
											//values are sending to cities map
											public void onClick(View v) 
											{ 
												Intent i=new Intent(japanMap,Tohoku_Map.class);
												i.putExtra("idValue_Tohoku", idValue_Tohoku);
												i.putExtra("Heading_Tohoku", name_Tohoku);
												japanMap.startActivity(i);
											}
										});
									}						    	
								}

								//*********Setting Koshin-etsu button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Koshin-etsu")){
									Button bt_Koshin_etsu=(Button)japanMap.findViewById(R.id.btn_Koshin_etsu);
									if(jsonObject.getInt("total")>0)
									{
										//************ setting the button visible if there are vacant houses are available in that state *******//
										bt_Koshin_etsu.setVisibility(Button.VISIBLE);
										bt_Koshin_etsu.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Koshin_etsu, aligationOfY_Koshin_etsu,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Koshin_etsu, aligationOfY_Koshin_etsu,widthInPx,heightinPX);
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										final int idValue_Koshin=jsonObject.getInt("id");
										final String name_Koshin=jsonObject.getString("name_jp");
										bt_Koshin_etsu.setLayoutParams(layoutParams);
										bt_Koshin_etsu.setTextSize(12);	
										bt_Koshin_etsu.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{	
												/*  Opening the new screen when user 
												  click on the button and sending the Id 
												 value and the name to the next screen */

												Intent i=new Intent(japanMap,Koshin_etsu_Map.class);
												System.out.println("IDVALUE:::::"+idValue_Koshin);
												i.putExtra("idValue_Koshin_etsu", idValue_Koshin);			
												i.putExtra("Heading_Koshin_etsu", name_Koshin);
												japanMap.startActivity(i);												
											}
										});
									}
								}

								//*********Setting Kanto button according to the screen resolution******//
								else if(jsonObject.getString("name_en").equals("Kanto")){
									Button bt_Kanto=(Button)japanMap.findViewById(R.id.btn_Kanto);
									if(jsonObject.getInt("total")>0)
									{
										bt_Kanto.setVisibility(Button.VISIBLE);
										bt_Kanto.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Kanto, aligationOfY_Kanto,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Kanto, aligationOfY_Kanto,widthInPx,heightinPX);
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										final int idValue_Kanto=jsonObject.getInt("id");
										final String name_Kanto=jsonObject.getString("name_jp");
										bt_Kanto.setLayoutParams(layoutParams);
										bt_Kanto.setTextSize(12);	
										bt_Kanto.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{												
												Intent i=new Intent(japanMap,Kanto_Map.class);
												System.out.println("IDVALUE:::::"+idValue_Kanto);
												i.putExtra("idValue_Kanto", idValue_Kanto);	
												i.putExtra("Heading_Kanto", name_Kanto);
												japanMap.startActivity(i);

											}
										});
									}
								}

								//*********Setting Kinki button according to the screen resolution******//

								else if(jsonObject.getString("name_en").equals("Kinki")){
									Button bt_Kinki=(Button)japanMap.findViewById(R.id.btn_Kinki);
									if(jsonObject.getInt("total")>0)
									{
										bt_Kinki.setVisibility(Button.VISIBLE);
										bt_Kinki.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Kinki, aligationOfY_Kinki,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Kinki, aligationOfY_Kinki,widthInPx,heightinPX);
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										final int idValue_Kinki=jsonObject.getInt("id");
										final String name_Kinki=jsonObject.getString("name_jp");
										bt_Kinki.setLayoutParams(layoutParams);
										bt_Kinki.setTextSize(12);	
										bt_Kinki.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{
												Intent i=new Intent(japanMap,Kinki_Map.class);
												System.out.println("IDVALUE:::::"+idValue_Kinki);
												i.putExtra("idValue_Kinki", idValue_Kinki);		
												i.putExtra("Heading_Kinki", name_Kinki);		
												japanMap.startActivity(i);
											}
										});
									}
								}

								//*********Setting Chugoku button according to the screen resolution******//

								else if(jsonObject.getString("name_en").equals("Chugoku")){
									Button bt_Chugoku=(Button)japanMap.findViewById(R.id.btn_Chugoku);
									if(jsonObject.getInt("total")>0)
									{
										bt_Chugoku.setVisibility(Button.VISIBLE);
										bt_Chugoku.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Chugoku, aligationOfY_Chugoku,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Chugoku, aligationOfY_Chugoku,widthInPx,heightinPX);
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										final int idValue_Chugoku=jsonObject.getInt("id");
										final String name_Chugoku=jsonObject.getString("name_jp");
										bt_Chugoku.setLayoutParams(layoutParams);
										bt_Chugoku.setTextSize(12);	
										bt_Chugoku.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{
												Intent i=new Intent(japanMap,Chugoku_Map.class);
												System.out.println("IDVALUE:::::"+idValue_Chugoku);
												i.putExtra("idValue_Chugoku", idValue_Chugoku);	
												i.putExtra("Heading_Chugoku", name_Chugoku);	
												japanMap.startActivity(i);
											}
										});
									}
								}

								//*********Setting Shikoku button according to the screen resolution******//

								else if(jsonObject.getString("name_en").equals("Shikoku")){
									Button bt_Shikoku=(Button)japanMap.findViewById(R.id.btn_Shikoku);
									if(jsonObject.getInt("total")>0)
									{
										bt_Shikoku.setVisibility(Button.VISIBLE);
										bt_Shikoku.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Shikoku, aligationOfY_Shikoku,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Shikoku, aligationOfY_Shikoku,widthInPx,heightinPX);
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										final int idValue_Shikoku=jsonObject.getInt("id");
										final String name_Shikoku=jsonObject.getString("name_jp");
										bt_Shikoku.setLayoutParams(layoutParams);
										bt_Shikoku.setTextSize(12);	
										bt_Shikoku.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{  
												Intent i=new Intent(japanMap,Shikoku_Map.class);
												System.out.println("IDVALUE:::::"+idValue_Shikoku);
												i.putExtra("idValue_Shikoku", idValue_Shikoku);	
												i.putExtra("Heading_Shikoku", name_Shikoku);	
												japanMap.startActivity(i);
											}
										});
									}
								}

								//*********Setting Kyushu button according to the screen resolution******//

								else if(jsonObject.getString("name_en").equals("Kyushu")){
									Button bt_Kyushu=(Button)japanMap.findViewById(R.id.btn_Kyushu);
									if(jsonObject.getInt("total")>0)
									{
										bt_Kyushu.setVisibility(Button.VISIBLE);
										bt_Kyushu.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Kyushu, aligationOfY_Kyushu,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Kyushu, aligationOfY_Kyushu,widthInPx,heightinPX);
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										final int idValue_Kyushu=jsonObject.getInt("id");
										final String name_Kyushu=jsonObject.getString("name_jp");
										bt_Kyushu.setLayoutParams(layoutParams);
										bt_Kyushu.setTextSize(12);	
										bt_Kyushu.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{
												Intent i=new Intent(japanMap,Kyushu_Map.class);
												System.out.println("IDVALUE:::::"+idValue_Kyushu);
												i.putExtra("idValue_Kyushu", idValue_Kyushu);		
												i.putExtra("Heading_Kyushu", name_Kyushu);
												japanMap.startActivity(i);
											}
										});
									}
								}

								//*********Setting Okinawa button according to the screen resolution******//

								else if(jsonObject.getString("name_en").equals("Okinawa")){
									Button bt_Okinawa=(Button)japanMap.findViewById(R.id.btn_Okinawa);
									if(jsonObject.getInt("total")>0)
									{
										bt_Okinawa.setVisibility(Button.VISIBLE);
										bt_Okinawa.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Okinawa, aligationOfY_Okinawa,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Okinawa, aligationOfY_Okinawa,widthInPx,heightinPX);
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										final int idValue_Okinawa=jsonObject.getInt("id");
										final String name_Okinawa=jsonObject.getString("name_jp");
										bt_Okinawa.setLayoutParams(layoutParams);
										bt_Okinawa.setTextSize(12);	
										bt_Okinawa.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{
												Intent i=new Intent(japanMap,Okinawa_Map.class);
												System.out.println("IDVALUE:::::"+idValue_Okinawa);
												i.putExtra("idValue_Okinawa_Ken",idValue_Okinawa);	
												i.putExtra("Heading_Okinawa_Ken",name_Okinawa);
												japanMap.startActivity(i);
											}
										});
									}
								}

								//*********Setting Hokkaido button according to the screen resolution******//

								else if(jsonObject.getString("name_en").equals("Hokkaido")){
									Button bt_Hokkaido=(Button)japanMap.findViewById(R.id.btn_Hokkaido);
									if(jsonObject.getInt("total")>0)
									{											
										bt_Hokkaido.setVisibility(Button.VISIBLE);
										bt_Hokkaido.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Hokkaido, aligationOfY_Hokkaido,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Hokkaido, aligationOfY_Hokkaido,widthInPx,heightinPX);
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										final int idValue_Hokkaido=jsonObject.getInt("id");
										final String name_Hokkaido=jsonObject.getString("name_jp");
										bt_Hokkaido.setLayoutParams(layoutParams);
										bt_Hokkaido.setTextSize(12);											
										bt_Hokkaido.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{
												Intent i=new Intent(japanMap,Hokkaido_Map.class);
												System.out.println("IDVALUE:::::"+idValue_Hokkaido);
												i.putExtra("idValue_Hokkaido", idValue_Hokkaido);	
												i.putExtra("Heading_Hokkaido", name_Hokkaido);
												japanMap.startActivity(i);
											}
										});
									}
								}

								//*********Setting Hokuriku button according to the screen resolution******//

								else if(jsonObject.getString("name_en").equals("Hokuriku")){
									Button bt_Hokuriku=(Button)japanMap.findViewById(R.id.btn_Hokuriku);									
									if(jsonObject.getInt("total")>0)
									{
										bt_Hokuriku.setVisibility(Button.VISIBLE);
										bt_Hokuriku.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Hokuriku, aligationOfY_Hokuriku,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Hokuriku, aligationOfY_Hokuriku,widthInPx,heightinPX);
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										final int idValue_Hokuriku=jsonObject.getInt("id");
										final String name_Hokuriku=jsonObject.getString("name_jp");
										bt_Hokuriku.setLayoutParams(layoutParams);
										bt_Hokuriku.setTextSize(12);	
										bt_Hokuriku.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{																									Intent i=new Intent(japanMap,Hokuriku_Map.class);
											Intent j=new Intent(japanMap,Hokuriku_Map.class);
											System.out.println("IDVALUE:::::"+idValue_Hokuriku);
											j.putExtra("idValue_Hokuriku", idValue_Hokuriku);	
											j.putExtra("Heading_Hokuriku", name_Hokuriku);
											japanMap.startActivity(j);
											}
										});
									}
								}

								//*********Setting Tokai button according to the screen resolution******//

								else if(jsonObject.getString("name_en").equals("Tokai")){
									Button bt_Tokai=(Button)japanMap.findViewById(R.id.btn_Tokai);
									if(jsonObject.getInt("total")>0)
									{
										bt_Tokai.setVisibility(Button.VISIBLE);
										bt_Tokai.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Tokai, aligationOfY_Tokai,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Tokai, aligationOfY_Tokai,widthInPx,heightinPX);
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										final int idValue_Tokai=jsonObject.getInt("id");
										final String name_Tokai=jsonObject.getString("name_jp");
										bt_Tokai.setLayoutParams(layoutParams);
										bt_Tokai.setTextSize(12);	
										bt_Tokai.setOnClickListener(new OnClickListener() {

											public void onClick(View v) 
											{
												Intent i=new Intent(japanMap,Tokai_Map.class);
												System.out.println("IDVALUE:::::"+idValue_Tokai);
												i.putExtra("idValue_Tokai", idValue_Tokai);	
												i.putExtra("Heading_Tokai", name_Tokai);	
												japanMap.startActivity(i);
											}
										});
									}
								}
							}
						}
						else
						{
							MyUtility.showAlert(getResources().getString(R.string.no_vacant_houses), japanMap);

						}
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
						MyUtility.netWorkDetail(japanMap);						
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
					MyUtility.netWorkDetail(japanMap);
				}
			});
		}
	}

	private void dataNotFoundActivity(){		

		new AlertDialog.Builder(japanMap)
		.setMessage(getResources().getString(R.string.unableToFetchContent_jp))
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				japanMap.finish();
			}
		}).show();

	}

}