package com.akiyaBank;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.util.MyUtility;
/*
 * This Activity is showing the Book marks contents
 */
public class BookMarkScreen extends CustomMainScreen implements OnClickListener,Runnable,OnItemClickListener {

	BookMarkScreen bookMarkScreenObj;
	public CustomAdapter mAdapter;	
	Button backButton;		
	public DataHelper dh;
	public ArrayList<String> mItems = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);		
		this.bookMarkScreenObj=this;
		RelativeLayout container=(RelativeLayout)this.findViewById(R.id.container);
		LayoutInflater mInflater = LayoutInflater.from(this);		
		RelativeLayout setRelativeLayout=(RelativeLayout) mInflater.inflate(R.layout.bookmark, null);
		container.addView(setRelativeLayout);	
		backButton=(Button)this.findViewById(R.id.BookMarks_backBtn);
		backButton.setOnClickListener(this);
		
		//*********** Retrieved the database values *************//
		new Thread(bookMarkScreenObj).start();   

	}

	public void run() 
	{
		// ************ Called when database is required    **************//

		this.runOnUiThread(new Runnable()
		{
			public void run() 
			{				
				try
				{
					//************** Fetch data base values  ********************//
					mAdapter.dataBaseValues(bookMarkScreenObj);

				}catch(SQLiteException ex)
				{
					ex.printStackTrace();
					Log.d("Error:::", "error name - " + ex.toString()+"error message"+ex.getMessage());
					
					new AlertDialog.Builder(bookMarkScreenObj)
					.setMessage("No House added Yet...")
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							bookMarkScreenObj.finish();
						}
					}).show();

				}
				//********** Run to the length of the database list  ****************//
				for (int i = 0; i<mAdapter.listLength; i++)
				{				
					mItems.add(Integer.toString(i));				
				}

				mAdapter = new CustomAdapter(bookMarkScreenObj, mItems);

				GridView g =(GridView)bookMarkScreenObj.findViewById(R.id.myGrid);
				g.setAdapter(mAdapter);
				registerForContextMenu(g);
				
				//****************  ClickListener for the Property selected  ********************//
				g.setOnItemClickListener(new OnItemClickListener()
				{
					public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

						int val=position;	//**** Position Value of Property selected****//					
						int arrayPos=val;   

						//**** Fetch the property from database at that position ****//
						int propertyIdValue = mAdapter.propertyID[arrayPos];						
						String propertyTitleValue=mAdapter.propertyName[arrayPos]; 
						
						
						//**** Send the values to the property Info page for detail description of property ****//
						Intent intent=new Intent(bookMarkScreenObj, PropertyInfo.class);
						intent.putExtra("Id", propertyIdValue+"");
						intent.putExtra("title",propertyTitleValue);
						bookMarkScreenObj.startActivity(intent);						

					}
				});
			}
		});
	}

	public void onClick(View v) {
		//************ Called when back button is pressed ****************//
		if(v.getId()==backButton.getId())
		{			
			this.finish();
		}
	}


	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	{

		super.onCreateContextMenu(menu, v, menuInfo);
		
		//************** Shake the grid view when press for long time ******************//
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		findViewById(R.id.myGrid).startAnimation(shake);	
		
		menu.setHeaderTitle("Context Menu");
		AdapterContextMenuInfo cmi = (AdapterContextMenuInfo) menuInfo;		
		menu.add(1, cmi.position, 0, "Delete");
		menu.add(2, cmi.position, 0, "Rearrange");

	}

	public boolean onContextItemSelected(MenuItem item)
	{

		GridView g = (GridView) findViewById(R.id.myGrid);
		String s = (String) g.getItemAtPosition(item.getItemId());
		
		//**** Position Value of Property selected****//
		int val=Integer.parseInt(s);		
		int arrayPos=val;	
		
		//**** Fetch the property from database at that position ****//
		int v = mAdapter.ids[arrayPos];	

		switch (item.getGroupId())
		{		
			case 1:
				//************ Delete Data for that position from database *************//
				try
				{
					bookMarkScreenObj.dh = new DataHelper(bookMarkScreenObj);
					bookMarkScreenObj.dh.deleteNote(v);
					mItems=new ArrayList<String>();
					
					//*********** Retrieved the database values *************//
					new Thread(bookMarkScreenObj).start();   
				}
				catch (IllegalStateException e) 
				{
					e.printStackTrace();
					Log.d("Error:::", "error name - " + e.toString()+"error message"+e.getMessage());
										
				}			
				break;
	
			case 2:
				//************ Shorting is applied or properties are arranged according to the name *************//
				mItems=new ArrayList<String>();
				MyUtility.permissionSetter=0;
				
				//*********** Retrieved the database values *************//
				new Thread(bookMarkScreenObj).start();   
				break;
		}
		return true;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}
}
