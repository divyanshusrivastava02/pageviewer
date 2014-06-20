package com.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

public class MyUtility 
{
	static float target_Width_forRatioFinding=600;
	static float target_Height_forRatioFinding=1024;	
	public static final String PROPERTYNAME="propertyName", PROPERTYIMAGE="propertyImage";
	public static int permissionSetter=1;
	MyUtility utility;	
	
	
	//public static String serverAddress="http://192.168.1.55/akiya/api/getStateData.php";
	public static String serverAddress="http://www.sabdekho.com/projects/akiya/api/getStateData.php";
	
	//public static String serverAddressForStates="http://192.168.1.55/akiya/api/getDistrictData.php?areaId=";
	public static String serverAddressForStates="http://www.sabdekho.com/projects/akiya/api/getDistrictData.php?areaId=";
	
	//public static String serverAddressForPropertyList="http://192.168.1.55/akiya/api/getDistrictInsight.php?aid=";
	public static String serverAddressForPropertyList="http://www.sabdekho.com/projects/akiya/api/getDistrictInsight.php?aid=";
	
	//public static String serverAddressForPropertyInfo="http://192.168.1.55/akiya/api/getHomeInsight.php?hid=";
	public static String serverAddressForPropertyInfo="http://www.sabdekho.com/projects/akiya/api/getHomeInsight.php?hid=";
	
	//url="http://www.sabdekho.com/projects/akiya/api/searchEstatelinks.php?q="+keyword;
	//public static String serverAddressForSearchByKeywords="http://192.168.1.55/akiya/api/searchKeywords.php?q=";
	public static String serverAddressForSearchByKeywords="http://www.sabdekho.com/projects/akiya/api/searchKeywords.php?q=";
	
	//public static String serverAddressForSearchByAddress="http://192.168.1.55/akiya/api/searchAddress.php?q=";
	//url="http://www.sabdekho.com/projects/akiya/api/searchEstatecommends.php?q="+keyword;
	public static String serverAddressForSearchByAddress="http://www.sabdekho.com/projects/akiya/api/searchAddress.php?q=";
	
	public static void showAlert(String message,Activity activity)
	{
		new AlertDialog.Builder(activity)

		.setMessage(message)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) 
			{
			}
		})

		.create().show();
	}

	//*********This function tells whether the device having an Internet connection or not*******///
	public static boolean isNetworkAvailable(Context context) 
	{
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		//System.out.println(activeNetworkInfo.);
		return activeNetworkInfo != null;
	}

	//*********For progress dialog box	
	public static ProgressDialog progressDialog(Activity activity,String message) 
	{
		if(message.equals(""))
			message="Please wait while loading..";
		ProgressDialog dialog= new ProgressDialog(activity);
		dialog.setMessage(message);
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		return dialog;
	}

	//*********Positioning the x coordinates of the button
	public static int calculateX(int x ,int y,int widthInPx, int heightinPX)
	{
		int returnValueX;
		float valX;
		if(target_Width_forRatioFinding!=widthInPx)
		{
			float ratioFinderX_Tohoku=(target_Width_forRatioFinding/x);
			valX= (widthInPx/ratioFinderX_Tohoku);
			returnValueX=(int)valX;
		}
		else{
			returnValueX=x;
		}
		return returnValueX;		
	}

	//*********Positioning the y coordinates of the button
	public static int calculateY(int x ,int y,int widthInPx,int heightinPX)
	{
		int returnValueY;
		float valY;
		if(target_Height_forRatioFinding!=heightinPX)
		{
			float ratioFinderY_Tohoku=(target_Height_forRatioFinding/y);
			valY=(heightinPX/ratioFinderY_Tohoku);
			returnValueY=(int)valY-20;
		}
		else{
			returnValueY=y;
		}	
		return returnValueY;
	}

	//*********Getting the width of the screen
	public static int getWidth(Activity context)
	{
		DisplayMetrics dmWidth = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dmWidth);
		return dmWidth.widthPixels;
	}

	//*********Getting the height of the screen
	public static int getHeight(Activity context)
	{
		DisplayMetrics dmHeight = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dmHeight);		
		return dmHeight.heightPixels;

	}

	//*********Getting the density of the screen
	public static int getDensity(Activity context)
	{
		DisplayMetrics dmDensity = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dmDensity);		
		float density = dmDensity.density;
		return (int) density;

	}
	
	public static String[] bookmarkValue={	
		MyUtility.PROPERTYIMAGE
		
		};
	
	

	public static String getPropertyName(Context context)
	{
		SharedPreferences settings = context.getSharedPreferences(MyUtility.PROPERTYIMAGE, 0);
		String silent = settings.getString("PropertyImage", "Null");
		return silent;
	}
	
	public static void netWorkDetail(Activity context){
		
		if(!MyUtility.isNetworkAvailable(context))
		{
			MyUtility.showAlert("Unable to connect to internet, Please make sure you are conneted to data service or wifi.", context);
		}
		else
		{
			MyUtility.showAlert("Unable to fetch content...., please try again.", context);
		}
	}
	class ViewHolder
	{		
		public String propertyTitle;
		public String propertyImage;
		public String propertyPlace;
		
	}

	

}