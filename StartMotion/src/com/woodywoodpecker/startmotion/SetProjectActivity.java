package com.woodywoodpecker.startmotion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SetProjectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_project);
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
