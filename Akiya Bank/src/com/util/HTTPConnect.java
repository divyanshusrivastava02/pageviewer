package com.util;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

public class HTTPConnect 
{
	public HTTPConnect()
	{
	}
	public String openHttpConnection(String urlStr) 
	{
		try {

			//InputStream in = null;
			StringBuffer s=new StringBuffer("");			
			Log.v("URL","URL_HIT::"+urlStr);
			int resCode = -1;
			URL url = new URL(urlStr);
			URLConnection urlConn = url.openConnection();
			if (!(urlConn instanceof HttpURLConnection)) 
			{
				throw new IOException ("URL is not an Http URL");
			}
			HttpURLConnection httpConn = (HttpURLConnection)urlConn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			resCode = httpConn.getResponseCode();
			if (resCode == HttpURLConnection.HTTP_OK) 
			{
				InputStream input = httpConn.getInputStream();
				byte[] data = new byte[256];
				int len = 0;
				int size = 0;
				StringBuffer raw = new StringBuffer();
				while ( -1 != (len = input.read(data)) )
				{
					raw.append(new String(data, 0, len));
					size += len;
				}             
				s = raw;
				input.close();
			} 
			return s.toString();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Log.d("Error::", "error name - " + e.toString());
			Log.d("Error::", "error message - " + e.getMessage());

		}

		return "";
	}
}