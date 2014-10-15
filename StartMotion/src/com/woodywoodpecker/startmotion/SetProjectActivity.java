package com.woodywoodpecker.startmotion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class SetProjectActivity extends Activity {
	
	private ListView list;
	private CustomListViewAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_project);
	 List<RowItem> rowItems = new ArrayList<RowItem>();

			this.list=(ListView) findViewById(R.id.listView1);	        
		RowItem item= new RowItem();
		item.setFrameNumber(0);
		//item.setImage(R.drawable.ic_launcher);
		RowItem item2= new RowItem();
		item2.setFrameNumber(1);
		//item2.setImage(R.drawable.ic_launcher);
		
		rowItems.add(item);
		rowItems.add(item2);
		
		//this.adapter = new CustomListViewAdapter(this,
               //R.layout.list_item, rowItems);
       // this.list.setAdapter(adapter);
        
       
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_project, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	 @SuppressLint("SdCardPath") public void onClick(View v) {
	        Bitmap[] bitmaps = getBitmaps();
	        new SaveGifTask().execute(bitmaps);	      
	    }
	 
	public Bitmap[] getBitmaps(){
    	Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
    
    	Bitmap[] array= new Bitmap[]{largeIcon};
    	
    	return array;
    }
	
	private class SaveGifTask extends AsyncTask<Bitmap, Void, Void> {

		@Override
		protected Void doInBackground(Bitmap... params) {
			
			Bitmap[] bitmaps = params;
			File outputFile = new File("/sdcard/Pictures/test.gif");
	        FileOutputStream fos = null;
	        try {
	            fos = new FileOutputStream(outputFile);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }

	        if (fos != null) {
	            AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
	            gifEncoder.start(fos);

	            for (Bitmap bitmap : bitmaps) {
	                gifEncoder.addFrame(bitmap);
	            }

	            gifEncoder.finish();
	        }
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.i("Asynk","saved");
		}
       
    }
}
