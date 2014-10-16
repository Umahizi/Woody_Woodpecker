package com.woodywoodpecker.startmotion.imageslist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.woodywoodpecker.startmotion.AnimatedGifEncoder;
import com.woodywoodpecker.startmotion.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ListImagesActivity extends Activity {
	ListView list;
	LazyImageLoadAdapter adapter;
	ArrayList<String> stringList;
	MyReceiver myReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_images);
		stringList = new ArrayList<String>();
		// stringList
		// .add("http://androidexample.com/media/webservice/LazyListView_images/image0.png");
		// stringList
		// .add("http://androidexample.com/media/webservice/LazyListView_images/image1.png");

		list = (ListView) findViewById(R.id.listImages);

		// Create custom adapter for listview
		adapter = new LazyImageLoadAdapter(this, stringList);

		// Set adapter to listview
		list.setAdapter(adapter);

		myReceiver = new MyReceiver();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(DatabaseService.MY_ACTION);
		registerReceiver(myReceiver, intentFilter);

		Intent intent = new Intent(ListImagesActivity.this,
				DatabaseService.class);
		startService(intent);

		Button b = (Button) findViewById(R.id.cacheButton);
		b.setOnClickListener(listener);
	}

	@Override
	public void onDestroy() {
		// Remove adapter refference from list
		list.setAdapter(null);
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		unregisterReceiver(myReceiver);
		super.onStop();
	}

	public OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {

			// Refresh cache directory downloaded images
			//adapter.imageLoader.clearCache();
			//adapter.notifyDataSetChanged();
			//Bitmap[] bitmaps = new Bitmap[stringList.size()];
			String[] stringArray= new String[stringList.size()];
			stringArray=stringList.toArray(stringArray);
			new BitmapLoader().execute(stringArray);
		}
	};
	
	

	public void onItemClick(int mPosition) {
		String tempValues = stringList.get(mPosition);

		Toast.makeText(ListImagesActivity.this, "Image URL : " + tempValues,
				Toast.LENGTH_LONG).show();
	}
	
	/*public void createGif(){
		//Bitmap[] bitmaps = getBitmaps();
		new SaveGifTask().execute(getBitmaps());
	}*/

	private class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {

			String datapassed = arg1
					.getStringExtra(DatabaseService.DATA_PASSED);

			if (!stringList.contains(datapassed)) {
				stringList.add(datapassed);

				adapter.notifyDataSetChanged();
				// Toast.makeText(
				// MainActivity.this,
				// "Triggered by Service!\n" + "Data passed: "
				// + String.valueOf(datapassed), Toast.LENGTH_LONG)
				// .show();
			}
		}
	}
	
	/*public Bitmap[] getBitmaps() {
		//Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
				//R.drawable.ic_launcher);
		Bitmap[] array = new Bitmap[this.stringList.size()];
		for(int i=0; i<array.length;i++ ){
			try{
			    String url1 = this.stringList.get(i);
			    URL ulrn = new URL(url1);
			    HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
			    InputStream is = con.getInputStream();
			    Bitmap bmp = BitmapFactory.decodeStream(is);
			    array[i]=bmp;
			}
			    catch(Exception e) {
			}
		}
		
		return array;
	}*/
	
	private class BitmapLoader extends AsyncTask<String,Void, Bitmap[]>{

		//Bitmap[] array = new Bitmap[stringList.size()];
		ArrayList<Bitmap> array=new ArrayList<Bitmap>();
		@Override
		protected Bitmap[] doInBackground(String... params) {
			String[] urls=params;
			for(String string : params){
				try{
				    String url1 = string;
				    URL ulrn = new URL(url1);
				    HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
				    InputStream is = con.getInputStream();
				    Bitmap bmp = BitmapFactory.decodeStream(is);
				    array.add(bmp);
				}
				    catch(Exception e) {
				}
			}
			Bitmap[] result = new Bitmap[array.size()];
			result=array.toArray(result);
			return result;
		}
		

		@Override
		protected void onPostExecute(Bitmap[] result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			new SaveGifTask().execute(result);
		}
		
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
			Log.i("Asynk", "saved");
		}

	}
}