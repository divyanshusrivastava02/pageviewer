package com.akiyaBank;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.util.MyUtility;
/*
 * This Activity is showing the Search result
 */
public class SearchResultScreen extends CustomMainScreen implements Runnable,OnClickListener
{
	SearchResultScreen searchResultScreen;
	LayoutInflater inflater;
	ProgressDialog dialog;
	ArrayList<ViewHolder> DATA;
	Button backButton;
	String keyword,valueSwitcher,url;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.searchResultScreen=this;
		inflater=searchResultScreen.getLayoutInflater();		
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		RelativeLayout searchResultScreenLayout=(RelativeLayout)inflater.inflate(R.layout.search_list_layout, null);
		container.addView(searchResultScreenLayout);
		backButton=(Button)searchResultScreen.findViewById(R.id.search_list_back_button);
		backButton.setOnClickListener(searchResultScreen);	
		DATA=new ArrayList<ViewHolder>();		
		dialog=MyUtility.progressDialog(searchResultScreen, getResources().getString(R.string.please_wait));
		dialog.show();
		new Thread(searchResultScreen).start();
		keyword=getIntent().getStringExtra("keyword");	
		valueSwitcher=getIntent().getStringExtra("valueSwitcherKeyword");	

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
			searchResultScreen.runOnUiThread(new Runnable() 
			{				

				public void run() 
				{
					dialog.dismiss();
					if(!MyUtility.isNetworkAvailable(searchResultScreen))
					{
						MyUtility.showAlert("No Network.", searchResultScreen);
					}
					else
					{
						MyUtility.showAlert("There is some problem, please try again.", searchResultScreen);
					}
				}
			});
		}
	}

	public void createList() throws Exception
	{
		DATA.clear();	
		if(valueSwitcher!=null)
		{
			url=MyUtility.serverAddressForSearchByKeywords+keyword;				
		}
		else 
		{	
			url=MyUtility.serverAddressForSearchByAddress+keyword;
		}

		//************* URL through encoded method  *******************//
		StringBuffer s=new StringBuffer("");
		ArrayList<NameValuePair> form = new ArrayList<NameValuePair>();
		form.add(new BasicNameValuePair("KEYWORD",keyword));
		try
		{
			final DefaultHttpClient client = new DefaultHttpClient();
			final HttpPost method = new HttpPost(new URI(url));
			method.setEntity(new UrlEncodedFormEntity(form, "UTF-8"));
			final HttpResponse res = client.execute(method);
			InputStream input = res.getEntity().getContent();
			byte[] data = new byte[256];
			int len = 0;
			int size = 0;
			StringBuffer raw = new StringBuffer();
			while ( -1 != (len = input.read(data)) )
			{
				raw.append(new String(data, 0, len));
				size += len;
			}             
			s=raw;
			input.close();
		}catch (SocketException e) 
		{
			Log.d("Error:::in SearchResult.class", "error name - " + e.toString()+"Message"+e.getMessage());
		}
		catch (UnknownHostException e)
		{
			Log.d("Error:::in SearchResult.class", "error name - " + e.toString()+"Message"+e.getMessage());
		}
		Log.v("Data::",s.toString());	     
		final JSONObject searchJsonObject=new JSONObject(s.toString());
		final String result=searchJsonObject.getString("result");
		Log.v("RESULT:::",result);	

		searchResultScreen.runOnUiThread(new Runnable() 
		{			

			public void run() 
			{
				try
				{
					dialog.dismiss();				
					if(searchJsonObject.getString("result").equals("success"))
					{
						System.out.println("Working");
						int total=searchJsonObject.getInt("total");	
						Log.v("TOTAL ELEMENTS",total+"");						
						for(int i=1;i<=total;i++)
						{						
							JSONObject jasonObject=searchJsonObject.getJSONObject(i+"");


							//******** Storing all the values in the View holder *********//
							ViewHolder holder=new ViewHolder();
							holder.propertyName=jasonObject.getString("name");	
							holder.urlValue=jasonObject.getString("url");
							holder.Area=jasonObject.getString("area");
							holder.id=jasonObject.getString("id");
							Log.v("ID Values",holder.id);
							DATA.add(holder);

						}
					}
					//************* If the search result not found***********************//
					else if(searchJsonObject.getString("result").equals("no data found"))
					{
						System.out.println("NO DATA FOUND:::::");
						//MyUtility.showAlert("Data not found.", searchResultScreen);
						new AlertDialog.Builder(searchResultScreen)
						.setMessage(getResources().getString(R.string.Property_not_Found))
						.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								searchResultScreen.finish();
							}
						}).show();
					}					
				}
				catch(Exception ex) //**** If JSON Exception encountered
				{
					ex.printStackTrace();
					Log.d("Error:::in SearchResult.class", "error name - " + ex.toString());
					Log.d("Error:::in SearchResult.class", "error message - " + ex.getMessage());

					if(!MyUtility.isNetworkAvailable(searchResultScreen))
					{
						MyUtility.showAlert("No Network.", searchResultScreen);
					}
					else
					{
						MyUtility.showAlert("No data found, please try again.", searchResultScreen);

					}
				}
				ListView searchListView=(ListView)searchResultScreen.findViewById(R.id.search_list_listview);
				EfficientAdapter efficientAdapter=new EfficientAdapter(searchResultScreen, DATA);
				searchListView.setAdapter(efficientAdapter);
				efficientAdapter.notifyDataSetChanged();
				searchListView.invalidate();
			}
		});
	}

	class ViewHolder
	{
		int userID;
		String urlValue;
		String propertyName,Area;
		String id;		
	}

	//********** Holds the data values **************//
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

		//*********** The UI of search ***************//
		public View getView(int position,View convertView, ViewGroup parent) 
		{
			final ViewHolder temp=(ViewHolder)DATA.get(position);
			if(convertView==null)
			{
				convertView=inflater.inflate(R.layout.search_list_row_layout, null);
			}
			((TextView)convertView.findViewById(R.id.search_list_row_title)).setText(temp.propertyName);
			((TextView)convertView.findViewById(R.id.search_list_row_place)).setText(temp.Area);
			((RelativeLayout)convertView.findViewById(R.id.search_list_row_container)).setOnClickListener(new OnClickListener() 
			{	
				public void onClick(View v) 
				{
					Intent intent=new Intent(searchResultScreen, PropertyInfo.class);
					intent.putExtra("Id", temp.id);
					intent.putExtra("title", temp.propertyName);
					searchResultScreen.startActivity(intent);
				}
			});
			return convertView;
		}		
	}

	public static Object fetch(String address) throws MalformedURLException,IOException 
	{
		//********** Fetches the url address***************//
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==backButton.getId())
		{
			searchResultScreen.finish();
		}

	}


}