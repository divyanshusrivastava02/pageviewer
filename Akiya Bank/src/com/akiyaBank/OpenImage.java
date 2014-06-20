package com.akiyaBank;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class OpenImage extends Activity 
{
	ImageThreadLoader imageLoader;
	String pic_url;
	OpenImage openImage;
	WebView webView;
	ProgressDialog dialog;
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		setContentView(R.layout.open_image);		
		imageLoader=new ImageThreadLoader(this);		
		webView=(WebView)findViewById(R.id.open_image_webView);
		pic_url=getIntent().getStringExtra("img_url");			
		webView.setWebViewClient(new HelloWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);		
		openUrl();

	}
	private class HelloWebViewClient extends WebViewClient
	{
		public boolean shouldOverrideUrlLoading(WebView view,String url)
		{		
			view.loadUrl(url);
			return true;
		}	
	}
	public void openUrl()
	{			
		webView.loadUrl(pic_url);
	}
	//*******back button coding ********//
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if((keyCode==KeyEvent.KEYCODE_BACK)&&(webView.canGoBack()))
		{
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


}
