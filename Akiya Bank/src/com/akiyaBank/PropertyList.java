package com.akiyaBank;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akiyaBank.ImageThreadLoader.ImageLoadedListener;
import com.util.HTTPConnect;
import com.util.MyUtility;
/*
 * This Activity is showing the property List
 */
public class PropertyList extends CustomMainScreen implements Runnable,OnClickListener
{
	PropertyList propertyList;
	LayoutInflater inflater;
	ProgressDialog dialog;
	ArrayList<ViewHolder> DATA;
	Button backButton;
	String id;
	ImageThreadLoader imageLoader;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.propertyList=this;
		inflater=propertyList.getLayoutInflater();
		imageLoader=new ImageThreadLoader(this);
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		RelativeLayout propertyListLayout=(RelativeLayout)inflater.inflate(R.layout.property_list_layout, null);
		container.addView(propertyListLayout);
		backButton=(Button)propertyList.findViewById(R.id.property_list_back_button);
		backButton.setOnClickListener(propertyList);	
		DATA=new ArrayList<ViewHolder>();
		id=getIntent().getStringExtra("Id");
		//**** Dialog box started*****//
		dialog=MyUtility.progressDialog(propertyList, getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(propertyList).start();
	}


	public void run() 
	{
		try
		{
			createList();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			propertyList.runOnUiThread(new Runnable() 
			{				

				public void run() 
				{
					dialog.dismiss();
					if(!MyUtility.isNetworkAvailable(propertyList))
					{
						MyUtility.showAlert("Unable to connect to internet, Please make sure you are conneted to data service or wifi.", propertyList);
					}
					else
					{
						MyUtility.showAlert("Unable to fetch content, please try again.", propertyList);
					}
				}
			});
		}
	}

	public void createList() throws Exception
	{
		DATA.clear();				
		String url=MyUtility.serverAddressForPropertyList+id;
		
		//*******Here we are getting the url response  ******// 
		HTTPConnect connect=new HTTPConnect();
		final String response=connect.openHttpConnection(url);
		propertyList.runOnUiThread(new Runnable() 
		{			

			public void run() 
			{
				try
				{
					dialog.dismiss();
					
					//**** Dialog box Finished*****//
					System.out.println("Response: "+response);
					JSONArray propertyJsonArray=new JSONArray(response);
					for(int i=0;i<propertyJsonArray.length();i++)
					{						
						JSONObject propertyJsonObject=propertyJsonArray.getJSONObject(i);
						if(propertyJsonObject.has("message"))
						{
							MyUtility.showAlert("Area not in the database.", propertyList);
						}
						else
						{
							//******** Storing all the values in the View holder *********//
							ViewHolder holder=new ViewHolder();
							holder.id=propertyJsonObject.getString("id");							
							holder.title=propertyJsonObject.getString("title");							
							holder.thumb_pict=propertyJsonObject.getString("thumb_pict");
							holder.main_pict=propertyJsonObject.getString("main_pict");
							holder.place=propertyJsonObject.getString("place");							
							holder.url=propertyJsonObject.getString("url");							
							DATA.add(holder);
						}
					}
					ListView propertyListView=(ListView)propertyList.findViewById(R.id.property_list_listview);
					EfficientAdapter efficientAdapter=new EfficientAdapter(propertyList, DATA);
					propertyListView.setAdapter(efficientAdapter);
					efficientAdapter.notifyDataSetChanged();
					propertyListView.invalidate();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					if(!MyUtility.isNetworkAvailable(propertyList))
					{
						MyUtility.showAlert("No Network.", propertyList);
					}
					else
					{
						MyUtility.showAlert("There is some problem, please try again.", propertyList);
					}
				}
			}
		});
	}
	
	class ViewHolder
	{
		String id,title,thumb_pict,main_pict,place,url;
	}
	
	public class EfficientAdapter extends BaseAdapter
	{
		LayoutInflater inflater;
		ArrayList<ViewHolder> DATA;
		public EfficientAdapter(Context context,ArrayList<ViewHolder> DATA)
		{
			inflater=LayoutInflater.from(context);
			this.DATA=DATA;
		}

		public int getCount() 
		{			
			return DATA.size();
		}


		public Object getItem(int position) 
		{			
			return position;
		}


		public long getItemId(int position) 
		{			
			return position;
		}


		public View getView(int position,View convertView, ViewGroup parent) 
		{
			final ViewHolder temp=(ViewHolder)DATA.get(position);
			if(convertView==null)
			{
				convertView=inflater.inflate(R.layout.property_list_row_layout, null);
			}
			((TextView)convertView.findViewById(R.id.property_list_row_title)).setText(temp.title);
			((TextView)convertView.findViewById(R.id.property_list_row_place)).setText(temp.place);
			//((TextView)convertView.findViewById(R.id.property_list_row_url)).setText(temp.url);
			final ImageView imageView=(ImageView)convertView.findViewById(R.id.property_row_imageview);
			final ProgressBar spinner=((ProgressBar)convertView.findViewById(R.id.property_row_spinner));
			spinner.setVisibility(ProgressBar.VISIBLE);
			imageView.setImageBitmap(null);
			final String picUrl="http://"+temp.thumb_pict;

			//********Loading the Image with bitmap and using the spinner **********//
			Bitmap cachedImage;
			try {
				cachedImage = imageLoader.loadImage(picUrl, new ImageLoadedListener() 
				{
					public void imageLoaded(Bitmap imageBitmap) 
					{
						spinner.setVisibility(ProgressBar.GONE);
						imageView.setVisibility(ImageView.VISIBLE);
						imageView.setImageBitmap(imageBitmap);
						notifyDataSetChanged(); 
					}
				});
				if(cachedImage!=null)
				{
					spinner.setVisibility(ProgressBar.GONE);
					imageView.setVisibility(ImageView.VISIBLE);
					imageView.setImageBitmap(cachedImage);
				}
			} catch (MalformedURLException e) {				
				e.printStackTrace();
			}

			((RelativeLayout)convertView.findViewById(R.id.property_list_row_container)).setOnClickListener(new OnClickListener() 
			{				

				public void onClick(View v) 
				{
					System.out.println("Clicked");
					Intent intent=new Intent(propertyList, PropertyInfo.class);
					intent.putExtra("Id", temp.id);
					intent.putExtra("title", temp.title);
					propertyList.startActivity(intent);
				}
			});
			return convertView;
		}		
	}

	//************ This method is used for the Image functionality ************//
	public void imageOperations(String url, String saveFilename) 
	{

		try 
		{
			InputStream is = (InputStream) fetch(url);
			final Drawable d = Drawable.createFromStream(is, "src");
			propertyList.runOnUiThread(new Runnable() 
			{
				public void run() 
				{
					
				}
			});
		}
		catch (Exception e) 
		{
			System.out.println("Exception "+e.toString());

		}
	}

	//************ This method is used for Fetching the image from url *************//
	public static Object fetch(String address) throws MalformedURLException,IOException 
	{
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}


	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==backButton.getId())
		{
			propertyList.finish();
		}
		
	}


}