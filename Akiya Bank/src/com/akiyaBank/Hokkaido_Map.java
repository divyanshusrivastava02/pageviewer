package com.akiyaBank;

import org.json.JSONArray;
import org.json.JSONObject;

import com.util.HTTPConnect;
import com.util.MyUtility;

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
/*
 * This Activity is showing the property in Hokkaido province
 */
public class Hokkaido_Map extends CustomMainScreen implements OnClickListener,Runnable {

	Hokkaido_Map hokkaido_Map;
	ProgressDialog dialog;
	TextView heading;
	Button back;
	String name_Hokkaido_Ken;	
	Button bt_Hokkaido_Ken;
	JSONArray hokkaidoMapJSONArray;	
	int idValue,length,densityDpi,heightinPX,widthInPx;	
	float density,ratioOfScreens;		
	String id,name_en,name_jp,total,txt_heading;

	//*********Following are the x and y positions of the Buttons(Hokkaido_Map state cities) 
	int aligationOfX_Hokkaido_Ken=100,aligationOfX_Heading=210;
	int aligationOfY_Hokkaido_Ken=360,aligationOfY_Heading=20;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.hokkaido_Map=this;		
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		LayoutInflater mInflater = LayoutInflater.from(this);		
		RelativeLayout setRelativeLayout=(RelativeLayout) mInflater.inflate(R.layout.hokkaido_map, null);
		container.addView(setRelativeLayout);	
		AbsoluteLayout absoluteLayoutObj=(AbsoluteLayout)this.findViewById(R.id.hokkaido_map_absoluteLayout);
		back=(Button)this.findViewById(R.id.btn_back_Hokkaido);
		back.setOnClickListener(this);				
		heightinPX=MyUtility.getHeight(this);
		widthInPx=MyUtility.getWidth(this);
		dialog=MyUtility.progressDialog(hokkaido_Map,getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(this).start();        		

		idValue=getIntent().getIntExtra("idValue_Hokkaido", -1);     //*********carry ID from main japan map
		txt_heading=getIntent().getStringExtra("Heading_Hokkaido");	//*********carry name from main japan map		
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
						hokkaidoMapJSONArray=new JSONArray(response);
						length=hokkaidoMapJSONArray.length();
						if(length>0)
						{
							for(int i=0;i<length;i++){
								JSONObject jsonObject=hokkaidoMapJSONArray.getJSONObject(i);
								int total=jsonObject.getInt("total");
								
								//*********Calculating and fixing the position of "States" according to screen resolution
								
								if(jsonObject.getString("name_en").equals("Hokkaido"))
								{	
									Button bt_Hokkaido_Ken=(Button)hokkaido_Map.findViewById(R.id.btn_hokkaido_ken);
									if(jsonObject.getInt("total")>0)
									{
										bt_Hokkaido_Ken.setVisibility(Button.VISIBLE);
										bt_Hokkaido_Ken.setText(jsonObject.getString("name_jp")+" ("+total+")");
										int x=MyUtility.calculateX(aligationOfX_Hokkaido_Ken, aligationOfY_Hokkaido_Ken,widthInPx,heightinPX);
										int y=MyUtility.calculateY(aligationOfX_Hokkaido_Ken, aligationOfY_Hokkaido_Ken,widthInPx,heightinPX);
										final String id=jsonObject.getString("id");										
										AbsoluteLayout.LayoutParams layoutParams= new
										AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,x, y);
										bt_Hokkaido_Ken.setLayoutParams(layoutParams);
										bt_Hokkaido_Ken.setTextSize(12);	
										bt_Hokkaido_Ken.setOnClickListener(new OnClickListener() {

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
							MyUtility.showAlert(getResources().getString(R.string.no_vacant_houses), hokkaido_Map);
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
					MyUtility.netWorkDetail(hokkaido_Map);
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
		Intent intent=new Intent(hokkaido_Map, PropertyList.class);
		intent.putExtra("Id", id);
		hokkaido_Map.startActivity(intent);
	}
	
	private void dataNotFoundActivity(){		
		
		new AlertDialog.Builder(hokkaido_Map)
		.setMessage(getResources().getString(R.string.unableToFetchContent_jp))
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				hokkaido_Map.finish();
			}
		}).show();
	
}
}