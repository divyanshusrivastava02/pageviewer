package com.akiyaBank;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akiyaBank.ImageThreadLoader.ImageLoadedListener;
import com.util.HTTPConnect;
import com.util.MyUtility;
/*
 * This Activity is showing the property Information
 */
public class PropertyInfo extends CustomMainScreen implements Runnable,OnClickListener,OnItemClickListener
{
	PropertyInfo propertyInfo;
	LayoutInflater inflater;
	ProgressDialog dialog;
	String id;
	String propertyTitle;
	String propertyImage;
	String propertyPlace;
	ProgressBar mainImageSpinner;
	Button backButton,homeButton;
	ImageThreadLoader imageLoader;
	ArrayList<ViewHolder> DATA;	
	ArrayList<Akiya> names;
	StringBuilder sb;
	public DataHelper dh;
	Akiya objAkiya=new Akiya();

	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		this.propertyInfo=this;
		inflater=propertyInfo.getLayoutInflater();
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		RelativeLayout propertyInfoLayout=(RelativeLayout)inflater.inflate(R.layout.property_info, null);
		container.addView(propertyInfoLayout);		
		right_btn.setOnClickListener(custom);

		DATA=new ArrayList<ViewHolder>();
		imageLoader=new ImageThreadLoader(this);
		System.out.println("STORED VALUE:::"+MyUtility.PROPERTYIMAGE);
		fromExtras();
		backButton=(Button)propertyInfo.findViewById(R.id.property_info_back_button);
		backButton.setOnClickListener(propertyInfo);	

		homeButton=(Button)propertyInfo.findViewById(R.id.property_info_home_button);
		homeButton.setOnClickListener(propertyInfo);			

		mainImageSpinner=(ProgressBar)propertyInfo.findViewById(R.id.property_info_main_image_spinner);
		dialog=MyUtility.progressDialog(propertyInfo, getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(this).start();
	}
	private void fromExtras()
	{
		id=getIntent().getStringExtra("Id");
		TextView headingTextView=(TextView)propertyInfo.findViewById(R.id.property_info_heading);
		headingTextView.setText(getIntent().getStringExtra("title"));
	}

	public static Object fetch(String address) throws MalformedURLException,IOException 
	{
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

	public void run() 
	{
		try
		{
			getUrlData();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			propertyInfo.runOnUiThread(new Runnable() 
			{				

				public void run() 
				{
					dialog.dismiss();					
					if(!MyUtility.isNetworkAvailable(propertyInfo))
					{
						MyUtility.showAlert("Unable to connect to internet, Please make sure you are conneted to data service or wifi.", propertyInfo);
					}
					else
					{
						MyUtility.showAlert("Unable to fetch content, please try again.", propertyInfo);
					}
				}
			});
		}		
	}
	private void getUrlData() throws Exception
	{
		DATA.clear();
		String url=MyUtility.serverAddressForPropertyInfo+id;
		//*******Here we are getting the url response  ******// 
		HTTPConnect connect=new HTTPConnect();
		final String response=connect.openHttpConnection(url);
		propertyInfo.runOnUiThread(new Runnable() 
		{			

			public void run() 
			{
				try
				{
					dialog.dismiss();
					System.out.println("Response: "+response);
					JSONArray propertyJsonArray=new JSONArray(response);
					for(int i=0;i<propertyJsonArray.length();i++)
					{						
						JSONObject propertyJsonObject=propertyJsonArray.getJSONObject(i);
						if(propertyJsonObject.has("message"))
						{
							MyUtility.showAlert("Area not in the database.", propertyInfo);
						}
						else
						{
							final String mainPicUrl="http://"+propertyJsonObject.getString("main_pict");
							TextView descriptionTextView=(TextView)propertyInfo.findViewById(R.id.property_info_description_content);
							descriptionTextView.setText(propertyJsonObject.getString("body"));
							WebView specificationView=(WebView)propertyInfo.findViewById(R.id.property_info_specification_text);
							specificationView.getSettings().setDefaultFontSize(12);
							specificationView.loadDataWithBaseURL(null, propertyJsonObject.getString("table"), "text/html", "utf-8", "about:blank");
							//specificationTextView.setText(Html.fromHtml(propertyJsonObject.getString("table")));
							final ImageView mainImageView=(ImageView)propertyInfo.findViewById(R.id.property_info_main_image);
							final String propertyPlace=propertyJsonObject.getString("place");
							final String propertyTitle=propertyJsonObject.getString("title");
							final String propertyImage=propertyJsonObject.getString("thumb_pict");
							System.out.println("PROPERTY PLACE::::"+propertyPlace);
							System.out.println("PROPERTY Title::::"+propertyTitle);
							System.out.println("PROPERTY Image::::"+propertyImage);

							//********on click of image, open it in phone gallery app
							mainImageView.setOnClickListener(new OnClickListener() 
							{									
								public void onClick(View v) 
								{									
									Intent intent=new Intent(propertyInfo, OpenImage.class);
									intent.putExtra("img_url", mainPicUrl);
									propertyInfo.startActivity(intent);
								}
							});

							//********Loading the Image with bitmap and using the spinner **********//
							Bitmap cachedImage;
							try {
								cachedImage = imageLoader.loadImage(mainPicUrl, new ImageLoadedListener() 
								{
									public void imageLoaded(Bitmap imageBitmap) 
									{
										mainImageSpinner.setVisibility(ProgressBar.GONE);
										mainImageView.setVisibility(ImageView.VISIBLE);
										mainImageView.setImageBitmap(imageBitmap);

									}
								});
								if(cachedImage!=null)
								{
									mainImageSpinner.setVisibility(ProgressBar.GONE);
									mainImageView.setVisibility(ImageView.VISIBLE);
									mainImageView.setImageBitmap(cachedImage);
								}
                               //************** Add Property to the database ****************//
								right_btn.setOnClickListener(new OnClickListener() 
								{	

									public void onClick(View v) 
									{	
										try 
										{
											propertyInfo.dh = new DataHelper(propertyInfo);
											//propertyInfo.dh.deleteAll();
											propertyInfo.dh.insert(propertyTitle,propertyImage,propertyPlace,id);
																
											new AlertDialog.Builder(propertyInfo)
											.setMessage("Property sucessfully saved")
											.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
											{
												public void onClick(DialogInterface dialog, int which) 
												{
													// TODO Auto-generated method stub
													propertyInfo.finish();
												}
											}).show();
											
										}catch(SQLiteConstraintException e)
										 {		
											Log.d("Error:::", "error name - " + e.toString()+"error message"+e.getMessage());
											new AlertDialog.Builder(propertyInfo)
											.setMessage("This House is already added...")
											.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

												public void onClick(DialogInterface dialog, int which) {
													// TODO Auto-generated method stub
													propertyInfo.finish();
												}
											}).show();
										}
										catch(IllegalStateException e)
										{
											e.printStackTrace();
											Log.d("Error:::", "error name - " + e.toString()+"error message"+e.getMessage());
																					
										}

									}

								});


							} catch (MalformedURLException e) 
							{
								Log.d("Error:::", "error name - " + e.toString()+"error message"+e.getMessage());
								e.printStackTrace();
							}

							if(propertyJsonObject.getString("sub_pict1").length()>0)
							{
								ViewHolder holder=new ViewHolder();
								holder.picUrl1="http://"+propertyJsonObject.getString("sub_pict1");
								holder.picCaption1=propertyJsonObject.getString("sub_pict1_cap");
								DATA.add(holder);
							}
							if(propertyJsonObject.getString("sub_pict2").length()>0)
							{
								ViewHolder holder1=new ViewHolder();
								holder1.picUrl1="http://"+propertyJsonObject.getString("sub_pict2");
								holder1.picCaption1=propertyJsonObject.getString("sub_pict2_cap");
								DATA.add(holder1);
							}
							if(propertyJsonObject.getString("sub_pict3").length()>0)
							{
								ViewHolder holder1=new ViewHolder();
								holder1.picUrl1="http://"+propertyJsonObject.getString("sub_pict3");
								holder1.picCaption1=propertyJsonObject.getString("sub_pict3_cap");
								DATA.add(holder1);
							}							
						}						
					}
					if(DATA.size()>0)
					{
						// Reference the Gallery view
						Gallery gallery=(Gallery)propertyInfo.findViewById(R.id.property_info_gallery);
						
						// Set the adapter to our custom adapter (below)
						gallery.setAdapter(new ImageAdapter(propertyInfo));
						gallery.setOnItemClickListener(propertyInfo);
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					if(!MyUtility.isNetworkAvailable(propertyInfo))
					{
						MyUtility.showAlert("Unable to connect to internet, Please make sure you are conneted to data service or wifi.", propertyInfo);
					}
					else
					{
						MyUtility.showAlert("Unable to fetch content, please try again.", propertyInfo);
					}
				}
			}
		});
	}


	public class ImageAdapter extends BaseAdapter 
	{
		int mGalleryItemBackground;
		LayoutInflater inflater;		

		public ImageAdapter(Context c) 
		{
			inflater=LayoutInflater.from(c);

			// See res/values/attrs.xml for the <declare-styleable> that defines
			// Gallery1.
			TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);
			mGalleryItemBackground = a.getResourceId(
					R.styleable.Gallery1_android_galleryItemBackground, 0);
			a.recycle();
		}

		public int getCount() {
			return DATA.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		//******* This method is used for the Image uploading *******//
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			System.out.println("DATA Size: "+DATA.size());			
			final ViewHolder temp=(ViewHolder)DATA.get(position);
			if(convertView==null)
			{
				convertView=inflater.inflate(R.layout.property_info_gallery_layout, null);
			}

			final ImageView i=(ImageView)convertView.findViewById(R.id.property_info_pic1);
			i.setImageBitmap(null);			
			final ProgressBar spinner=(ProgressBar)convertView.findViewById(R.id.property_info_pic1_spinner);
			TextView pic1Caption=(TextView)convertView.findViewById(R.id.property_info_pic1_text);
			Bitmap cachedImage;
			try {
				cachedImage = imageLoader.loadImage(temp.picUrl1, new ImageLoadedListener() 
				{
					public void imageLoaded(Bitmap imageBitmap) 
					{
						spinner.setVisibility(ProgressBar.GONE);
						i.setVisibility(ImageView.VISIBLE);
						i.setImageBitmap(imageBitmap);
						notifyDataSetChanged(); 						
					}
				});
				if(cachedImage!=null)
				{
					spinner.setVisibility(ProgressBar.GONE);
					i.setVisibility(ImageView.VISIBLE);
					i.setImageBitmap(cachedImage);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i.setScaleType(ImageView.ScaleType.FIT_XY);

			// The preferred Gallery item background
			i.setBackgroundResource(mGalleryItemBackground);
			pic1Caption.setText(temp.picCaption1);

			return convertView;
		}
	}
	public void onClick(View v) 
	{
		if(v.getId()==backButton.getId())
		{
			propertyInfo.finish();
		}
		else if(v.getId()==homeButton.getId())
		{
			Intent i=new Intent(propertyInfo,JapanMap.class);
			propertyInfo.startActivity(i);		

		}
	}
	class ViewHolder
	{
		String picUrl1,picCaption1;

	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {		
		ViewHolder temp=(ViewHolder)DATA.get(arg2);
		Intent intent=new Intent(propertyInfo, OpenImage.class);
		intent.putExtra("img_url", temp.picUrl1);
		propertyInfo.startActivity(intent);
	}


}
