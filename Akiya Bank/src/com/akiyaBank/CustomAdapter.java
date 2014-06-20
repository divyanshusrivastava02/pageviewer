package com.akiyaBank;


import java.net.MalformedURLException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.akiyaBank.ImageThreadLoader.ImageLoadedListener;
import com.util.MyUtility;

public class CustomAdapter extends BaseAdapter  
{
	public Context mContext;
	public ArrayList<String> mItems;
	ImageThreadLoader imageLoader;		
	public static ArrayList<Akiya> list = new ArrayList<Akiya>();	
	public static int listLength;	
	int[] ids; // Stored the id
	int[] propertyID; // Stored the unique property IDs coming from response
	String[] propertyName; // Stored the property name

	public CustomAdapter(Context c, ArrayList<String> items)
	{		
		mContext = c;
		mItems = items;
		ids = new int[list.size()];
		propertyID=new int[list.size()];
		propertyName=new String[list.size()];
		imageLoader=new ImageThreadLoader(c);	// For Image loading   
	}	

	public int getCount() {		
		return mItems.size();
	}

	public Object getItem(int position) {		
		return mItems.get(position);
	}

	public long getItemId(int position) {		
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;		
		Akiya objAkiya=new Akiya();
		if (view == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.grid_items, null);
			TextView textView = (TextView)view.findViewById(R.id.grid_item_text);
			TextView textView2 = (TextView)view.findViewById(R.id.grid_item_text2);
			final ImageView imageView = (ImageView)view.findViewById(R.id.grid_item_image);
			imageView.setImageBitmap(null);	
			
			try
			{
				//*********** Retrieving property data and fixing it at that position ************//
				int i = position;			
				{		
					try
					{
						objAkiya=list.get(i);
					}
					catch (IndexOutOfBoundsException e)
					{
						e.printStackTrace();
						Log.d("Error:::", "error name - " + e.toString()+"error message"+e.getMessage());
						new AlertDialog.Builder(mContext)
						.setMessage("No vacent Houses ")
						.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int which)
							{								
								((Activity) mContext).finish();
							}
						}).show();
					}


					ids[i]= Integer.parseInt(objAkiya.id);  //Retrieving the ID
					propertyID[i]=Integer.parseInt(objAkiya.propertyId);  //Retrieving the unique Property IDs
					propertyName[i]=objAkiya.propertyTitle; //Retrieving the unique Property Title
					textView.setText("[ "+objAkiya.propertyPlace+" ]");  //Retrieving the unique Property Place
					textView2.setText(objAkiya.propertyTitle);
					String imageUrl=objAkiya.propertyImage;  //Retrieving the unique Property Image url
					final String picUrl="http://"+imageUrl;	

					//********* Retrieving the image from URL  ***********//
					Bitmap cachedImage;
					try 
					{			
						cachedImage = imageLoader.loadImage(picUrl, new ImageLoadedListener() 
						{
							public void imageLoaded(Bitmap imageBitmap) 
							{											
								imageView.setVisibility(ImageView.VISIBLE);
								imageView.setImageBitmap(imageBitmap);
								notifyDataSetChanged(); 
							}
						});
						if(cachedImage!=null)
						{									
							imageView.setVisibility(ImageView.VISIBLE);
							imageView.setImageBitmap(cachedImage);
						}
					} 					
					catch (MalformedURLException e) 
					{				
						e.printStackTrace();
						Log.d("Error:::", "error name - " + e.toString()+"error message"+e.getMessage());
					}


				}
			}catch(NumberFormatException e){
				e.printStackTrace();
				Log.d("Error:::", "error name - " + e.toString()+"error message"+e.getMessage());
				
			}

		}
		return view;
	}

	//*************** Retrieving the Database values *********************//
	public static void dataBaseValues(Activity context)
	{
		list.clear();
		DataBaseHelper objDataBaseHelper = new DataBaseHelper(context);
		SQLiteDatabase data=null;
		objDataBaseHelper.openDataBase();
		data=objDataBaseHelper.myDataBase;

		Cursor c= null;
		try
		{ 
			if(MyUtility.permissionSetter==1)
			{
				//********* Query for fetching the values from table **********//
				c=data.query("akiyaDatabaseTable",new String[]{"id","propertyTitle","propertyImage","propertyPlace","propertyId"},null,null,null,null,null);
			}
			else
			{
				//********* Query for fetching the values from table and arranging it according to name **********//
				c=data.query("akiyaDatabaseTable",new String[]{"id","propertyTitle","propertyImage","propertyPlace","propertyId"},null,null,null,null,"propertyTitle");
			}
			/* Check if our result was valid. */

			if (c != null) 
			{
				/*checks if first value exists*/
				if(c.moveToFirst())
				{
					int i = 0;
					/* Loop through all Results */
					do {
						if(i<c.getCount())
						{

							/* Add current Entry to results. */							
							Akiya akiyaObj = new Akiya();
							akiyaObj.id=c.getString(0)+"";							
							akiyaObj.propertyTitle = c.getString(1);								
							akiyaObj.propertyImage = c.getString(2);	
							akiyaObj.propertyPlace = c.getString(3);
							akiyaObj.propertyId = c.getString(4);
							list.add(akiyaObj);  
							listLength=list.size();						
						}
						i++;
					} while (c.moveToNext());
				}
			}

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			Log.d("Error:::", "error name - " + e.toString()+"error message"+e.getMessage());
			
		}
		finally
		{			
			if(objDataBaseHelper != null)
			{
				objDataBaseHelper.close();
			}
			c.close();			
		}

	}


}